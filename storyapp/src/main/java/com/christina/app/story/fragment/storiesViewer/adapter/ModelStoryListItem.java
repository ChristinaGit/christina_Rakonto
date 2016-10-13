package com.christina.app.story.fragment.storiesViewer.adapter;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.christina.common.contract.Contracts;
import com.christina.api.story.model.Story;

public final class ModelStoryListItem implements StoryListItem {
    public ModelStoryListItem(final @NonNull Story story) {
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
    @Override
    public String getStoryName() {
        return getStory().getName();
    }

    @Nullable
    @Override
    public Uri getStoryPreviewUri() {
        return getStory().getPreviewUri();
    }

    @Nullable
    @Override
    public String getStoryText() {
        return getStory().getText();
    }

    @NonNull
    private final Story _story;
}
