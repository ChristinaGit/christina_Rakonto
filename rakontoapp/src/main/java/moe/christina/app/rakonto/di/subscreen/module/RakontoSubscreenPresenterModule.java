package moe.christina.app.rakonto.di.subscreen.module;

import android.support.annotation.NonNull;

import moe.christina.app.rakonto.core.manager.StoryServiceManager;
import moe.christina.app.rakonto.di.qualifier.PresenterNames;
import moe.christina.app.rakonto.di.qualifier.ScopeNames;
import moe.christina.app.rakonto.di.subscreen.RakontoSubscreenScope;
import moe.christina.app.rakonto.presenter.StoriesListPresenter;
import moe.christina.app.rakonto.presenter.StoryFramesEditorPresenter;
import moe.christina.app.rakonto.presenter.StoryTextEditorPresenter;
import moe.christina.app.rakonto.screen.StoriesListScreen;
import moe.christina.app.rakonto.screen.StoryFramesEditorScreen;
import moe.christina.app.rakonto.screen.StoryTextEditorScreen;
import moe.christina.common.contract.Contracts;
import moe.christina.common.mvp.presenter.Presenter;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
@RakontoSubscreenScope
public final class RakontoSubscreenPresenterModule {
    @Named(PresenterNames.STORIES_LIST)
    @Provides
    @RakontoSubscreenScope
    @NonNull
    public final Presenter<StoriesListScreen> provideStoriesViewerPresenter(
        @Named(ScopeNames.SUBSCREEN) @NonNull final StoryServiceManager storyServiceManager) {
        Contracts.requireNonNull(storyServiceManager, "storyServiceManager == null");

        return new StoriesListPresenter(storyServiceManager);
    }

    @Named(PresenterNames.STORY_FRAMES_EDITOR)
    @Provides
    @RakontoSubscreenScope
    @NonNull
    public final Presenter<StoryFramesEditorScreen> provideStoryFramesEditorPresenter(
        @Named(ScopeNames.SUBSCREEN) @NonNull final StoryServiceManager storyServiceManager) {
        Contracts.requireNonNull(storyServiceManager, "storyServiceManager == null");

        return new StoryFramesEditorPresenter(storyServiceManager);
    }

    @Named(PresenterNames.STORY_TEXT_EDITOR)
    @Provides
    @RakontoSubscreenScope
    @NonNull
    public final Presenter<StoryTextEditorScreen> provideStoryTextEditorPresenter(
        @Named(ScopeNames.SUBSCREEN) @NonNull final StoryServiceManager storyServiceManager) {
        Contracts.requireNonNull(storyServiceManager, "storyServiceManager == null");

        return new StoryTextEditorPresenter(storyServiceManager);
    }
}
