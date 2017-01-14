package com.christina.app.story.core.manager;

import android.support.annotation.NonNull;

import lombok.Getter;
import lombok.experimental.Accessors;

import com.christina.app.story.core.manager.data.RealmManager;
import com.christina.app.story.core.manager.message.MessageManager;
import com.christina.app.story.core.manager.navigation.StoryNavigator;
import com.christina.app.story.core.manager.resource.ResourceManager;
import com.christina.app.story.core.manager.rx.RxManager;
import com.christina.common.contract.Contracts;

@Accessors(prefix = "_")
public final class ServiceManager {
    public ServiceManager(
        @NonNull final ResourceManager resourceManager,
        @NonNull final StoryNavigator storyNavigator,
        @NonNull final RealmManager realmManager,
        @NonNull final RxManager rxManager,
        @NonNull final MessageManager messageManager) {
        Contracts.requireNonNull(resourceManager, "resourceManager == null");
        Contracts.requireNonNull(storyNavigator, "storyNavigator == null");
        Contracts.requireNonNull(realmManager, "realmManager == null");
        Contracts.requireNonNull(rxManager, "rxManager == null");
        Contracts.requireNonNull(messageManager, "messageManager == null");

        _resourceManager = resourceManager;
        _storyNavigator = storyNavigator;
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
    private final ResourceManager _resourceManager;

    @Getter
    @NonNull
    private final RxManager _rxManager;

    @Getter
    @NonNull
    private final StoryNavigator _storyNavigator;
}
