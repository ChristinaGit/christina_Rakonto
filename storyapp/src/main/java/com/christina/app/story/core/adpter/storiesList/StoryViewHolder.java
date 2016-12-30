package com.christina.app.story.core.adpter.storiesList;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.christina.app.story.R;
import com.christina.common.contract.Contracts;
import com.christina.common.view.recyclerView.viewHolder.ExtendedRecyclerViewHolder;

import butterknife.BindView;

@SuppressWarnings("PublicField")
/*package-private*/ final class StoryViewHolder extends ExtendedRecyclerViewHolder {
    @BindView(R.id.story_card)
    @NonNull
    public CardView cardView;

    @BindView(R.id.edit_story)
    @NonNull
    public Button editStoryView;

    @BindView(R.id.share_story)
    @NonNull
    public Button shareStoryView;

    @BindView(R.id.story_name)
    @NonNull
    public TextView storyNameView;

    @BindView(R.id.story_preview)
    @NonNull
    public ImageView storyPreviewView;

    @BindView(R.id.story_text)
    @NonNull
    public TextView storyTextView;

    public StoryViewHolder(@NonNull final View itemView) {
        super(Contracts.requireNonNull(itemView, "itemView == null"));

        bindViews();

        Contracts.requireNonNull(cardView);
        Contracts.requireNonNull(editStoryView);
        Contracts.requireNonNull(shareStoryView);
        Contracts.requireNonNull(storyPreviewView);
        Contracts.requireNonNull(storyNameView);
        Contracts.requireNonNull(storyTextView);
    }
}
