package com.christina.app.story.di;

import android.support.annotation.NonNull;

import com.christina.app.story.di.storyViewFragment.StoryViewFragmentComponent;

public interface StoryViewFragmentComponentProvider {
    @NonNull
    StoryViewFragmentComponent getStoryViewFragmentComponent();
}
