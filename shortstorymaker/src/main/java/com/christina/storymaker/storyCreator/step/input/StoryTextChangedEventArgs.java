package com.christina.storymaker.storyCreator.step.input;

import android.support.annotation.Nullable;

import com.christina.common.event.EventArgs;

public final class StoryTextChangedEventArgs extends EventArgs {
    public static final StoryTextChangedEventArgs EMPTY = new StoryTextChangedEventArgs(null);

    public StoryTextChangedEventArgs() {
        this(null);
    }

    public StoryTextChangedEventArgs(@Nullable final String storyText) {
        _storyText = storyText;
    }

    @Nullable
    public final String getStoryText() {
        return _storyText;
    }

    @Nullable
    private final String _storyText;
}
