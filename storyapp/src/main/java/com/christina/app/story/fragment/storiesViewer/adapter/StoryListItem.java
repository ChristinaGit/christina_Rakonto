package com.christina.app.story.fragment.storiesViewer.adapter;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.christina.api.story.model.Story;
import com.christina.common.contract.Contracts;
import com.christina.common.view.recyclerView.StableListItem;

public final class StoryListItem implements StableListItem {
    public StoryListItem(final @NonNull Story story) {
        Contracts.requireNonNull(story, "story == null");

        _story = story;
    }

    @NonNull
    public final Story getStory() {
        return _story;
    }

    @Override
    public long getId() {
        return getStory().getId();
    }

    @Nullable
    public String getStoryName() {
        return getStory().getName();
    }

    @Nullable
    public Uri getStoryPreviewUri() {
        return getStory().getPreviewUri();
    }

    @Nullable
    public String getStoryText() {
        return getStory().getText();
    }

    @NonNull
    private final Story _story;
}
