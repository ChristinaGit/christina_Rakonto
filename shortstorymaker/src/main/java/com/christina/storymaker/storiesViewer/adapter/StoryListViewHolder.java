package com.christina.storymaker.storiesViewer.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.christina.common.contract.Contracts;
import com.christina.common.view.recyclerView.AbstractRecyclerViewHolder;
import com.christina.storymaker.R;

public final class StoryListViewHolder extends AbstractRecyclerViewHolder {
    @NonNull
    public final CardView cardView;

    @NonNull
    public final Button expandStoryView;

    @NonNull
    public final Button shareStoryView;

    @NonNull
    public final TextView storyNameView;

    @NonNull
    public final ImageView storyPreviewView;

    public StoryListViewHolder(@NonNull final View itemView) {
        super(itemView);

        Contracts.requireNonNull(itemView, "itemView == null");

        cardView = (CardView) itemView.findViewById(R.id.story_card);
        expandStoryView = (Button) itemView.findViewById(R.id.explore_story);
        shareStoryView = (Button) itemView.findViewById(R.id.share_story);
        storyNameView = (TextView) itemView.findViewById(R.id.story_name);
        storyPreviewView = (ImageView) itemView.findViewById(R.id.story_preview);

        Contracts.requireNonNull(cardView);
        Contracts.requireNonNull(expandStoryView);
        Contracts.requireNonNull(shareStoryView);
        Contracts.requireNonNull(storyPreviewView);
        Contracts.requireNonNull(storyNameView);
    }
}
