package com.christina.storymaker.storiesViewer.adapter;

import android.support.annotation.NonNull;

import com.christina.common.contract.Contracts;
import com.christina.common.view.recyclerView.ListItem;
import com.christina.content.story.model.Story;

public final class StoriesItem implements ListItem {
    public StoriesItem(final @NonNull Story story) {
        Contracts.requireNonNull(story, "story == null");

        _story = story;
    }

    @NonNull
    public final Story getStory() {
        return _story;
    }

    @Override
    public long getId() {
        return _story.getId();
    }

    @NonNull
    private final Story _story;
}
