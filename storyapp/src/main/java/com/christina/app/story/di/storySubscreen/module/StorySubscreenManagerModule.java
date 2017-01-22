package com.christina.app.story.di.storySubscreen.module;

import android.support.annotation.NonNull;

import io.realm.RealmConfiguration;

import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.android.FragmentEvent;

import com.christina.app.story.core.manager.StoryServiceManager;
import com.christina.app.story.di.qualifier.ScopeNames;
import com.christina.app.story.di.storySubscreen.StorySubscreenScope;
import com.christina.common.contract.Contracts;
import com.christina.common.control.adviser.ResourceAdviser;
import com.christina.common.control.manager.realm.AndroidRealmManger;
import com.christina.common.control.manager.realm.RealmManager;
import com.christina.common.control.manager.rx.AndroidRxManager;
import com.christina.common.control.manager.rx.RxManager;
import com.christina.common.data.realm.RealmIdGenerator;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
@StorySubscreenScope
public final class StorySubscreenManagerModule {

    @Named(ScopeNames.SUBSCREEN)
    @Provides
    @StorySubscreenScope
    @NonNull
    public final RealmManager provideDataManager(
        @NonNull @Named(ScopeNames.SUBSCREEN) final ResourceAdviser resourceAdviser,
        @NonNull final RealmConfiguration realmConfiguration,
        @NonNull final RealmIdGenerator realmIdGenerator) {
        Contracts.requireNonNull(resourceAdviser, "resourceAdviser == null");
        Contracts.requireNonNull(realmConfiguration, "realmConfiguration == null");
        Contracts.requireNonNull(realmIdGenerator, "realmIdGenerator == null");

        return new AndroidRealmManger(resourceAdviser, realmConfiguration, realmIdGenerator);
    }

    @Named(ScopeNames.SUBSCREEN)
    @Provides
    @StorySubscreenScope
    @NonNull
    public final RxManager provideRxManager(
        @NonNull @Named(ScopeNames.SUBSCREEN)
        final LifecycleProvider<FragmentEvent> lifecycleProvider) {
        Contracts.requireNonNull(lifecycleProvider, "lifecycleProvider == null");

        return new AndroidRxManager<>(lifecycleProvider);
    }

    @Named(ScopeNames.SUBSCREEN)
    @Provides
    @StorySubscreenScope
    @NonNull
    public final StoryServiceManager provideStoryServiceManager(
        @NonNull @Named(ScopeNames.SCREEN) final StoryServiceManager storyServiceManager,
        @NonNull @Named(ScopeNames.SUBSCREEN) final RealmManager realmManager,
        @NonNull @Named(ScopeNames.SUBSCREEN) final RxManager rxManager) {
        Contracts.requireNonNull(storyServiceManager, "storyServiceManager == null");
        Contracts.requireNonNull(realmManager, "realmManager == null");
        Contracts.requireNonNull(rxManager, "rxManager == null");

        return new StoryServiceManager(
            storyServiceManager.getStoryFileManager(),
            storyServiceManager.getStoryNavigationManager(),
            realmManager,
            rxManager,
            storyServiceManager.getMessageManager(),
            storyServiceManager.getStorySearchManager(),
            storyServiceManager.getTaskManager());
    }
}
