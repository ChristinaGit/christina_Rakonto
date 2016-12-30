package com.christina.app.story.di.storyView.module;

import android.support.annotation.NonNull;

import com.christina.app.story.di.qualifier.PresenterNames;
import com.christina.app.story.di.qualifier.ScopeNames;
import com.christina.app.story.di.storyView.StoryViewScope;
import com.christina.app.story.core.manager.ServiceManager;
import com.christina.app.story.presentation.StoriesViewerPresenter;
import com.christina.app.story.presentation.StoryEditorPresenter;
import com.christina.app.story.view.StoriesViewerPresentableView;
import com.christina.app.story.view.StoryEditorPresentableView;
import com.christina.common.contract.Contracts;
import com.christina.common.view.presentation.Presenter;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
@StoryViewScope
public final class StoryViewPresenterModule {
    @Named(PresenterNames.STORIES_VIEWER)
    @Provides
    @StoryViewScope
    @NonNull
    public final Presenter<StoriesViewerPresentableView> provideStoriesViewerPresenter(
        @Named(ScopeNames.ACTIVITY) @NonNull final ServiceManager serviceManager) {
        Contracts.requireNonNull(serviceManager, "serviceManager == null");

        return new StoriesViewerPresenter(serviceManager);
    }

    @Named(PresenterNames.STORY_EDITOR)
    @Provides
    @StoryViewScope
    @NonNull
    public final Presenter<StoryEditorPresentableView> provideStoryEditorPresenter(
        @Named(ScopeNames.ACTIVITY) @NonNull final ServiceManager serviceManager) {
        Contracts.requireNonNull(serviceManager, "serviceManager == null");

        return new StoryEditorPresenter(serviceManager);
    }
}
