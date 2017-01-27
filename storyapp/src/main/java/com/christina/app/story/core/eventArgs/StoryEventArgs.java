package com.christina.app.story.core.eventArgs;

import android.support.annotation.Nullable;

import lombok.Getter;
import lombok.experimental.Accessors;

import com.christina.common.event.eventArgs.EventArgs;

@Accessors(prefix = "_")
public class StoryEventArgs extends EventArgs {
    public StoryEventArgs(@Nullable final Long storyId) {
        _storyId = storyId;
    }

    @Getter
    @Nullable
    private final Long _storyId;
}
