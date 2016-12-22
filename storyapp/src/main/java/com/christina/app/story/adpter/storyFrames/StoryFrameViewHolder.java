package com.christina.app.story.adpter.storyFrames;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.christina.common.contract.Contracts;
import com.christina.common.view.recyclerView.viewHolder.ExtendedRecyclerViewHolder;

@SuppressWarnings("PublicField")
public final class StoryFrameViewHolder extends ExtendedRecyclerViewHolder {
    @NonNull
    public TextView storyFrameImageView;

    @NonNull
    public TextView storyFrameTextView;

    public StoryFrameViewHolder(@NonNull final View itemView) {
        super(Contracts.requireNonNull(itemView, "itemView == null"));

        bindViews();

        Contracts.requireNonNull(storyFrameImageView, "storyFrameImageView == null");
        Contracts.requireNonNull(storyFrameTextView, "storyFrameTextView == null");
    }
}
