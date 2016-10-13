package com.christina.app.story.fragment.storyTextEditor.loader;

import android.support.annotation.Nullable;

import com.christina.api.story.model.Story;

public final class StoryLoaderResult {
    public StoryLoaderResult(@Nullable final Story story) {
        _story = story;
    }

    @Nullable
    public final Story getStory() {
        return _story;
    }

    @Nullable
    private final Story _story;
}
