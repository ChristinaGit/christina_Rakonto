package com.christina.app.story.presentation;

import android.support.annotation.NonNull;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;

import com.christina.app.story.core.manager.StoryServiceManager;
import com.christina.app.story.core.manager.file.StoryFileManager;
import com.christina.app.story.core.manager.message.MessageManager;
import com.christina.app.story.core.manager.navigation.StoryNavigationManager;
import com.christina.app.story.core.manager.realm.RealmManager;
import com.christina.app.story.core.manager.rx.RxManager;
import com.christina.common.contract.Contracts;
import com.christina.common.presentation.AbstractPresenter;
import com.christina.common.presentation.Screen;

@Accessors(prefix = "_")
public abstract class BaseStoryPresenter<TScreen extends Screen>
    extends AbstractPresenter<TScreen> {
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
