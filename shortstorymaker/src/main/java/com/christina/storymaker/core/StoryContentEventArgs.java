package com.christina.storymaker.core;

import com.christina.common.data.model.Model;
import com.christina.common.event.EventArgs;

public class StoryContentEventArgs extends EventArgs {
    public static final StoryContentEventArgs EMPTY = new StoryContentEventArgs();

    public StoryContentEventArgs() {
        this(Model.NO_ID);
    }

    public StoryContentEventArgs(final long id) {
        _id = id;
    }

    public final long getId() {
        return _id;
    }

    private final long _id;
}
