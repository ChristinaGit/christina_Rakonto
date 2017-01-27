package moe.christina.app.rakonto.core.eventArgs;

import android.support.annotation.NonNull;

import lombok.Getter;
import lombok.experimental.Accessors;

import moe.christina.app.rakonto.model.ui.UIStory;
import moe.christina.common.event.eventArgs.EventArgs;

@Accessors(prefix = "_")
public class StoryContentEventArgs extends EventArgs {
    public StoryContentEventArgs(@NonNull final UIStory story) {
        _story = story;
    }

    @Getter
    @NonNull
    private final UIStory _story;
}
