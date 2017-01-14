package com.christina.app.story.core.manager.resource;

import android.support.annotation.NonNull;

import com.christina.common.event.notice.NoticeEvent;

public interface ResourceManager {
    @NonNull
    NoticeEvent getAcquireResourcesEvent();

    @NonNull
    NoticeEvent getReleaseResourcesEvent();
}
