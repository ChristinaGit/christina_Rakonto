package com.christina.app.story.fragment.storyFramesEditor.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.christina.app.story.R;
import com.christina.common.contract.Contracts;
import com.christina.common.view.recyclerView.viewHolder.AbstractRecyclerViewHolder;

@SuppressWarnings("PublicField")
public final class StoryFrameViewHolder extends AbstractRecyclerViewHolder {
    @NonNull
    public final TextView storyFrameImageView;

    @NonNull
    public final TextView storyFrameTextView;

    public StoryFrameViewHolder(@NonNull final View itemView) {
        super(itemView);

        Contracts.requireNonNull(itemView, "itemView == null");

        storyFrameImageView = (TextView) itemView.findViewById(R.id.story_frame_image);
        storyFrameTextView = (TextView) itemView.findViewById(R.id.story_frame_text);

        Contracts.requireNonNull(storyFrameImageView, "storyFrameImageView == null");
        Contracts.requireNonNull(storyFrameTextView, "storyFrameTextView == null");
    }
}
