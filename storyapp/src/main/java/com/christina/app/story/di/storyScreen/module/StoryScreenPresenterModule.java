package com.christina.app.story.di.storyScreen.module;

import android.support.annotation.NonNull;

import com.christina.app.story.core.manager.StoryServiceManager;
import com.christina.app.story.di.qualifier.PresenterNames;
import com.christina.app.story.di.qualifier.ScopeNames;
import com.christina.app.story.di.storyScreen.StoryScreenScope;
import com.christina.app.story.presentation.StoriesViewerPresenter;
import com.christina.app.story.presentation.StoryEditorPresenter;
import com.christina.app.story.view.StoriesViewerScreen;
import com.christina.app.story.view.StoryEditorScreen;
import com.christina.common.contract.Contracts;
import com.christina.common.presentation.Presenter;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
@StoryScreenScope
public final class StoryScreenPresenterModule {
    @Named(PresenterNames.STORIES_VIEWER)
    @Provides
    @StoryScreenScope
    @NonNull
    public final Presenter<StoriesViewerScreen> provideStoriesViewerPresenter(
        @NonNull @Named(ScopeNames.SCREEN) final StoryServiceManager storyServiceManager) {
        Contracts.requireNonNull(storyServiceManager, "storyServiceManager == null");

        return new StoriesViewerPresenter(storyServiceManager);
    }

    @Named(PresenterNames.STORY_EDITOR)
    @Provides
    @StoryScreenScope
    @NonNull
    public final Presenter<StoryEditorScreen> provideStoryEditorPresenter(
        @NonNull @Named(ScopeNames.SCREEN) final StoryServiceManager storyServiceManager) {
        Contracts.requireNonNull(storyServiceManager, "storyServiceManager == null");

        return new StoryEditorPresenter(storyServiceManager);
    }
}
