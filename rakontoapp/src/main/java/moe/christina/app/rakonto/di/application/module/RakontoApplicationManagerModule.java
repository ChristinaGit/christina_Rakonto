package moe.christina.app.rakonto.di.application.module;

import android.support.annotation.NonNull;

import moe.christina.app.rakonto.core.api.pixabay.PixabayService;
import moe.christina.app.rakonto.core.manager.file.StoryFileManager;
import moe.christina.app.rakonto.core.manager.search.StoryPixabaySearchManager;
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
    public final StorySearchManager provideStorySearchManager(
        @NonNull final PixabayService pixabayService) {
        Contracts.requireNonNull(pixabayService, "pixabayService == null");

        return new StoryPixabaySearchManager(pixabayService);
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
