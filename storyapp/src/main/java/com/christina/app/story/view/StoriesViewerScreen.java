package com.christina.app.story.view;

import android.support.annotation.NonNull;

import com.christina.common.event.notice.NoticeEvent;
import com.christina.common.mvp.screen.Screen;

public interface StoriesViewerScreen extends Screen {
    @NonNull
    NoticeEvent getRemoveAllEvent();

    @NonNull
    NoticeEvent getRequestInsertStoryEvent();
}
