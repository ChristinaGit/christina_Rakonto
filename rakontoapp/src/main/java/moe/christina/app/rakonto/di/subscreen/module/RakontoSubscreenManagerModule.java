package moe.christina.app.rakonto.di.subscreen.module;

import android.support.annotation.NonNull;

import io.realm.RealmConfiguration;

import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.android.FragmentEvent;

import moe.christina.app.rakonto.core.manager.StoryServiceManager;
import moe.christina.app.rakonto.di.qualifier.ScopeNames;
import moe.christina.app.rakonto.di.subscreen.RakontoSubscreenScope;
import moe.christina.common.contract.Contracts;
import moe.christina.common.control.adviser.ResourceAdviser;
import moe.christina.common.control.manager.realm.AndroidRealmManger;
import moe.christina.common.control.manager.realm.RealmManager;
import moe.christina.common.control.manager.rx.AndroidRxManager;
import moe.christina.common.control.manager.rx.RxManager;
import moe.christina.common.data.realm.RealmIdGenerator;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
@RakontoSubscreenScope
public final class RakontoSubscreenManagerModule {

    @Named(ScopeNames.SUBSCREEN)
    @Provides
    @RakontoSubscreenScope
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
    @RakontoSubscreenScope
    @NonNull
    public final RxManager provideRxManager(
        @NonNull @Named(ScopeNames.SUBSCREEN)
        final LifecycleProvider<FragmentEvent> lifecycleProvider) {
        Contracts.requireNonNull(lifecycleProvider, "lifecycleProvider == null");

        return new AndroidRxManager<>(lifecycleProvider);
    }

    @Named(ScopeNames.SUBSCREEN)
    @Provides
    @RakontoSubscreenScope
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
