package com.christina.app.story.core.adpter.storyEditorPages;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.christina.common.event.notice.NoticeEvent;

public interface StoryEditorPage {
    @NonNull
    NoticeEvent getContentChangedEvent();

    @Nullable
    Long getStoryId();

    void setStoryId(@Nullable Long storyId);

    boolean hasContent();

    void notifyStartEditing();

    void notifyStopEditing();
}
