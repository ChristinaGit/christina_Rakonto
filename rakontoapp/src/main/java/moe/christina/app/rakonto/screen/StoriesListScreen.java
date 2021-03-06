package moe.christina.app.rakonto.screen;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import moe.christina.app.rakonto.core.eventArgs.StoryEventArgs;
import moe.christina.app.rakonto.model.ui.UIStory;
import moe.christina.common.event.generic.Event;
import moe.christina.common.event.notice.NoticeEvent;
import moe.christina.common.mvp.screen.Screen;

import java.util.List;

public interface StoriesListScreen extends Screen {
    void displayStories(@Nullable List<UIStory> stories);

    void displayStoriesLoading();

    @NonNull
    Event<StoryEventArgs> getDeleteStoryEvent();

    @NonNull
    Event<StoryEventArgs> getEditStoryEvent();

    @NonNull
    NoticeEvent getViewStoriesEvent();
}
