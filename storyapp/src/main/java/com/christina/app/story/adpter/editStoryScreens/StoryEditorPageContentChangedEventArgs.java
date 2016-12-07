package com.christina.app.story.adpter.editStoryScreens;

import com.christina.common.event.eventArgs.EventArgs;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
public final class StoryEditorPageContentChangedEventArgs extends EventArgs {
    public StoryEditorPageContentChangedEventArgs(final int page) {
        _page = page;
    }

    @Getter
    private final int _page;
}
