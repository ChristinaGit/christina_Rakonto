package com.christina.app.story.core.manager;

import android.support.annotation.NonNull;

import lombok.Getter;
import lombok.experimental.Accessors;

import com.christina.app.story.core.manager.file.StoryFileManager;
import com.christina.app.story.core.manager.navigation.StoryNavigationManager;
import com.christina.app.story.core.manager.search.StorySearchManager;
import com.christina.common.contract.Contracts;
import com.christina.common.control.manager.message.MessageManager;
import com.christina.common.control.manager.realm.RealmManager;
import com.christina.common.control.manager.rx.RxManager;
import com.christina.common.control.manager.task.TaskManager;

@Accessors(prefix = "_")
public final class StoryServiceManager {
    public StoryServiceManager(
        @NonNull final StoryFileManager storyFileManager,
        @NonNull final StoryNavigationManager storyNavigationManager,
        @NonNull final RealmManager realmManager,
        @NonNull final RxManager rxManager,
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

        _storyFileManager = storyFileManager;
        _storyNavigationManager = storyNavigationManager;
        _realmManager = realmManager;
        _rxManager = rxManager;
        _messageManager = messageManager;
        _storySearchManager = storySearchManager;
        _taskManager = taskManager;
    }

    @Getter
    @NonNull
    private final MessageManager _messageManager;

    @Getter
    @NonNull
    private final RealmManager _realmManager;

    @Getter
    @NonNull
    private final RxManager _rxManager;

    @Getter
    @NonNull
    private final StoryFileManager _storyFileManager;

    @Getter
    @NonNull
    private final StoryNavigationManager _storyNavigationManager;

    @Getter
    @NonNull
    private final StorySearchManager _storySearchManager;

    @Getter
    @NonNull
    private final TaskManager _taskManager;
}
