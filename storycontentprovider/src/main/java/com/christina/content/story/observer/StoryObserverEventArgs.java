package com.christina.content.story.observer;

import com.christina.common.data.model.Model;
import com.christina.common.event.EventArgs;

public class StoryObserverEventArgs extends EventArgs {
    public static final StoryObserverEventArgs EMPTY = new StoryObserverEventArgs();

    public StoryObserverEventArgs() {
        this(Model.NO_ID);
    }

    public StoryObserverEventArgs(final long id) {
        _id = id;
    }

    public final long getId() {
        return _id;
    }

    private final long _id;
}
