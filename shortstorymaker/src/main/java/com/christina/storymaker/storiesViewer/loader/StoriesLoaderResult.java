package com.christina.storymaker.storiesViewer.loader;

import android.support.annotation.Nullable;

import com.christina.content.story.model.Story;

import java.util.List;

public final class StoriesLoaderResult {
    @Nullable
    public final List<Story> stories;

    public StoriesLoaderResult(@Nullable final List<Story> stories) {
        this.stories = stories;
    }
}
