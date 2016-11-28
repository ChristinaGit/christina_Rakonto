package com.christina.app.story.fragment;

import android.support.annotation.NonNull;

import com.christina.common.event.NoticeEvent;

public interface StoryEditorFragment {
    boolean hasContent();

    @NonNull
    NoticeEvent getOnContentChangedEvent();

    void onStartEditing();

    void onStopEditing();
}
