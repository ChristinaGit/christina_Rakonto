package moe.christina.app.rakonto.di.application.module;

import android.support.annotation.NonNull;

import moe.christina.app.rakonto.core.manager.file.StoryFileManager;
import moe.christina.app.rakonto.core.manager.search.GoogleStorySearchManager;
import moe.christina.app.rakonto.core.manager.search.StorySearchManager;
import moe.christina.app.rakonto.di.application.RakontoApplicationScope;
import moe.christina.common.contract.Contracts;
import moe.christina.common.control.manager.task.AndroidTaskManager;
import moe.christina.common.control.manager.task.TaskManager;

import dagger.Module;
import dagger.Provides;

@Module
@RakontoApplicationScope
public final class RakontoApplicationManagerModule {
    public RakontoApplicationManagerModule(@NonNull final StoryFileManager storyFileManager) {
        Contracts.requireNonNull(storyFileManager, "storyFileManager == null");

        _storyFileManager = storyFileManager;
    }

    @Provides
    @RakontoApplicationScope
    @NonNull
    public final StoryFileManager provideStoryFileManager() {
        return _storyFileManager;
    }

    @Provides
    @RakontoApplicationScope
    @NonNull
    public final StorySearchManager provideStorySearchManager() {

        return new GoogleStorySearchManager();
    }

    @Provides
    @RakontoApplicationScope
    @NonNull
    public final TaskManager provideTaskManager() {
        return new AndroidTaskManager();
    }

    @NonNull
    private final StoryFileManager _storyFileManager;
}
