package com.christina.app.story.di;

import android.support.annotation.NonNull;

import com.christina.app.story.di.storySubscreen.StorySubscreenComponent;

public interface StorySubscreenComponentProvider {
    @NonNull
    StorySubscreenComponent getStorySubscreenComponent();
}
