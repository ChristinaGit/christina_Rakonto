package com.christina.app.story.di.storySubscreen.module;

import android.support.annotation.NonNull;

import com.christina.app.story.core.manager.ServiceManager;
import com.christina.app.story.di.qualifier.PresenterNames;
import com.christina.app.story.di.qualifier.ScopeNames;
import com.christina.app.story.di.storySubscreen.StorySubscreenScope;
import com.christina.app.story.presentation.StoriesListPresenter;
import com.christina.app.story.presentation.StoryFramesEditorPresenter;
import com.christina.app.story.presentation.StoryTextEditorPresenter;
import com.christina.app.story.view.StoriesListScreen;
import com.christina.app.story.view.StoryFramesEditorScreen;
import com.christina.app.story.view.StoryTextEditorScreen;
import com.christina.common.contract.Contracts;
import com.christina.common.presentation.Presenter;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
@StorySubscreenScope
public final class StorySubscreenPresenterModule {
    @Named(PresenterNames.STORIES_LIST)
    @Provides
    @StorySubscreenScope
    @NonNull
    public final Presenter<StoriesListScreen> provideStoriesViewerPresenter(
        @Named(ScopeNames.SUBSCREEN) @NonNull final ServiceManager serviceManager) {
        Contracts.requireNonNull(serviceManager, "serviceManager == null");

        return new StoriesListPresenter(serviceManager);
    }

    @Named(PresenterNames.STORY_FRAMES_EDITOR)
    @Provides
    @StorySubscreenScope
    @NonNull
    public final Presenter<StoryFramesEditorScreen> provideStoryFramesEditorPresenter(
        @Named(ScopeNames.SUBSCREEN) @NonNull final ServiceManager serviceManager) {
        Contracts.requireNonNull(serviceManager, "serviceManager == null");

        return new StoryFramesEditorPresenter(serviceManager);
    }

    @Named(PresenterNames.STORY_TEXT_EDITOR)
    @Provides
    @StorySubscreenScope
    @NonNull
    public final Presenter<StoryTextEditorScreen> provideStoryTextEditorPresenter(
        @Named(ScopeNames.SUBSCREEN) @NonNull final ServiceManager serviceManager) {
        Contracts.requireNonNull(serviceManager, "serviceManager == null");

        return new StoryTextEditorPresenter(serviceManager);
    }
}
