package com.christina.app.story.view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.christina.api.story.model.Story;
import com.christina.app.story.core.StoryEventArgs;
import com.christina.common.data.cursor.dataCursor.DataCursor;
import com.christina.common.event.Event;
import com.christina.common.view.presentation.PresentableView;

public interface StoriesListPresentableView extends PresentableView {
    @NonNull
    Event<StoryEventArgs> getOnDeleteStoryEvent();

    @NonNull
    Event<StoryEventArgs> getOnEditStoryEvent();

    boolean isLoadingVisible();

    void setLoadingVisible(boolean visible);

    boolean isStoriesVisible();

    void setStoriesVisible(boolean visible);

    void displayStories(@Nullable DataCursor<Story> stories);
}
