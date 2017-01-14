package com.christina.app.story.di.storySubscreen.module;

import android.support.annotation.NonNull;

import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.android.FragmentEvent;

import com.christina.app.story.di.qualifier.ScopeNames;
import com.christina.app.story.di.storySubscreen.StorySubscreenScope;
import com.christina.common.contract.Contracts;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
@StorySubscreenScope
public final class StorySubscreenRxModule {
    public StorySubscreenRxModule(
        @NonNull final LifecycleProvider<FragmentEvent> lifecycleProvider) {
        Contracts.requireNonNull(lifecycleProvider, "lifecycleProvider == null");

        _lifecycleProvider = lifecycleProvider;
    }

    @Named(ScopeNames.SUBSCREEN)
    @Provides
    @StorySubscreenScope
    @NonNull
    public final LifecycleProvider<FragmentEvent> provideLifecycleProvider() {
        return _lifecycleProvider;
    }

    @NonNull
    private final LifecycleProvider<FragmentEvent> _lifecycleProvider;
}
