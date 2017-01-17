package com.christina.app.story.di.storyApplication.module;

import android.support.annotation.NonNull;

import com.christina.app.story.core.manager.file.StoryFileManager;
import com.christina.app.story.di.storyApplication.StoryApplicationScope;
import com.christina.common.contract.Contracts;

import dagger.Module;
import dagger.Provides;

@Module
@StoryApplicationScope
public final class StoryApplicationManagerModule {
    public StoryApplicationManagerModule(@NonNull final StoryFileManager storyFileManager) {
        Contracts.requireNonNull(storyFileManager, "storyFileManager == null");

        _storyFileManager = storyFileManager;
    }

    @Provides
    @StoryApplicationScope
    @NonNull
    public final StoryFileManager provideStoryFileManager() {
        return _storyFileManager;
    }

    @NonNull
    private final StoryFileManager _storyFileManager;
}
