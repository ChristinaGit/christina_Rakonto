package moe.christina.app.rakonto.screen;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import moe.christina.app.rakonto.core.eventArgs.StoryEventArgs;
import moe.christina.app.rakonto.core.manager.search.StoryFrameImage;
import moe.christina.app.rakonto.model.ui.UIStory;
import moe.christina.common.event.generic.Event;
import moe.christina.common.mvp.screen.Screen;

import java.util.List;

public interface StoryFramesEditorScreen extends Screen {
    void displayStoreFrameImageCandidates(
        long storyFrameId, @Nullable List<StoryFrameImage> candidatesUris);

    void displayStory(@Nullable UIStory story);

    @NonNull
    Event<StoryEventArgs> getStartEditStoryEvent();
}
