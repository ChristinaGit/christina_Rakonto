package moe.christina.app.rakonto.core.adpter.storyFrames;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.val;

import com.bumptech.glide.Glide;

import moe.christina.app.rakonto.model.ui.UIStoryFrame;
import moe.christina.common.ConstantBuilder;
import moe.christina.common.contract.Contracts;
import moe.christina.common.extension.view.recyclerView.adapter.RecyclerViewListAdapter;

import java.util.List;
import java.util.Objects;

@Accessors(prefix = "_")
public final class StoryFramesAdapter
    extends RecyclerViewListAdapter<UIStoryFrame, StoryFrameViewHolder> {
    private static final String _LOG_TAG = ConstantBuilder.logTag(StoryFramesAdapter.class);

    public StoryFramesAdapter() {
        setHasStableIds(true);
    }

    public final void setStoryText(@Nullable final String storyText) {
        if (!Objects.equals(_storyText, storyText)) {
            _storyText = storyText;

            notifyDataSetChanged();
        }
    }

    @Override
    public StoryFrameViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final val inflater = LayoutInflater.from(parent.getContext());
        final val view =
            inflater.inflate(moe.christina.app.rakonto.R.layout.fragment_story_frames_editor_item,
                             parent,
                             false);
        return new StoryFrameViewHolder(view);
    }

    @Override
    public final long getItemId(final int position) {
        Contracts.requireInRange(position, 0, getItemCount() - 1, new IndexOutOfBoundsException());

        return getItem(position).getId();
    }

    @Override
    protected void onBindViewHolder(
        @NonNull final StoryFrameViewHolder holder,
        @NonNull final UIStoryFrame item,
        final int position) {
        super.onBindViewHolder(Contracts.requireNonNull(holder, "holder == null"),
                               Contracts.requireNonNull(item, "item == null"),
                               Contracts.requireInRange(position,
                                                        0,
                                                        getItemCount() - 1,
                                                        new IndexOutOfBoundsException()));

        final String storyFrameText;

        final val storyText = getStoryText();
        if (!TextUtils.isEmpty(storyText)) {
            final int storyTextLength = storyText.length();

            final int textStart = item.getTextStartPosition();
            final int textEnd = item.getTextEndPosition();
            if (storyTextLength >= textEnd && textEnd > textStart) {
                storyFrameText = storyText.substring(textStart, textEnd);
            } else {
                storyFrameText = null;
            }
        } else {
            storyFrameText = null;
        }

        holder.storyFrameTextView.setText(storyFrameText);

        Glide
            .with(holder.getContext())
            .load(item.getImageUri())
            .animate(moe.christina.app.rakonto.R.anim.fade_in_long)
            .centerCrop()
            .into(holder.storyFrameImageView);
    }

    @Getter(onMethod = @__(@Override))
    @Setter
    @Nullable
    private List<UIStoryFrame> _items;

    @Getter
    @Nullable
    private String _storyText;
}
