package moe.christina.app.rakonto.screen;

import android.support.annotation.NonNull;

import moe.christina.common.event.notice.NoticeEvent;
import moe.christina.common.mvp.screen.Screen;

public interface StoriesViewerScreen extends Screen {
    @NonNull
    NoticeEvent getRemoveAllEvent();

    @NonNull
    NoticeEvent getRequestInsertStoryEvent();
}
