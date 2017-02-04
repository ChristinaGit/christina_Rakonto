package moe.christina.app.rakonto.screen;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import moe.christina.app.rakonto.core.eventArgs.StoryChangedEventArgs;
import moe.christina.app.rakonto.core.eventArgs.StoryEventArgs;
import moe.christina.app.rakonto.model.ui.UIStory;
import moe.christina.common.event.generic.Event;
import moe.christina.common.mvp.screen.Screen;

public interface StoryTextEditorScreen extends Screen {
    void displaySaveStoryChangedComplete();

    void displayStory(@Nullable UIStory story);

    @NonNull
    Event<StoryEventArgs> getStartEditStoryEvent();

    @NonNull
    Event<StoryChangedEventArgs> getStoryChangedEvent();
}
