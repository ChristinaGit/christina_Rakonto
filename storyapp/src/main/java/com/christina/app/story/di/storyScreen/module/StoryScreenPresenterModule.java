package com.christina.app.story.di.storyScreen.module;

import android.support.annotation.NonNull;

import com.christina.app.story.core.manager.ServiceManager;
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
        @NonNull @Named(ScopeNames.SCREEN) final ServiceManager serviceManager) {
        Contracts.requireNonNull(serviceManager, "serviceManager == null");

        return new StoriesViewerPresenter(serviceManager);
    }

    @Named(PresenterNames.STORY_EDITOR)
    @Provides
    @StoryScreenScope
    @NonNull
    public final Presenter<StoryEditorScreen> provideStoryEditorPresenter(
        @NonNull @Named(ScopeNames.SCREEN) final ServiceManager serviceManager) {
        Contracts.requireNonNull(serviceManager, "serviceManager == null");

        return new StoryEditorPresenter(serviceManager);
    }
}
