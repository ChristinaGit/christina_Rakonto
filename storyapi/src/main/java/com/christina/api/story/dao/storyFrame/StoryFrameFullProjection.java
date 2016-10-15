package com.christina.api.story.dao.storyFrame;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.christina.common.contract.Contracts;
import com.christina.api.story.database.StoryTable;

final class StoryFrameFullProjection {
    public static final String[] PROJECTION;

    private static int _indexer = 0;

    public static final int INDEX_ID = _indexer++;

    public static final int INDEX_STORY_ID = _indexer++;

    public static final int INDEX_TEXT_POSITION = _indexer++;

    public static final int INDEX_IMAGE = _indexer++;

    public static final int COLUMN_COUNT = _indexer;

    public static long getId(@NonNull final Cursor cursor) {
        Contracts.requireNonNull(cursor, "cursor == null");

        return cursor.getLong(INDEX_ID);
    }

    public static long getStoryId(@NonNull final Cursor cursor) {
        Contracts.requireNonNull(cursor, "cursor == null");

        return cursor.getLong(INDEX_STORY_ID);
    }

    public static int getTextPosition(@NonNull final Cursor cursor) {
        Contracts.requireNonNull(cursor, "cursor == null");

        return cursor.getInt(INDEX_TEXT_POSITION);
    }

    @Nullable
    public static String getImage(@NonNull final Cursor cursor) {
        Contracts.requireNonNull(cursor, "cursor == null");

        return cursor.getString(INDEX_IMAGE);
    }

    static {
        PROJECTION = new String[COLUMN_COUNT];
        PROJECTION[INDEX_ID] = StoryTable.COLUMN_ID;
        PROJECTION[INDEX_STORY_ID] = StoryTable.StoryFrame.COLUMN_STORY_ID;
        PROJECTION[INDEX_TEXT_POSITION] = StoryTable.StoryFrame.COLUMN_TEXT_POSITION;
        PROJECTION[INDEX_IMAGE] = StoryTable.StoryFrame.COLUMN_IMAGE;
    }
}
