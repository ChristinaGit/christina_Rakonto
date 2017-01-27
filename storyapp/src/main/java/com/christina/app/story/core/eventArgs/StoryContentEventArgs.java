package com.christina.app.story.core.eventArgs;

import android.support.annotation.NonNull;

import lombok.Getter;
import lombok.experimental.Accessors;

import com.christina.app.story.model.ui.UIStory;
import com.christina.common.event.eventArgs.EventArgs;

@Accessors(prefix = "_")
public class StoryContentEventArgs extends EventArgs {
    public StoryContentEventArgs(@NonNull final UIStory story) {
        _story = story;
    }

    @Getter
    @NonNull
    private final UIStory _story;
}
