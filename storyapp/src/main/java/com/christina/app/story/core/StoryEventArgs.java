package com.christina.app.story.core;

import com.christina.api.story.model.Story;
import com.christina.common.event.eventArgs.EventArgs;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
public class StoryEventArgs extends EventArgs {
    public static final StoryEventArgs EMPTY = new StoryEventArgs();

    public StoryEventArgs() {
        this(Story.NO_ID);
    }

    public StoryEventArgs(final long storyId) {

        _storyId = storyId;
    }

    @Getter
    private final long _storyId;
}
