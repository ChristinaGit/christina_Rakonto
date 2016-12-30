package com.christina.app.story.core.adpter.storyFrames;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.christina.app.story.R;
import com.christina.common.contract.Contracts;
import com.christina.common.view.recyclerView.viewHolder.ExtendedRecyclerViewHolder;

import butterknife.BindView;

@SuppressWarnings("PublicField")
public final class StoryFrameViewHolder extends ExtendedRecyclerViewHolder {
    @BindView(R.id.story_frame_image)
    @NonNull
    public ImageView storyFrameImageView;

    @BindView(R.id.story_frame_text)
    @NonNull
    public TextView storyFrameTextView;

    public StoryFrameViewHolder(@NonNull final View itemView) {
        super(Contracts.requireNonNull(itemView, "itemView == null"));

        bindViews();

        Contracts.requireNonNull(storyFrameImageView, "storyFrameImageView == null");
        Contracts.requireNonNull(storyFrameTextView, "storyFrameTextView == null");
    }
}
