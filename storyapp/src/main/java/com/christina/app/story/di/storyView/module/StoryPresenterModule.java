package com.christina.app.story.di.storyView.module;

import android.support.annotation.NonNull;

import com.christina.app.story.di.storyView.StoryViewScope;
import com.christina.app.story.manager.content.StoryContentObserverManager;
import com.christina.app.story.manager.message.MessageManager;
import com.christina.app.story.manager.navigation.StoryNavigator;
import com.christina.app.story.presentation.StoriesViewerPresenter;
import com.christina.common.contract.Contracts;

import dagger.Module;
import dagger.Provides;

@Module
@StoryViewScope
public final class StoryPresenterModule {
    @Provides
    @StoryViewScope
    @NonNull
    public final StoriesViewerPresenter provideStoriesViewerPresenter(
        @NonNull final StoryNavigator storyNavigator,
        @NonNull final MessageManager messageManager,
        @NonNull final StoryContentObserverManager storyContentObserverManager) {
        Contracts.requireNonNull(storyNavigator, "storyNavigator == null");
        Contracts.requireNonNull(messageManager, "messageManager == null");
        Contracts.requireNonNull(
            storyContentObserverManager,
            "storyContentObserverManager == null");

        return new StoriesViewerPresenter(
            storyNavigator,
            messageManager,
            storyContentObserverManager);
    }
}
