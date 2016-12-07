package com.christina.app.story.di.storyViewFragment.module;

import android.support.annotation.NonNull;

import com.christina.app.story.di.storyViewFragment.StoryViewFragmentScope;
import com.christina.app.story.manager.ServiceManager;
import com.christina.app.story.presentation.StoriesListPresenter;
import com.christina.app.story.presentation.StoryTextEditorPresenter;
import com.christina.common.contract.Contracts;

import dagger.Module;
import dagger.Provides;

@Module
@StoryViewFragmentScope
public final class StoryViewFragmentPresenterModule {
    @Provides
    @StoryViewFragmentScope
    @NonNull
    public final StoriesListPresenter provideStoriesViewerPresenter(
        @NonNull final ServiceManager serviceManager) {
        Contracts.requireNonNull(serviceManager, "serviceManager == null");

        return new StoriesListPresenter(serviceManager);
    }

    @Provides
    @StoryViewFragmentScope
    @NonNull
    public final StoryTextEditorPresenter provideStoryTextEditorPresenter(
        @NonNull final ServiceManager serviceManager) {
        Contracts.requireNonNull(serviceManager, "serviceManager == null");

        return new StoryTextEditorPresenter(serviceManager);
    }
}
