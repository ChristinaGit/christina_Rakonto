package com.christina.storymaker.core;

import android.support.annotation.NonNull;

import com.christina.content.story.observer.StoryContentObserver;

public interface StoryContentObserverProvider {
    @NonNull
    StoryContentObserver getStoryContentObserver();
}
