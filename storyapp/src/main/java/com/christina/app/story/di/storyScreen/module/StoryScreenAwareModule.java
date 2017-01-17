package com.christina.app.story.di.storyScreen.module;

import android.support.annotation.NonNull;

import com.christina.app.story.di.qualifier.ScopeNames;
import com.christina.app.story.di.storyScreen.StoryScreenScope;
import com.christina.common.aware.ResourceAware;
import com.christina.common.contract.Contracts;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
@StoryScreenScope
public final class StoryScreenAwareModule {
    public StoryScreenAwareModule(@NonNull final ResourceAware resourceAware) {
        Contracts.requireNonNull(resourceAware, "resourceAware == null");

        _resourceAware = resourceAware;
    }

    @Named(ScopeNames.SCREEN)
    @Provides
    @StoryScreenScope
    @NonNull
    public final ResourceAware provideResourceAware() {
        return _resourceAware;
    }

    @NonNull
    private final ResourceAware _resourceAware;
}
