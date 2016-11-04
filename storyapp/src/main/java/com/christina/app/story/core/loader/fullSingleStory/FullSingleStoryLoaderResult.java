package com.christina.app.story.core.loader.fullSingleStory;

import android.support.annotation.Nullable;

import com.christina.api.story.model.Story;
import com.christina.api.story.model.StoryFrame;

import java.util.List;

public final class FullSingleStoryLoaderResult {
    public FullSingleStoryLoaderResult(@Nullable final Story story,
        @Nullable final List<StoryFrame> storyFrames) {
        _story = story;
        _storyFrames = storyFrames;
    }

    @Nullable
    public final Story getStory() {
        return _story;
    }

    @Nullable
    public final List<StoryFrame> getStoryFrames() {
        return _storyFrames;
    }

    @Nullable
    private final Story _story;

    @Nullable
    private final List<StoryFrame> _storyFrames;
}
