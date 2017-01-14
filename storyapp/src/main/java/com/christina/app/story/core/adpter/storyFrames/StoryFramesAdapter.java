package com.christina.app.story.core.adpter.storyFrames;

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

import com.christina.app.story.R;
import com.christina.app.story.data.model.StoryFrame;
import com.christina.common.contract.Contracts;
import com.christina.common.view.recyclerView.adapter.RecyclerViewListAdapter;

import java.util.List;
import java.util.Objects;

@Accessors(prefix = "_")
public final class StoryFramesAdapter
    extends RecyclerViewListAdapter<StoryFrame, StoryFrameViewHolder> {
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
            inflater.inflate(R.layout.fragment_story_frames_editor_item, parent, false);
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
        @NonNull final StoryFrame item,
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
            .animate(android.R.anim.fade_in)
            .fallback(R.drawable.im_loading_image_placeholder)
            .error(R.drawable.im_loading_image_placeholder)
            .centerCrop()
            .into(holder.storyFrameImageView);
    }

    @Getter(onMethod = @__(@Override))
    @Setter
    @Nullable
    private List<StoryFrame> _items;

    @Getter
    @Nullable
    private String _storyText;
}