package com.christina.storymaker.storiesViewer.loader;

import android.support.annotation.Nullable;

import com.christina.content.story.model.Story;

import java.util.List;

public final class StoriesLoaderResult {
    public StoriesLoaderResult(@Nullable final List<Story> stories) {
        _stories = stories;
    }

    @Nullable
    public final List<Story> getStories() {
        return _stories;
    }

    @Nullable
    private final List<Story> _stories;
}
