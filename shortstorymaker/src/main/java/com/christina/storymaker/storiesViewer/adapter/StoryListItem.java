package com.christina.storymaker.storiesViewer.adapter;

import android.support.annotation.NonNull;

import com.christina.common.contract.Contracts;
import com.christina.common.view.recyclerView.ListItem;
import com.christina.content.story.model.Story;

public final class StoryListItem implements ListItem {
    @NonNull
    public final Story story;

    public StoryListItem(final @NonNull Story story) {
        Contracts.requireNonNull(story, "story == null");

        this.story = story;
    }

    @Override
    public long getId() {
        return story.getId();
    }
}
