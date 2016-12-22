package com.christina.app.story.di.qualifier;

import com.christina.common.contract.Contracts;

public final class PresenterNames {
    public static final String STORIES_LIST = "stories_list";

    public static final String STORIES_VIEWER = "stories_viewer";

    public static final String STORY_EDITOR = "story_editor";

    public static final String STORY_FRAMES_EDITOR = "story_frames_editor";

    public static final String STORY_TEXT_EDITOR = "story_text_editor";

    private PresenterNames() {
        Contracts.unreachable();
    }
}
