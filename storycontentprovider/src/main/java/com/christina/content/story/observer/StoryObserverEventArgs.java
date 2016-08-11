package com.christina.content.story.observer;

import com.christina.common.data.model.Model;
import com.christina.common.event.EventArgs;

public class StoryObserverEventArgs extends EventArgs {
    public final long id;

    public StoryObserverEventArgs() {
        this(Model.NO_ID);
    }

    public StoryObserverEventArgs(final long id) {
        this.id = id;
    }
}
