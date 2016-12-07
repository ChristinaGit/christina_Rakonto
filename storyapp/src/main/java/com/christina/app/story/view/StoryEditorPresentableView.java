package com.christina.app.story.view;

import android.support.annotation.NonNull;

import com.christina.app.story.core.StoryEventArgs;
import com.christina.common.event.Event;
import com.christina.common.event.NoticeEvent;
import com.christina.common.view.presentation.PresentableView;

public interface StoryEditorPresentableView extends PresentableView {
    void displayStory(long storyId);

    @NonNull
    Event<StoryEventArgs> getOnEditStoryEvent();

    @NonNull
    NoticeEvent getOnInsertStoryEvent();
}
