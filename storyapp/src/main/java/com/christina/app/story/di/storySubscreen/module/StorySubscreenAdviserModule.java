package com.christina.app.story.di.storySubscreen.module;

import android.support.annotation.NonNull;

import com.christina.app.story.di.qualifier.ScopeNames;
import com.christina.app.story.di.storySubscreen.StorySubscreenScope;
import com.christina.common.control.adviser.ResourceAdviser;
import com.christina.common.contract.Contracts;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
@StorySubscreenScope
public final class StorySubscreenAdviserModule {
    public StorySubscreenAdviserModule(@NonNull final ResourceAdviser resourceAdviser) {
        Contracts.requireNonNull(resourceAdviser, "resourceAdviser == null");

        _resourceAdviser = resourceAdviser;
    }

    @Named(ScopeNames.SUBSCREEN)
    @Provides
    @StorySubscreenScope
    @NonNull
    public final ResourceAdviser provideResourceAdviser() {
        return _resourceAdviser;
    }

    @NonNull
    private final ResourceAdviser _resourceAdviser;
}
