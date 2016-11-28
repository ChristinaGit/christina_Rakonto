package com.christina.app.story.di;

import android.support.annotation.NonNull;

import com.christina.app.story.di.storyViewFragment.StoryViewFragmentComponent;

public interface StoryScreenFragmentComponentProvider {
    @NonNull
    StoryViewFragmentComponent getStoryScreenFragmentComponent();
}
