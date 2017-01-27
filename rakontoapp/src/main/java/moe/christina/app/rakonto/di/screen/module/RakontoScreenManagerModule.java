package moe.christina.app.rakonto.di.screen.module;

import android.support.annotation.NonNull;

import io.realm.RealmConfiguration;

import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.android.ActivityEvent;

import moe.christina.app.rakonto.R;
import moe.christina.app.rakonto.core.manager.StoryServiceManager;
import moe.christina.app.rakonto.core.manager.file.StoryFileManager;
import moe.christina.app.rakonto.core.manager.navigation.ActivityStoryNavigationManager;
import moe.christina.app.rakonto.core.manager.navigation.StoryNavigationManager;
import moe.christina.app.rakonto.core.manager.search.StorySearchManager;
import moe.christina.app.rakonto.di.qualifier.ScopeNames;
import moe.christina.app.rakonto.di.screen.RakontoScreenScope;
import moe.christina.common.contract.Contracts;
import moe.christina.common.control.adviser.ResourceAdviser;
import moe.christina.common.control.manager.message.ActivityMessageManager;
import moe.christina.common.control.manager.message.MessageManager;
import moe.christina.common.control.manager.realm.AndroidRealmManger;
import moe.christina.common.control.manager.realm.RealmManager;
import moe.christina.common.control.manager.rx.AndroidRxManager;
import moe.christina.common.control.manager.rx.RxManager;
import moe.christina.common.control.manager.task.TaskManager;
import moe.christina.common.data.realm.RealmIdGenerator;
import moe.christina.common.extension.activity.ObservableActivity;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
@RakontoScreenScope
public final class RakontoScreenManagerModule {
    public RakontoScreenManagerModule(
        @NonNull final ObservableActivity observableActivity) {
        Contracts.requireNonNull(observableActivity, "observableActivity == null");

        _observableActivity = observableActivity;
    }

    @Provides
    @RakontoScreenScope
    @NonNull
    public final MessageManager provideMessageManager(
        @NonNull final ObservableActivity observableActivity) {
        Contracts.requireNonNull(observableActivity, "observableActivity == null");

        return new ActivityMessageManager(observableActivity, R.id.coordinator);
    }

    @Provides
    @RakontoScreenScope
    @NonNull
    public final ObservableActivity provideObservableActivity() {
        return _observableActivity;
    }

    @Named(ScopeNames.SCREEN)
    @Provides
    @RakontoScreenScope
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
    @RakontoScreenScope
    @NonNull
    public final RxManager provideRxManager(
        @NonNull @Named(ScopeNames.SCREEN)
        final LifecycleProvider<ActivityEvent> lifecycleProvider) {
        Contracts.requireNonNull(lifecycleProvider, "lifecycleProvider == null");

        return new AndroidRxManager<>(lifecycleProvider);
    }

    @Provides
    @RakontoScreenScope
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
    @RakontoScreenScope
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
