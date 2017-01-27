package moe.christina.app.rakonto.di.screen.module;

import android.support.annotation.NonNull;

import moe.christina.app.rakonto.core.manager.StoryServiceManager;
import moe.christina.app.rakonto.di.qualifier.PresenterNames;
import moe.christina.app.rakonto.di.qualifier.ScopeNames;
import moe.christina.app.rakonto.di.screen.RakontoScreenScope;
import moe.christina.app.rakonto.presenter.StoriesViewerPresenter;
import moe.christina.app.rakonto.presenter.StoryEditorPresenter;
import moe.christina.app.rakonto.screen.StoriesViewerScreen;
import moe.christina.app.rakonto.screen.StoryEditorScreen;
import moe.christina.common.contract.Contracts;
import moe.christina.common.mvp.presenter.Presenter;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
@RakontoScreenScope
public final class RakontoScreenPresenterModule {
    @Named(PresenterNames.STORIES_VIEWER)
    @Provides
    @RakontoScreenScope
    @NonNull
    public final Presenter<StoriesViewerScreen> provideStoriesViewerPresenter(
        @NonNull @Named(ScopeNames.SCREEN) final StoryServiceManager storyServiceManager) {
        Contracts.requireNonNull(storyServiceManager, "storyServiceManager == null");

        return new StoriesViewerPresenter(storyServiceManager);
    }

    @Named(PresenterNames.STORY_EDITOR)
    @Provides
    @RakontoScreenScope
    @NonNull
    public final Presenter<StoryEditorScreen> provideStoryEditorPresenter(
        @NonNull @Named(ScopeNames.SCREEN) final StoryServiceManager storyServiceManager) {
        Contracts.requireNonNull(storyServiceManager, "storyServiceManager == null");

        return new StoryEditorPresenter(storyServiceManager);
    }
}
