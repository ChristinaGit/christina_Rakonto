package com.christina.api.story.database;

import com.christina.common.contract.Contracts;

public final class StoryFrameTable {
    public static final String NAME = "StoryFrames";

    public static final String COLUMN_ID = "id";

    public static final String COLUMN_STORY_ID = "storyId";

    public static final String COLUMN_TEXT_START_POSITION = "textStartPosition";

    public static final String COLUMN_TEXT_END_POSITION = "textEndPosition";

    public static final String COLUMN_IMAGE = "image";

    private StoryFrameTable() {
        Contracts.unreachable();
    }
}
