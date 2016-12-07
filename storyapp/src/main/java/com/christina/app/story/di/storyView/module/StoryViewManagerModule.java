package com.christina.app.story.di.storyView.module;

import android.content.ContentResolver;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;

import com.christina.api.story.observer.StoryContentObserver;
import com.christina.app.story.di.qualifier.ForApplication;
import com.christina.app.story.di.storyView.StoryViewScope;
import com.christina.app.story.manager.ServiceManager;
import com.christina.app.story.manager.content.StoryContentObserverManager;
import com.christina.app.story.manager.content.StoryDaoManager;
import com.christina.app.story.manager.asyncTask.ActivityStoryTaskManager;
import com.christina.app.story.manager.asyncTask.StoryTaskManager;
import com.christina.app.story.manager.message.MessageManager;
import com.christina.app.story.manager.navigation.StoryNavigator;
import com.christina.common.contract.Contracts;

import dagger.Module;
import dagger.Provides;

@Module
@StoryViewScope
public final class StoryViewManagerModule {
    public StoryViewManagerModule(
        @NonNull final LoaderManager loaderManager,
        @NonNull final StoryNavigator storyNavigator,
        @NonNull final MessageManager messageManager) {
        Contracts.requireNonNull(loaderManager, "loaderManager == null");
        Contracts.requireNonNull(storyNavigator, "storyNavigator == null");
        Contracts.requireNonNull(messageManager, "messageManager == null");

        _loaderManager = loaderManager;
        _storyNavigator = storyNavigator;
        _messageManager = messageManager;
    }

    @Provides
    @StoryViewScope
    @NonNull
    public final LoaderManager provideLoaderManager() {
        return _loaderManager;
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
    public final ServiceManager provideServiceManager(
        @NonNull final StoryNavigator storyNavigator,
        @NonNull final StoryTaskManager storyTaskManager,
        @NonNull final MessageManager messageManager,
        @NonNull final StoryDaoManager storyDaoManager,
        @NonNull final StoryContentObserverManager storyContentObserverManager) {
        Contracts.requireNonNull(storyNavigator, "storyNavigator == null");
        Contracts.requireNonNull(storyTaskManager, "storyLoaderManager == null");
        Contracts.requireNonNull(messageManager, "messageManager == null");
        Contracts.requireNonNull(storyDaoManager, "storyDaoManager == null");
        Contracts.requireNonNull(storyContentObserverManager,
                                 "storyContentObserverManager == null");
        return new ServiceManager(
            storyNavigator, storyTaskManager,
            messageManager,
            storyDaoManager,
            storyContentObserverManager);
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
    public final StoryTaskManager provideStoryTaskManager(
        @NonNull @ForApplication final Context context,
        @NonNull final LoaderManager loaderManager,
        @NonNull final StoryDaoManager storyDaoManager) {
        Contracts.requireNonNull(context, "context == null");
        Contracts.requireNonNull(loaderManager, "loaderManager == null");
        Contracts.requireNonNull(storyDaoManager, "storyDaoManager == null");

        return new ActivityStoryTaskManager(context, loaderManager, storyDaoManager);
    }

    @Provides
    @StoryViewScope
    @NonNull
    public final StoryNavigator provideStoryNavigator() {
        return _storyNavigator;
    }

    @NonNull
    private final LoaderManager _loaderManager;

    @NonNull
    private final MessageManager _messageManager;

    @NonNull
    private final StoryNavigator _storyNavigator;
}
