package com.christina.app.story.di.storyView.module;

import android.content.ContentResolver;
import android.support.annotation.NonNull;

import com.christina.api.story.observer.StoryContentObserver;
import com.christina.app.story.di.storyView.StoryViewScope;
import com.christina.app.story.manager.content.StoryContentObserverManager;
import com.christina.app.story.manager.message.MessageManager;
import com.christina.app.story.manager.navigation.StoryNavigator;
import com.christina.common.contract.Contracts;

import dagger.Module;
import dagger.Provides;

@Module
@StoryViewScope
public final class StoryViewManagerModule {
    public StoryViewManagerModule(
        @NonNull final StoryNavigator storyNavigator,
        @NonNull final MessageManager messageManager) {
        Contracts.requireNonNull(storyNavigator, "storyNavigator == null");
        Contracts.requireNonNull(messageManager, "messageManager == null");

        _storyNavigator = storyNavigator;
        _messageManager = messageManager;
    }

    @Provides
    @StoryViewScope
    @NonNull
    public final MessageManager provideMessageManager() {
        return _messageManager;
    }

    @Provides
    @StoryViewScope
    @NonNull
    public final StoryContentObserverManager provideStoryContentObserverManager(
        @NonNull final ContentResolver contentResolver,
        @NonNull final StoryContentObserver storyContentObserver) {
        Contracts.requireNonNull(contentResolver, "contentResolver == null");
        Contracts.requireNonNull(storyContentObserver, "storyContentObserver == null");

        return new StoryContentObserverManager(contentResolver, storyContentObserver);
    }

    @Provides
    @StoryViewScope
    @NonNull
    public final StoryNavigator provideStoryNavigator() {
        return _storyNavigator;
    }

    @NonNull
    private final MessageManager _messageManager;

    @NonNull
    private final StoryNavigator _storyNavigator;
}
