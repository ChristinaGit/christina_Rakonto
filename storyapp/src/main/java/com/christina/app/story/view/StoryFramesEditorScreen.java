package com.christina.app.story.view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.christina.app.story.core.StoryEventArgs;
import com.christina.app.story.model.ui.UIStory;
import com.christina.common.event.generic.Event;
import com.christina.common.mvp.screen.Screen;

import java.util.List;

public interface StoryFramesEditorScreen extends Screen {
    void displayStoreFrameCandidates(long storyFrameId, @Nullable List<String> candidatesUris);

    void displayStory(@Nullable UIStory story);

    void displayStoryLoading();

    @NonNull
    Event<StoryEventArgs> getStartEditStoryEvent();
}
