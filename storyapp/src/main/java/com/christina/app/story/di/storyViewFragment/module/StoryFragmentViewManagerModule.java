package com.christina.app.story.di.storyViewFragment.module;

import android.support.annotation.NonNull;

import com.christina.app.story.core.manager.ServiceManager;
import com.christina.app.story.core.manager.rx.AndroidRxManager;
import com.christina.app.story.core.manager.rx.RxManager;
import com.christina.app.story.di.qualifier.ScopeNames;
import com.christina.app.story.di.storyViewFragment.StoryViewFragmentScope;
import com.christina.common.contract.Contracts;
import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.android.FragmentEvent;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
@StoryViewFragmentScope
public final class StoryFragmentViewManagerModule {
    public StoryFragmentViewManagerModule(
        @NonNull final LifecycleProvider<FragmentEvent> lifecycleProvider) {
        Contracts.requireNonNull(lifecycleProvider, "lifecycleProvider == null");

        _lifecycleProvider = lifecycleProvider;
    }

    @Named(ScopeNames.FRAGMENT)
    @Provides
    @StoryViewFragmentScope
    @NonNull
    public final LifecycleProvider<FragmentEvent> provideLifecycleProvider() {
        return _lifecycleProvider;
    }

    @Named(ScopeNames.FRAGMENT)
    @Provides
    @StoryViewFragmentScope
    @NonNull
    public final RxManager provideRxManager(
        @Named(ScopeNames.FRAGMENT) @NonNull
        final LifecycleProvider<FragmentEvent> lifecycleProvider) {
        Contracts.requireNonNull(lifecycleProvider, "lifecycleProvider == null");

        return new AndroidRxManager<>(lifecycleProvider);
    }

    @Named(ScopeNames.FRAGMENT)
    @Provides
    @StoryViewFragmentScope
    @NonNull
    public final ServiceManager provideServiceManager(
        @Named(ScopeNames.ACTIVITY) @NonNull final ServiceManager serviceManager,
        @Named(ScopeNames.FRAGMENT) @NonNull final RxManager rxManager) {
        Contracts.requireNonNull(serviceManager, "serviceManager == null");
        Contracts.requireNonNull(rxManager, "rxManager == null");
        return new ServiceManager(
            serviceManager.getStoryNavigator(),
            rxManager,
            serviceManager.getMessageManager(),
            serviceManager.getStoryDaoManager(),
            serviceManager.getStoryContentObserverManager());
    }

    @NonNull
    private final LifecycleProvider<FragmentEvent> _lifecycleProvider;
}
