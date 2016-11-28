package com.christina.app.story.fragment.storyTextEditor;

import android.support.annotation.Nullable;

import com.christina.common.event.EventArgs;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
public final class StoryTextChangedEventArgs extends EventArgs {
    public StoryTextChangedEventArgs(
        @Nullable final String oldStoryText, @Nullable final String newStoryText) {
        _newStoryText = newStoryText;
        _oldStoryText = oldStoryText;
    }

    @Getter
    @Nullable
    private final String _newStoryText;

    @Getter
    @Nullable
    private final String _oldStoryText;
}
