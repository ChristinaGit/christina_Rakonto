package moe.christina.app.rakonto.core.adpter.storyEditorPages;

import lombok.Getter;
import lombok.experimental.Accessors;

import moe.christina.common.event.eventArgs.EventArgs;

@Accessors(prefix = "_")
public final class StoryEditorPageChangedEventArgs extends EventArgs {
    public StoryEditorPageChangedEventArgs(final int page) {
        _page = page;
    }

    @Getter
    private final int _page;
}
