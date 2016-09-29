package com.christina.content.story.database;

public final class StoryTable {
    public static final String COLUMN_ID = "id";

    private StoryTable() {
    }

    public static final class Story {
        public static final String NAME = "Stories";

        public static final String COLUMN_NAME = "name";

        public static final String COLUMN_CREATE_DATE = "createDate";

        public static final String COLUMN_MODIFY_DATE = "modifyDate";

        public static final String COLUMN_TEXT = "text";

        public static final String COLUMN_PREVIEW = "preview";

        //@formatter:off
        public static final String CREATE =
            "CREATE TABLE IF NOT EXISTS " + NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_CREATE_DATE + " INTEGER NOT NULL, " +
                COLUMN_MODIFY_DATE + " INTEGER NOT NULL, " +
                COLUMN_TEXT + " TEXT, " +
                COLUMN_PREVIEW + " TEXT)";
        //@formatter:on

        private Story() {
        }
    }

    public static final class StoryFrame {
        public static final String NAME = "StoryFrames";

        public static final String COLUMN_STORY_ID = "storyId";

        public static final String COLUMN_TEXT_POSITION = "textPosition";

        public static final String COLUMN_IMAGE = "image";

        ;

        //@formatter:off
        public static final String CREATE =
            "CREATE TABLE IF NOT EXISTS " + NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_STORY_ID + " INTEGER NOT NULL, " +
                COLUMN_TEXT_POSITION + " INTEGER NOT NULL, " +
                COLUMN_IMAGE + " TEXT)";
        //@formatter:on

        private StoryFrame() {
        }
    }
}
