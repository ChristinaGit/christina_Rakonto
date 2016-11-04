package com.christina.app.story.fragment;

import android.support.annotation.NonNull;

import com.christina.common.event.NoticeEvent;

public interface StoryEditorFragment {
    boolean hasContent();

    @NonNull
    NoticeEvent onContentChanged();

    void onStartEditing();

    void onStopEditing();
}
