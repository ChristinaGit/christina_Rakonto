package com.christina.api.story.observer;

import com.christina.common.data.model.Model;
import com.christina.common.event.eventArgs.EventArgs;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
public class StoryObserverEventArgs extends EventArgs {
    public static final StoryObserverEventArgs EMPTY = new StoryObserverEventArgs();

    public StoryObserverEventArgs() {
        this(Model.NO_ID);
    }

    public StoryObserverEventArgs(final long id) {
        _id = id;
    }

    @Getter
    private final long _id;
}
