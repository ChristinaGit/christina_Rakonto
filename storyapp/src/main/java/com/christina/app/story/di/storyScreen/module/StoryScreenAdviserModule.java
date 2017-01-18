package com.christina.app.story.di.storyScreen.module;

import android.support.annotation.NonNull;

import com.christina.app.story.di.qualifier.ScopeNames;
import com.christina.app.story.di.storyScreen.StoryScreenScope;
import com.christina.common.adviser.ResourceAdviser;
import com.christina.common.contract.Contracts;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
@StoryScreenScope
public final class StoryScreenAdviserModule {
    public StoryScreenAdviserModule(@NonNull final ResourceAdviser resourceAdviser) {
        Contracts.requireNonNull(resourceAdviser, "resourceAdviser == null");

        _resourceAdviser = resourceAdviser;
    }

    @Named(ScopeNames.SCREEN)
    @Provides
    @StoryScreenScope
    @NonNull
    public final ResourceAdviser provideResourceAdviser() {
        return _resourceAdviser;
    }

    @NonNull
    private final ResourceAdviser _resourceAdviser;
}
