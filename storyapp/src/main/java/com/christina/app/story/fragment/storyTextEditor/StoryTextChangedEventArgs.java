package com.christina.app.story.fragment.storyTextEditor;

import android.support.annotation.Nullable;

import com.christina.common.event.EventArgs;

public final class StoryTextChangedEventArgs extends EventArgs {
    public StoryTextChangedEventArgs(@Nullable final String oldStoryText,
        @Nullable final String newStoryText) {
        _newStoryText = newStoryText;
        _oldStoryText = oldStoryText;
    }

    @Nullable
    public final String getNewStoryText() {
        return _newStoryText;
    }

    @Nullable
    public final String getOldStoryText() {
        return _oldStoryText;
    }

    @Nullable
    private final String _newStoryText;

    @Nullable
    private final String _oldStoryText;
}
