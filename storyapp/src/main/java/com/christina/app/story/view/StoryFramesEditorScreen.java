package com.christina.app.story.view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.christina.app.story.core.StoryEventArgs;
import com.christina.app.story.data.model.Story;
import com.christina.app.story.data.model.StoryFrame;
import com.christina.common.event.generic.Event;
import com.christina.common.presentation.Screen;

import java.util.List;

public interface StoryFramesEditorScreen extends Screen {
    void displayStory(@Nullable Story story);

    void displayStoryFrames(@Nullable List<StoryFrame> storyFrames);

    void displayStoryFramesLoading();

    void displayStoryLoading();

    @NonNull
    Event<StoryEventArgs> getStartEditStoryEvent();
}
