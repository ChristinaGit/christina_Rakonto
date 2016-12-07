package com.christina.app.story.core;

import android.support.annotation.Nullable;

import com.christina.api.story.model.Story;
import com.christina.common.event.eventArgs.EventArgs;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
public class StoryContentEventArgs extends EventArgs {
    public static final StoryContentEventArgs EMPTY = new StoryContentEventArgs();

    public StoryContentEventArgs() {
        this(null);
    }

    public StoryContentEventArgs(@Nullable final Story story) {
        _story = story;
    }

    @Getter
    @Nullable
    private final Story _story;
}
