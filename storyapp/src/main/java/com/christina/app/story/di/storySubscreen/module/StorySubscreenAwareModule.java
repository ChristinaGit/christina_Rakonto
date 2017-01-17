package com.christina.app.story.di.storySubscreen.module;

import android.support.annotation.NonNull;

import com.christina.app.story.di.qualifier.ScopeNames;
import com.christina.app.story.di.storySubscreen.StorySubscreenScope;
import com.christina.common.aware.ResourceAware;
import com.christina.common.contract.Contracts;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
@StorySubscreenScope
public final class StorySubscreenAwareModule {
    public StorySubscreenAwareModule(@NonNull final ResourceAware resourceAware) {
        Contracts.requireNonNull(resourceAware, "resourceAware == null");

        _resourceAware = resourceAware;
    }

    @Named(ScopeNames.SUBSCREEN)
    @Provides
    @StorySubscreenScope
    @NonNull
    public final ResourceAware provideResourceAware() {
        return _resourceAware;
    }

    @NonNull
    private final ResourceAware _resourceAware;
}
