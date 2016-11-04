package com.christina.api.story.dao.storyFrame;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.christina.api.story.database.StoryTable;
import com.christina.common.contract.Contracts;

final class StoryFrameFullProjection {
    public static final String[] PROJECTION;

    public static final int INDEX_ID;

    public static final int INDEX_STORY_ID;

    public static final int INDEX_TEXT_POSITION;

    public static final int INDEX_IMAGE;

    public static final int COLUMN_COUNT;

    static {
        int indexer = 0;

        INDEX_ID = indexer++;
        INDEX_STORY_ID = indexer++;
        INDEX_TEXT_POSITION = indexer++;
        INDEX_IMAGE = indexer++;

        COLUMN_COUNT = indexer;

        PROJECTION = new String[COLUMN_COUNT];

        PROJECTION[INDEX_ID] = StoryTable.COLUMN_ID;
        PROJECTION[INDEX_STORY_ID] = StoryTable.StoryFrame.COLUMN_STORY_ID;
        PROJECTION[INDEX_TEXT_POSITION] = StoryTable.StoryFrame.COLUMN_TEXT_POSITION;
        PROJECTION[INDEX_IMAGE] = StoryTable.StoryFrame.COLUMN_IMAGE;
    }

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

    private StoryFrameFullProjection() {
        Contracts.unreachable();
    }
}
