package com.christina.app.story.di;

import android.support.annotation.NonNull;

import com.christina.app.story.di.storyApplication.StoryApplicationComponent;

public interface StoryApplicationComponentProvider {
    @NonNull
    StoryApplicationComponent getStoryApplicationComponent();
}
