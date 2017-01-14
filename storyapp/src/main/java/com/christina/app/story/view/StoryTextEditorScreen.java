package com.christina.app.story.view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.christina.app.story.core.StoryContentEventArgs;
import com.christina.app.story.core.StoryEventArgs;
import com.christina.app.story.data.model.Story;
import com.christina.common.event.generic.Event;
import com.christina.common.presentation.Screen;

public interface StoryTextEditorScreen extends Screen {
    void displayStory(@Nullable Story story);

    void displayStoryLoading();

    @NonNull
    Event<StoryEventArgs> getStartEditStoryEvent();

    @NonNull
    Event<StoryContentEventArgs> getStoryChangedEvent();
}
