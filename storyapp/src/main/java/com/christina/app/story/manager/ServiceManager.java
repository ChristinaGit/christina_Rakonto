package com.christina.app.story.manager;

import android.support.annotation.NonNull;

import com.christina.app.story.manager.content.StoryContentObserverManager;
import com.christina.app.story.manager.content.StoryDaoManager;
import com.christina.app.story.manager.asyncTask.StoryTaskManager;
import com.christina.app.story.manager.message.MessageManager;
import com.christina.app.story.manager.navigation.StoryNavigator;
import com.christina.common.contract.Contracts;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
public final class ServiceManager {
    public ServiceManager(
        @NonNull final StoryNavigator storyNavigator,
        @NonNull final StoryTaskManager storyTaskManager,
        @NonNull final MessageManager messageManager,
        @NonNull final StoryDaoManager storyDaoManager,
        @NonNull final StoryContentObserverManager storyContentObserverManager) {
        Contracts.requireNonNull(storyNavigator, "storyNavigator == null");
        Contracts.requireNonNull(storyTaskManager, "storyLoaderManager == null");
        Contracts.requireNonNull(messageManager, "messageManager == null");
        Contracts.requireNonNull(storyDaoManager, "storyDaoManager == null");
        Contracts.requireNonNull(storyContentObserverManager,
                                 "storyContentObserverManager == null");

        _storyNavigator = storyNavigator;
        _storyTaskManager = storyTaskManager;
        _messageManager = messageManager;
        _storyDaoManager = storyDaoManager;
        _storyContentObserverManager = storyContentObserverManager;
    }

    @Getter
    @NonNull
    private final MessageManager _messageManager;

    @Getter
    @NonNull
    private final StoryContentObserverManager _storyContentObserverManager;

    @Getter
    @NonNull
    private final StoryDaoManager _storyDaoManager;

    @Getter
    @NonNull
    private final StoryTaskManager _storyTaskManager;

    @Getter
    @NonNull
    private final StoryNavigator _storyNavigator;
}
