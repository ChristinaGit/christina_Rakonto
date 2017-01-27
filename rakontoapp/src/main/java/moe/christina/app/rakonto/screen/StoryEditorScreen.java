package moe.christina.app.rakonto.screen;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import moe.christina.app.rakonto.core.eventArgs.StoryEventArgs;
import moe.christina.app.rakonto.model.ui.UIStory;
import moe.christina.common.event.generic.Event;
import moe.christina.common.event.notice.NoticeEvent;
import moe.christina.common.mvp.screen.Screen;

public interface StoryEditorScreen extends Screen {
    void displayStory(@Nullable final UIStory story);

    void displayStoryLoading();

    @NonNull
    Event<StoryEventArgs> getEditStoryEvent();

    @NonNull
    NoticeEvent getInsertStoryEvent();
}
