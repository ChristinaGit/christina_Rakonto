package com.christina.app.story.di.storyApplication.module;

import android.support.annotation.NonNull;

import com.christina.app.story.core.manager.file.StoryFileManager;
import com.christina.app.story.core.manager.search.GoogleStorySearchManager;
import com.christina.app.story.core.manager.search.StorySearchManager;
import com.christina.app.story.di.storyApplication.StoryApplicationScope;
import com.christina.common.contract.Contracts;
import com.christina.common.control.manager.task.AndroidTaskManager;
import com.christina.common.control.manager.task.TaskManager;

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

    @Provides
    @StoryApplicationScope
    @NonNull
    public final StorySearchManager provideStorySearchManager() {

        return new GoogleStorySearchManager();
    }

    @Provides
    @StoryApplicationScope
    @NonNull
    public final TaskManager provideTaskManager() {
        return new AndroidTaskManager();
    }

    @NonNull
    private final StoryFileManager _storyFileManager;
}
