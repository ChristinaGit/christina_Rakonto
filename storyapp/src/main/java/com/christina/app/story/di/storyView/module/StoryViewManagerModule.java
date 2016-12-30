package com.christina.app.story.di.storyView.module;

import android.content.ContentResolver;
import android.support.annotation.NonNull;

import com.christina.api.story.observer.StoryContentObserver;
import com.christina.app.story.di.qualifier.ScopeNames;
import com.christina.app.story.di.storyView.StoryViewScope;
import com.christina.app.story.core.manager.ServiceManager;
import com.christina.app.story.core.manager.content.StoryContentObserverManager;
import com.christina.app.story.core.manager.content.StoryDaoManager;
import com.christina.app.story.core.manager.message.MessageManager;
import com.christina.app.story.core.manager.navigation.StoryNavigator;
import com.christina.app.story.core.manager.rx.AndroidRxManager;
import com.christina.app.story.core.manager.rx.RxManager;
import com.christina.common.contract.Contracts;
import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.android.ActivityEvent;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
@StoryViewScope
public final class StoryViewManagerModule {
    public StoryViewManagerModule(
        @NonNull final LifecycleProvider<ActivityEvent> lifecycleProvider,
        @NonNull final StoryNavigator storyNavigator,
        @NonNull final MessageManager messageManager) {
        Contracts.requireNonNull(lifecycleProvider, "lifecycleProvider == null");
        Contracts.requireNonNull(storyNavigator, "storyNavigator == null");
        Contracts.requireNonNull(messageManager, "messageManager == null");

        _lifecycleProvider = lifecycleProvider;
        _storyNavigator = storyNavigator;
        _messageManager = messageManager;
    }

    @Named(ScopeNames.ACTIVITY)
    @Provides
    @StoryViewScope
    @NonNull
    public final LifecycleProvider<ActivityEvent> provideLifecycleProvider() {
        return _lifecycleProvider;
    }

    @Provides
    @StoryViewScope
    @NonNull
    public final MessageManager provideMessageManager() {
        return _messageManager;
    }

    @Named(ScopeNames.ACTIVITY)
    @Provides
    @StoryViewScope
    @NonNull
    public final RxManager provideRxManager(
        @Named(ScopeNames.ACTIVITY) @NonNull
        final LifecycleProvider<ActivityEvent> lifecycleProvider) {
        Contracts.requireNonNull(lifecycleProvider, "lifecycleProvider == null");

        return new AndroidRxManager<>(lifecycleProvider);
    }

    @Named(ScopeNames.ACTIVITY)
    @Provides
    @StoryViewScope
    @NonNull
    public final ServiceManager provideServiceManager(
        @NonNull final StoryNavigator storyNavigator,
        @Named(ScopeNames.ACTIVITY) @NonNull final RxManager rxManager,
        @NonNull final MessageManager messageManager,
        @NonNull final StoryDaoManager storyDaoManager,
        @NonNull final StoryContentObserverManager storyContentObserverManager) {
        Contracts.requireNonNull(storyNavigator, "storyNavigator == null");
        Contracts.requireNonNull(rxManager, "rxManager == null");
        Contracts.requireNonNull(messageManager, "messageManager == null");
        Contracts.requireNonNull(storyDaoManager, "storyDaoManager == null");
        Contracts.requireNonNull(storyContentObserverManager,
                                 "storyContentObserverManager == null");
        return new ServiceManager(storyNavigator,
                                  rxManager,
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
    public final StoryNavigator provideStoryNavigator() {
        return _storyNavigator;
    }

    @NonNull
    private final LifecycleProvider<ActivityEvent> _lifecycleProvider;

    @NonNull
    private final MessageManager _messageManager;

    @NonNull
    private final StoryNavigator _storyNavigator;
}
