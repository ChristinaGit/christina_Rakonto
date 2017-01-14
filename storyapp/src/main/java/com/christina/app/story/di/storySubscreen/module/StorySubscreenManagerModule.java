package com.christina.app.story.di.storySubscreen.module;

import android.support.annotation.NonNull;

import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.android.FragmentEvent;

import com.christina.app.story.core.RealmIdGenerator;
import com.christina.app.story.core.manager.ServiceManager;
import com.christina.app.story.core.manager.data.AndroidRealmManger;
import com.christina.app.story.core.manager.data.RealmManager;
import com.christina.app.story.core.manager.resource.ResourceManager;
import com.christina.app.story.core.manager.rx.AndroidRxManager;
import com.christina.app.story.core.manager.rx.RxManager;
import com.christina.app.story.di.qualifier.ScopeNames;
import com.christina.app.story.di.storySubscreen.StorySubscreenScope;
import com.christina.common.contract.Contracts;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
@StorySubscreenScope
public final class StorySubscreenManagerModule {
    public StorySubscreenManagerModule(@NonNull final ResourceManager resourceManager) {
        Contracts.requireNonNull(resourceManager, "resourceManager == null");

        _resourceManager = resourceManager;
    }

    @Named(ScopeNames.SUBSCREEN)
    @Provides
    @StorySubscreenScope
    @NonNull
    public final RealmManager provideRealmManager(
        @NonNull @Named(ScopeNames.SUBSCREEN) final ResourceManager resourceManager,
        @NonNull final RealmIdGenerator realmIdGenerator) {
        Contracts.requireNonNull(resourceManager, "resourceManager == null");
        Contracts.requireNonNull(realmIdGenerator, "realmIdGenerator == null");

        return new AndroidRealmManger(resourceManager, realmIdGenerator);
    }

    @Named(ScopeNames.SUBSCREEN)
    @Provides
    @StorySubscreenScope
    @NonNull
    public final ResourceManager provideResourceManager() {
        return _resourceManager;
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
    public final ServiceManager provideServiceManager(
        @NonNull @Named(ScopeNames.SCREEN) final ServiceManager serviceManager,
        @NonNull @Named(ScopeNames.SUBSCREEN) final ResourceManager resourceManager,
        @NonNull @Named(ScopeNames.SUBSCREEN) final RealmManager realmManager,
        @NonNull @Named(ScopeNames.SUBSCREEN) final RxManager rxManager) {
        Contracts.requireNonNull(serviceManager, "serviceManager == null");
        Contracts.requireNonNull(resourceManager, "resourceManager == null");
        Contracts.requireNonNull(realmManager, "realmManager == null");
        Contracts.requireNonNull(rxManager, "rxManager == null");

        return new ServiceManager(
            resourceManager,
            serviceManager.getStoryNavigator(),
            realmManager,
            rxManager,
            serviceManager.getMessageManager());
    }

    @NonNull
    private final ResourceManager _resourceManager;
}
