package com.christina.api.story.database;

import com.christina.common.contract.Contracts;

public final class StoryTable {
    public static final String COLUMN_ID = "id";

    private StoryTable() {
        Contracts.unreachable();
    }

    public static final class Story {
        public static final String NAME = "Stories";

        public static final String COLUMN_NAME = "name";

        public static final String COLUMN_CREATE_DATE = "createDate";

        public static final String COLUMN_MODIFY_DATE = "modifyDate";

        public static final String COLUMN_TEXT = "text";

        public static final String COLUMN_PREVIEW = "preview";

        private Story() {
            Contracts.unreachable();
        }
    }

    public static final class StoryFrame {
        public static final String NAME = "StoryFrames";

        public static final String COLUMN_STORY_ID = "storyId";

        public static final String COLUMN_TEXT_POSITION = "textPosition";

        public static final String COLUMN_IMAGE = "image";

        private StoryFrame() {
            Contracts.unreachable();
        }
    }
}
