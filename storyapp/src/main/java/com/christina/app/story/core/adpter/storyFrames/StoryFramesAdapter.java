package com.christina.app.story.core.adpter.storyFrames;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.christina.api.story.dao.storyFrame.StoryFrameFullProjection;
import com.christina.api.story.model.StoryFrame;
import com.christina.app.story.R;
import com.christina.common.contract.Contracts;
import com.christina.common.view.recyclerView.adapter.DataCursorRecyclerViewAdapter;

import java.util.Objects;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;

@Accessors(prefix = "_")
public final class StoryFramesAdapter
    extends DataCursorRecyclerViewAdapter<StoryFrame, StoryFrameViewHolder> {
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
        final long itemId;

        final val dataCursor = getDataCursor();
        if (dataCursor != null && dataCursor.moveToPosition(position)) {
            itemId = getStoryFrameFullProjection().getId(dataCursor);
        } else {
            itemId = StoryFrame.NO_ID;
        }

        return itemId;
    }

    @Override
    protected void onBindViewHolder(
        @NonNull final StoryFrameViewHolder holder,
        @NonNull final StoryFrame item,
        final int position) {
        Contracts.requireNonNull(holder, "holder == null");
        Contracts.requireNonNull(item, "item == null");

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
            .error(R.drawable.im_loading_image_placeholder)
            .centerCrop()
            .into(holder.storyFrameImageView);
    }

    @Getter(value = AccessLevel.PROTECTED, lazy = true)
    @NonNull
    private final StoryFrameFullProjection _storyFrameFullProjection =
        new StoryFrameFullProjection();

    @Getter
    @Nullable
    private String _storyText;
}
