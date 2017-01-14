package com.christina.app.story.di.storyScreen.module;

import android.support.annotation.NonNull;

import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.android.ActivityEvent;

import com.christina.app.story.di.qualifier.ScopeNames;
import com.christina.app.story.di.storyScreen.StoryScreenScope;
import com.christina.common.contract.Contracts;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
@StoryScreenScope
public final class StoryScreenRxModule {
    public StoryScreenRxModule(@NonNull final LifecycleProvider<ActivityEvent> lifecycleProvider) {
        Contracts.requireNonNull(lifecycleProvider, "lifecycleProvider == null");

        _lifecycleProvider = lifecycleProvider;
    }

    @Named(ScopeNames.SCREEN)
    @Provides
    @StoryScreenScope
    @NonNull
    public final LifecycleProvider<ActivityEvent> provideLifecycleProvider() {
        return _lifecycleProvider;
    }

    @NonNull
    private final LifecycleProvider<ActivityEvent> _lifecycleProvider;
}
