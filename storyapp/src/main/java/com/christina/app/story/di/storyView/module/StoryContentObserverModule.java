package com.christina.app.story.di.storyView.module;

import android.support.annotation.NonNull;

import com.christina.api.story.observer.StoryContentObserver;
import com.christina.app.story.di.storyView.StoryViewScope;

import dagger.Module;
import dagger.Provides;

@Module
@StoryViewScope
public final class StoryContentObserverModule {
    @Provides
    @StoryViewScope
    @NonNull
    public final StoryContentObserver provideStoryContentObserver() {
        return new StoryContentObserver();
    }
}
