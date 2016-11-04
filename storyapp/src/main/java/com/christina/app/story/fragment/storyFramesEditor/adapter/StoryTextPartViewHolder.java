package com.christina.app.story.fragment.storyFramesEditor.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.christina.app.story.R;
import com.christina.common.contract.Contracts;
import com.christina.common.view.recyclerView.BaseRecyclerViewHolder;

@SuppressWarnings("PublicField")
public final class StoryTextPartViewHolder extends BaseRecyclerViewHolder {
    @NonNull
    public final TextView storyFrameTextView;

    public StoryTextPartViewHolder(@NonNull final View itemView) {
        super(itemView);

        Contracts.requireNonNull(itemView, "itemView == null");

        storyFrameTextView = (TextView) itemView.findViewById(R.id.story_frame_text);

        Contracts.requireNonNull(storyFrameTextView, "storyFrameTextView == null");
    }
}
