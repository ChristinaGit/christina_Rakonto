package com.christina.app.story.di.storyScreen.module;

import android.support.annotation.NonNull;

import io.realm.RealmConfiguration;

import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.android.ActivityEvent;

import com.christina.app.story.R;
import com.christina.app.story.core.manager.StoryServiceManager;
import com.christina.app.story.core.manager.file.StoryFileManager;
import com.christina.app.story.core.manager.navigation.ActivityStoryNavigationManager;
import com.christina.app.story.core.manager.navigation.StoryNavigationManager;
import com.christina.app.story.core.manager.search.StorySearchManager;
import com.christina.app.story.di.qualifier.ScopeNames;
import com.christina.app.story.di.storyScreen.StoryScreenScope;
import com.christina.common.contract.Contracts;
import com.christina.common.control.adviser.ResourceAdviser;
import com.christina.common.control.manager.message.ActivityMessageManager;
import com.christina.common.control.manager.message.MessageManager;
import com.christina.common.control.manager.realm.AndroidRealmManger;
import com.christina.common.control.manager.realm.RealmManager;
import com.christina.common.control.manager.rx.AndroidRxManager;
import com.christina.common.control.manager.rx.RxManager;
import com.christina.common.control.manager.task.TaskManager;
import com.christina.common.data.realm.RealmIdGenerator;
import com.christina.common.extension.activity.ObservableActivity;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
@StoryScreenScope
public final class StoryScreenManagerModule {
    public StoryScreenManagerModule(
        @NonNull final ObservableActivity observableActivity) {
        Contracts.requireNonNull(observableActivity, "observableActivity == null");

        _observableActivity = observableActivity;
    }

    @Provides
    @StoryScreenScope
    @NonNull
    public final MessageManager provideMessageManager(
        @NonNull final ObservableActivity observableActivity) {
        Contracts.requireNonNull(observableActivity, "observableActivity == null");

        return new ActivityMessageManager(observableActivity, R.id.coordinator);
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
        @NonNull @Named(ScopeNames.SCREEN) final ResourceAdviser resourceAdviser,
        @NonNull final RealmConfiguration realmConfiguration,
        @NonNull final RealmIdGenerator realmIdGenerator) {
        Contracts.requireNonNull(resourceAdviser, "resourceAdviser == null");
        Contracts.requireNonNull(realmConfiguration, "realmConfiguration == null");
        Contracts.requireNonNull(realmIdGenerator, "realmIdGenerator == null");

        return new AndroidRealmManger(resourceAdviser, realmConfiguration, realmIdGenerator);
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

    @Provides
    @StoryScreenScope
    @NonNull
    public final StoryNavigationManager provideStoryNavigationManager(
        @NonNull @Named(ScopeNames.SCREEN) final ResourceAdviser resourceAdviser,
        @NonNull final ObservableActivity observableActivity) {
        Contracts.requireNonNull(resourceAdviser, "resourceAdviser == null");
        Contracts.requireNonNull(observableActivity, "observableActivity == null");

        return new ActivityStoryNavigationManager(resourceAdviser, observableActivity);
    }

    @Named(ScopeNames.SCREEN)
    @Provides
    @StoryScreenScope
    @NonNull
    public final StoryServiceManager provideStoryServiceManager(
        @NonNull final StoryFileManager storyFileManager,
        @NonNull final StoryNavigationManager storyNavigationManager,
        @NonNull @Named(ScopeNames.SCREEN) final RealmManager realmManager,
        @NonNull @Named(ScopeNames.SCREEN) final RxManager rxManager,
        @NonNull final MessageManager messageManager,
        @NonNull final StorySearchManager storySearchManager,
        @NonNull final TaskManager taskManager) {
        Contracts.requireNonNull(storyFileManager, "storyFileManager == null");
        Contracts.requireNonNull(storyNavigationManager, "storyNavigationManager == null");
        Contracts.requireNonNull(realmManager, "realmManager == null");
        Contracts.requireNonNull(rxManager, "rxManager == null");
        Contracts.requireNonNull(messageManager, "messageManager == null");
        Contracts.requireNonNull(storySearchManager, "storySearchManager == null");
        Contracts.requireNonNull(taskManager, "taskManager == null");

        return new StoryServiceManager(
            storyFileManager,
            storyNavigationManager,
            realmManager,
            rxManager,
            messageManager,
            storySearchManager,
            taskManager);
    }

    @NonNull
    private final ObservableActivity _observableActivity;
}
