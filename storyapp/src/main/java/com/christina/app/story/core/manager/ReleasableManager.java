package com.christina.app.story.core.manager;

import android.support.annotation.NonNull;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;

import com.christina.app.story.core.manager.resource.ResourceManager;
import com.christina.common.contract.Contracts;
import com.christina.common.event.notice.NoticeEventHandler;

@Accessors(prefix = "_")
public abstract class ReleasableManager {
    protected ReleasableManager(@NonNull final ResourceManager resourceManager) {
        Contracts.requireNonNull(resourceManager, "resourceManager == null");

        _resourceManager = resourceManager;

        resourceManager.getAcquireResourcesEvent().addHandler(_acquireResourcesHandler);
        resourceManager.getReleaseResourcesEvent().addHandler(_releaseResourcesHandler);
    }

    protected abstract void onAcquireResources();

    protected abstract void onReleaseResources();

    @NonNull
    private final NoticeEventHandler _acquireResourcesHandler = new NoticeEventHandler() {
        @Override
        public void onEvent() {
            onAcquireResources();
        }
    };

    @NonNull
    private final NoticeEventHandler _releaseResourcesHandler = new NoticeEventHandler() {
        @Override
        public void onEvent() {
            onReleaseResources();

            final val resourceManager = getResourceManager();

            resourceManager.getAcquireResourcesEvent().removeHandler(_acquireResourcesHandler);
            resourceManager.getReleaseResourcesEvent().removeHandler(_releaseResourcesHandler);
        }
    };

    @Getter(AccessLevel.PROTECTED)
    @NonNull
    private final ResourceManager _resourceManager;
}
