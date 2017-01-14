package com.christina.app.story.di;

import android.support.annotation.NonNull;

import com.christina.app.story.di.storyScreen.StoryScreenComponent;

public interface StoryScreenComponentProvider {
    @NonNull
    StoryScreenComponent getStoryScreenComponent();
}
