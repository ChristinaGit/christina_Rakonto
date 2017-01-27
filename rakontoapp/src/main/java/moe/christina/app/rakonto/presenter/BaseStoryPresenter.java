package moe.christina.app.rakonto.presenter;

import android.support.annotation.NonNull;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;

import moe.christina.app.rakonto.core.manager.StoryServiceManager;
import moe.christina.app.rakonto.core.manager.file.StoryFileManager;
import moe.christina.app.rakonto.core.manager.navigation.StoryNavigationManager;
import moe.christina.app.rakonto.core.manager.search.StorySearchManager;
import moe.christina.common.contract.Contracts;
import moe.christina.common.control.manager.message.MessageManager;
import moe.christina.common.control.manager.realm.RealmManager;
import moe.christina.common.control.manager.rx.RxManager;
import moe.christina.common.control.manager.task.TaskManager;
import moe.christina.common.mvp.presenter.BasePresenter;
import moe.christina.common.mvp.screen.Screen;

@Accessors(prefix = "_")
public abstract class BaseStoryPresenter<TScreen extends Screen> extends BasePresenter<TScreen> {
    @NonNull
    public final RealmManager getRealmManager() {
        return getStoryServiceManager().getRealmManager();
    }

    @NonNull
    public final RxManager getRxManager() {
        return getStoryServiceManager().getRxManager();
    }

    @NonNull
    public final StoryFileManager getStoryFileManager() {
        return getStoryServiceManager().getStoryFileManager();
    }

    @NonNull
    public final StorySearchManager getStorySearchManager() {
        return getStoryServiceManager().getStorySearchManager();
    }

    @NonNull
    public final TaskManager getTaskManager() {
        return getStoryServiceManager().getTaskManager();
    }

    protected BaseStoryPresenter(
        @NonNull final StoryServiceManager storyServiceManager) {
        Contracts.requireNonNull(storyServiceManager, "storyServiceManager == null");

        _storyServiceManager = storyServiceManager;
    }

    @NonNull
    protected final MessageManager getMessageManager() {
        return getStoryServiceManager().getMessageManager();
    }

    @NonNull
    protected final StoryNavigationManager getStoryNavigationManager() {
        return getStoryServiceManager().getStoryNavigationManager();
    }

    @Getter(AccessLevel.PROTECTED)
    @NonNull
    private final StoryServiceManager _storyServiceManager;
}
