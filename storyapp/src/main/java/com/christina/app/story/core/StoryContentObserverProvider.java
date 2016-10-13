package com.christina.app.story.core;

import android.support.annotation.NonNull;

import com.christina.api.story.observer.StoryContentObserver;

public interface StoryContentObserverProvider {
    @NonNull
    StoryContentObserver getStoryContentObserver();
}
