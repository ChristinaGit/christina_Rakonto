package com.christina.app.story.core;

import android.support.annotation.Nullable;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
public class StoryChangedEventArgs extends StoryEventArgs {
    public StoryChangedEventArgs(@Nullable final Long storyId) {
        super(storyId);
    }

    @Getter
    @Setter
    @Nullable
    private String _storyText;
}
