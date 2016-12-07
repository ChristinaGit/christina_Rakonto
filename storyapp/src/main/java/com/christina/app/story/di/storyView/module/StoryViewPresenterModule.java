package com.christina.app.story.di.storyView.module;

import android.support.annotation.NonNull;

import com.christina.app.story.di.storyView.StoryViewScope;
import com.christina.app.story.manager.ServiceManager;
import com.christina.app.story.presentation.StoriesViewerPresenter;
import com.christina.app.story.presentation.StoryEditorPresenter;
import com.christina.common.contract.Contracts;

import dagger.Module;
import dagger.Provides;

@Module
@StoryViewScope
public final class StoryViewPresenterModule {
    @Provides
    @StoryViewScope
    @NonNull
    public final StoriesViewerPresenter provideStoriesViewerPresenter(
        @NonNull final ServiceManager serviceManager) {
        Contracts.requireNonNull(serviceManager, "serviceManager == null");

        return new StoriesViewerPresenter(serviceManager);
    }

    @Provides
    @StoryViewScope
    @NonNull
    public final StoryEditorPresenter provideStoryEditorPresenter(
        @NonNull final ServiceManager serviceManager) {
        Contracts.requireNonNull(serviceManager, "serviceManager == null");

        return new StoryEditorPresenter(serviceManager);
    }
}
