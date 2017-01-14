package com.christina.app.story.di.storyScreen.module;

import android.support.annotation.NonNull;

import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.android.ActivityEvent;

import com.christina.app.story.core.RealmIdGenerator;
import com.christina.app.story.core.manager.ServiceManager;
import com.christina.app.story.core.manager.data.AndroidRealmManger;
import com.christina.app.story.core.manager.data.RealmManager;
import com.christina.app.story.core.manager.message.ActivityMessageManager;
import com.christina.app.story.core.manager.message.MessageManager;
import com.christina.app.story.core.manager.navigation.ActivityNavigator;
import com.christina.app.story.core.manager.navigation.StoryNavigator;
import com.christina.app.story.core.manager.resource.ResourceManager;
import com.christina.app.story.core.manager.rx.AndroidRxManager;
import com.christina.app.story.core.manager.rx.RxManager;
import com.christina.app.story.di.qualifier.ScopeNames;
import com.christina.app.story.di.storyScreen.StoryScreenScope;
import com.christina.common.contract.Contracts;
import com.christina.common.view.observerable.ObservableActivity;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
@StoryScreenScope
public final class StoryScreenManagerModule {
    public StoryScreenManagerModule(
        @NonNull final ObservableActivity observableActivity,
        @NonNull final ResourceManager resourceManager) {
        Contracts.requireNonNull(observableActivity, "observableActivity == null");
        Contracts.requireNonNull(resourceManager, "resourceManager == null");

        _observableActivity = observableActivity;
        _resourceManager = resourceManager;
    }

    @Provides
    @StoryScreenScope
    @NonNull
    public final MessageManager provideMessageManager(
        @NonNull final ObservableActivity observableActivity) {
        Contracts.requireNonNull(observableActivity, "observableActivity == null");

        return new ActivityMessageManager(observableActivity);
    }

    @Provides
    @StoryScreenScope
    @NonNull
    public final ObservableActivity provideObservableActivity() {
        return _observableActivity;
    }

    @Named(ScopeNames.SCREEN)
    @Provides
    @StoryScreenScope
    @NonNull
    public final RealmManager provideRealmManager(
        @NonNull @Named(ScopeNames.SCREEN) final ResourceManager resourceManager,
        @NonNull final RealmIdGenerator realmIdGenerator) {
        Contracts.requireNonNull(resourceManager, "resourceManager == null");
        Contracts.requireNonNull(realmIdGenerator, "realmIdGenerator == null");

        return new AndroidRealmManger(resourceManager, realmIdGenerator);
    }

    @Named(ScopeNames.SCREEN)
    @Provides
    @StoryScreenScope
    @NonNull
    public final ResourceManager provideResourceManager() {
        return _resourceManager;
    }

    @Named(ScopeNames.SCREEN)
    @Provides
    @StoryScreenScope
    @NonNull
    public final RxManager provideRxManager(
        @NonNull @Named(ScopeNames.SCREEN)
        final LifecycleProvider<ActivityEvent> lifecycleProvider) {
        Contracts.requireNonNull(lifecycleProvider, "lifecycleProvider == null");

        return new AndroidRxManager<>(lifecycleProvider);
    }

    @Named(ScopeNames.SCREEN)
    @Provides
    @StoryScreenScope
    @NonNull
    public final ServiceManager provideServiceManager(
        @NonNull @Named(ScopeNames.SCREEN) final ResourceManager resourceManager,
        @NonNull final StoryNavigator storyNavigator,
        @NonNull @Named(ScopeNames.SCREEN) final RealmManager realmManager,
        @NonNull @Named(ScopeNames.SCREEN) final RxManager rxManager,
        @NonNull final MessageManager messageManager) {
        Contracts.requireNonNull(resourceManager, "resourceManager == null");
        Contracts.requireNonNull(storyNavigator, "storyNavigator == null");
        Contracts.requireNonNull(realmManager, "realmManager == null");
        Contracts.requireNonNull(rxManager, "rxManager == null");
        Contracts.requireNonNull(messageManager, "messageManager == null");

        return new ServiceManager(resourceManager,
                                  storyNavigator,
                                  realmManager,
                                  rxManager,
                                  messageManager);
    }

    @Provides
    @StoryScreenScope
    @NonNull
    public final StoryNavigator provideStoryNavigator(
        @NonNull final ObservableActivity observableActivity,
        @NonNull @Named(ScopeNames.SCREEN) final ResourceManager resourceManager) {
        Contracts.requireNonNull(observableActivity, "observableActivity == null");
        Contracts.requireNonNull(resourceManager, "resourceManager == null");

        return new ActivityNavigator(resourceManager, observableActivity);
    }

    @NonNull
    private final ObservableActivity _observableActivity;

    @NonNull
    private final ResourceManager _resourceManager;
}