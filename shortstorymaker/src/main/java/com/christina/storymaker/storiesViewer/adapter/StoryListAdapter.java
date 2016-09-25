package com.christina.storymaker.storiesViewer.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.christina.common.contract.Contracts;
import com.christina.common.view.recyclerView.AbstractRecyclerViewAdapter;
import com.christina.common.view.recyclerView.ListItem;
import com.christina.content.story.model.Story;
import com.christina.storymaker.R;

public final class StoryListAdapter
    extends AbstractRecyclerViewAdapter<Story, StoryListViewHolder> {

    @Override
    public StoryListViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(R.layout.story_list_viewer_item, parent, false);
        return new StoryListViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull final StoryListViewHolder holder,
                                    @NonNull final ListItem listItem, final int position) {
        Contracts.requireNonNull(holder, "holder == null");
        Contracts.requireNonNull(listItem, "listItem == null");

        if (listItem instanceof StoryListItem) {
            final Story story = ((StoryListItem) listItem).story;

            holder.storyNameView.setText(story.getText());
            Glide.with(holder.getContext())
                 .load(story.getPreviewUri())
                 .centerCrop()
                 .into(holder.storyPreviewView)
                 .getRequest()
                 .begin();
        }
    }

    @NonNull
    @Override
    protected ListItem onWrapItem(@NonNull final Story story) {
        Contracts.requireNonNull(story, "story == null");

        return new StoryListItem(story);
    }
}
