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

@SuppressWarnings("PublicField")
public final class StoriesItemViewHolder extends AbstractRecyclerViewHolder {
    @NonNull
    public final CardView cardView;

    @NonNull
    public final Button editStoryView;

    @NonNull
    public final Button shareStoryView;

    @NonNull
    public final TextView storyNameView;

    @NonNull
    public final ImageView storyPreviewView;

    @NonNull
    public final TextView storyTextView;

    public StoriesItemViewHolder(@NonNull final View itemView) {
        super(itemView);

        Contracts.requireNonNull(itemView, "itemView == null");

        cardView = (CardView) itemView.findViewById(R.id.story_card);
        editStoryView = (Button) itemView.findViewById(R.id.edit_story);
        shareStoryView = (Button) itemView.findViewById(R.id.share_story);
        storyNameView = (TextView) itemView.findViewById(R.id.story_name);
        storyPreviewView = (ImageView) itemView.findViewById(R.id.story_preview);
        storyTextView = (TextView) itemView.findViewById(R.id.story_text);

        Contracts.requireNonNull(cardView);
        Contracts.requireNonNull(editStoryView);
        Contracts.requireNonNull(shareStoryView);
        Contracts.requireNonNull(storyPreviewView);
        Contracts.requireNonNull(storyNameView);
        Contracts.requireNonNull(storyTextView);
    }
}
