package com.christina.app.story.presenter;

import android.support.annotation.NonNull;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;

import com.christina.app.story.core.manager.StoryServiceManager;
import com.christina.app.story.core.manager.file.StoryFileManager;
import com.christina.app.story.core.manager.navigation.StoryNavigationManager;
import com.christina.app.story.core.manager.search.StorySearchManager;
import com.christina.common.contract.Contracts;
import com.christina.common.control.manager.message.MessageManager;
import com.christina.common.control.manager.realm.RealmManager;
import com.christina.common.control.manager.rx.RxManager;
import com.christina.common.control.manager.task.TaskManager;
import com.christina.common.mvp.presenter.BasePresenter;
import com.christina.common.mvp.screen.Screen;

@Accessors(prefix = "_")
public abstract class BaseStoryPresenter<TScreen extends Screen> extends BasePresenter<TScreen> {
    public BaseStoryPresenter(
        @NonNull final StoryServiceManager storyServiceManager) {
        Contracts.requireNonNull(storyServiceManager, "storyServiceManager == null");

        _storyServiceManager = storyServiceManager;
    }

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
