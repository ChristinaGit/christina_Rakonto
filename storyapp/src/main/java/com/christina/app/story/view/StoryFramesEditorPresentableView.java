package com.christina.app.story.view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.christina.api.story.model.Story;
import com.christina.api.story.model.StoryFrame;
import com.christina.app.story.core.StoryEventArgs;
import com.christina.common.data.cursor.dataCursor.DataCursor;
import com.christina.common.event.Event;
import com.christina.common.view.presentation.PresentableView;

public interface StoryFramesEditorPresentableView extends PresentableView {
    void displayStory(@Nullable Story story);

    void displayStoryFrames(@Nullable DataCursor<StoryFrame> storyFrames);

    long getEditedStoryId();

    @NonNull
    Event<StoryEventArgs> getOnStartEditStoryEvent();

    boolean isLoadingVisible();

    void setLoadingVisible(boolean visible);

    boolean isStoryFramesVisible();

    void setStoryFramesVisible(boolean visible);
}
