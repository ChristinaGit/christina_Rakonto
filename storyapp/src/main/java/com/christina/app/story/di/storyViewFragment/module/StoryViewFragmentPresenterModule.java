package com.christina.app.story.di.storyViewFragment.module;

import android.support.annotation.NonNull;

import com.christina.app.story.di.qualifier.PresenterNames;
import com.christina.app.story.di.storyViewFragment.StoryViewFragmentScope;
import com.christina.app.story.manager.ServiceManager;
import com.christina.app.story.presentation.StoriesListPresenter;
import com.christina.app.story.presentation.StoryFramesEditorPresenter;
import com.christina.app.story.presentation.StoryTextEditorPresenter;
import com.christina.app.story.view.StoriesListPresentableView;
import com.christina.app.story.view.StoryFramesEditorPresentableView;
import com.christina.app.story.view.StoryTextEditorPresentableView;
import com.christina.common.contract.Contracts;
import com.christina.common.view.presentation.Presenter;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
@StoryViewFragmentScope
public final class StoryViewFragmentPresenterModule {
    @Named(PresenterNames.STORIES_LIST)
    @Provides
    @StoryViewFragmentScope
    @NonNull
    public final Presenter<StoriesListPresentableView> provideStoriesViewerPresenter(
        @NonNull final ServiceManager serviceManager) {
        Contracts.requireNonNull(serviceManager, "serviceManager == null");

        return new StoriesListPresenter(serviceManager);
    }

    @Named(PresenterNames.STORY_FRAMES_EDITOR)
    @Provides
    @StoryViewFragmentScope
    @NonNull
    public final Presenter<StoryFramesEditorPresentableView> provideStoryFramesEditorPresenter(
        @NonNull final ServiceManager serviceManager) {
        Contracts.requireNonNull(serviceManager, "serviceManager == null");

        return new StoryFramesEditorPresenter(serviceManager);
    }

    @Named(PresenterNames.STORY_TEXT_EDITOR)
    @Provides
    @StoryViewFragmentScope
    @NonNull
    public final Presenter<StoryTextEditorPresentableView> provideStoryTextEditorPresenter(
        @NonNull final ServiceManager serviceManager) {
        Contracts.requireNonNull(serviceManager, "serviceManager == null");

        return new StoryTextEditorPresenter(serviceManager);
    }
}
