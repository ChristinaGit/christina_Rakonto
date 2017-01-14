package com.christina.app.story.view;

import android.support.annotation.NonNull;

import com.christina.common.event.notice.NoticeEvent;
import com.christina.common.presentation.Screen;

public interface StoriesViewerScreen extends Screen {
    @NonNull
    NoticeEvent getRequestInsertStoryEvent();

    @NonNull
    NoticeEvent getRemoveAllEvent();
}