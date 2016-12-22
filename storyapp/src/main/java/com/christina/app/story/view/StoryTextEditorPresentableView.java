package com.christina.app.story.view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.christina.api.story.model.Story;
import com.christina.app.story.core.StoryEventArgs;
import com.christina.common.event.Event;
import com.christina.common.event.NoticeEvent;
import com.christina.common.view.presentation.PresentableView;

public interface StoryTextEditorPresentableView extends PresentableView {
    void displayStory(@Nullable Story story);

    @Nullable
    Story getDisplayedStory();

    @NonNull
    Event<StoryEventArgs> getOnStartEditStoryEvent();

    @NonNull
    NoticeEvent getOnStoryChangedEvent();

    boolean isLoadingVisible();

    void setLoadingVisible(boolean visible);

    boolean isStoryVisible();

    void setStoryVisible(boolean visible);
}
