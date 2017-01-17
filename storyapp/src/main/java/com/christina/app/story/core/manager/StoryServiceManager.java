package com.christina.app.story.core.manager;

import android.support.annotation.NonNull;

import lombok.Getter;
import lombok.experimental.Accessors;

import com.christina.app.story.core.manager.file.StoryFileManager;
import com.christina.app.story.core.manager.message.MessageManager;
import com.christina.app.story.core.manager.navigation.StoryNavigationManager;
import com.christina.app.story.core.manager.realm.RealmManager;
import com.christina.app.story.core.manager.rx.RxManager;
import com.christina.common.contract.Contracts;

@Accessors(prefix = "_")
public final class StoryServiceManager {
    public StoryServiceManager(
        @NonNull final StoryFileManager storyFileManager,
        @NonNull final StoryNavigationManager storyNavigationManager,
        @NonNull final RealmManager realmManager,
        @NonNull final RxManager rxManager,
        @NonNull final MessageManager messageManager) {
        Contracts.requireNonNull(storyFileManager, "storyFileManager == null");
        Contracts.requireNonNull(storyNavigationManager, "storyNavigationManager == null");
        Contracts.requireNonNull(realmManager, "realmManager == null");
        Contracts.requireNonNull(rxManager, "rxManager == null");
        Contracts.requireNonNull(messageManager, "messageManager == null");

        _storyFileManager = storyFileManager;
        _storyNavigationManager = storyNavigationManager;
        _realmManager = realmManager;
        _rxManager = rxManager;
        _messageManager = messageManager;
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
}
