package com.christina.app.story.view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.christina.app.story.core.StoryEventArgs;
import com.christina.app.story.data.model.ui.UIStory;
import com.christina.common.event.generic.Event;
import com.christina.common.event.notice.NoticeEvent;
import com.christina.common.presentation.Screen;

public interface StoryEditorScreen extends Screen {
    void displayStory(@Nullable final UIStory story);

    void displayStoryLoading();

    @NonNull
    Event<StoryEventArgs> getEditStoryEvent();

    @NonNull
    NoticeEvent getInsertStoryEvent();
}
