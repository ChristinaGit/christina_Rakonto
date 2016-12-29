package com.christina.app.story.di.qualifier;

import com.christina.common.contract.Contracts;

public final class PresenterNames {
    private static final String NAME_PREFIX = "presenter:";

    public static final String STORIES_LIST = NAME_PREFIX + "stories_list";

    public static final String STORIES_VIEWER = NAME_PREFIX + "stories_viewer";

    public static final String STORY_EDITOR = NAME_PREFIX + "story_editor";

    public static final String STORY_FRAMES_EDITOR = NAME_PREFIX + "story_frames_editor";

    public static final String STORY_TEXT_EDITOR = NAME_PREFIX + "story_text_editor";

    private PresenterNames() {
        Contracts.unreachable();
    }
}
