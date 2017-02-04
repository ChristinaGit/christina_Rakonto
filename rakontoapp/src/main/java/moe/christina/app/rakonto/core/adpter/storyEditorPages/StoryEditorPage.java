package moe.christina.app.rakonto.core.adpter.storyEditorPages;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import moe.christina.common.event.notice.NoticeEvent;

public interface StoryEditorPage {
    @NonNull
    NoticeEvent getContentChangedEvent();

    @Nullable
    Long getStoryId();

    void setStoryId(@Nullable Long storyId);

    boolean hasContent();

    void notifyStartEditing(@Nullable ReadyCallback callback);

    void notifyStopEditing(@Nullable ReadyCallback callback);

    interface ReadyCallback {
        void onPageReady();
    }
}
