package com.christina.api.story.dao.story;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.christina.api.story.database.StoryTable;
import com.christina.common.contract.Contracts;

public final class StoryFullProjection {
    public static final String[] PROJECTION;

    public static final int INDEX_ID;

    public static final int INDEX_NAME;

    public static final int INDEX_CREATE_DATE;

    public static final int INDEX_MODIFY_DATE;

    public static final int INDEX_TEXT;

    public static final int INDEX_PREVIEW;

    public static final int COLUMN_COUNT;

    static {
        int indexer = 0;

        INDEX_ID = indexer++;
        INDEX_NAME = indexer++;
        INDEX_CREATE_DATE = indexer++;
        INDEX_MODIFY_DATE = indexer++;
        INDEX_TEXT = indexer++;
        INDEX_PREVIEW = indexer++;

        COLUMN_COUNT = indexer;

        PROJECTION = new String[COLUMN_COUNT];

        PROJECTION[INDEX_ID] = StoryTable.COLUMN_ID;
        PROJECTION[INDEX_NAME] = StoryTable.Story.COLUMN_NAME;
        PROJECTION[INDEX_CREATE_DATE] = StoryTable.Story.COLUMN_CREATE_DATE;
        PROJECTION[INDEX_MODIFY_DATE] = StoryTable.Story.COLUMN_MODIFY_DATE;
        PROJECTION[INDEX_TEXT] = StoryTable.Story.COLUMN_TEXT;
        PROJECTION[INDEX_PREVIEW] = StoryTable.Story.COLUMN_PREVIEW;
    }

    public static long getId(@NonNull final Cursor cursor) {
        Contracts.requireNonNull(cursor, "cursor == null");

        return cursor.getLong(INDEX_ID);
    }

    @Nullable
    public static String getName(@NonNull final Cursor cursor) {
        Contracts.requireNonNull(cursor, "cursor == null");

        return cursor.getString(INDEX_NAME);
    }

    public static long getCreateDate(@NonNull final Cursor cursor) {
        Contracts.requireNonNull(cursor, "cursor == null");

        return cursor.getLong(INDEX_CREATE_DATE);
    }

    public static long getModifyDate(@NonNull final Cursor cursor) {
        Contracts.requireNonNull(cursor, "cursor == null");

        return cursor.getLong(INDEX_MODIFY_DATE);
    }

    @Nullable
    public static String getText(@NonNull final Cursor cursor) {
        Contracts.requireNonNull(cursor, "cursor == null");

        return cursor.getString(INDEX_TEXT);
    }

    @Nullable
    public static String getPreview(@NonNull final Cursor cursor) {
        Contracts.requireNonNull(cursor, "cursor == null");

        return cursor.getString(INDEX_PREVIEW);
    }

    private StoryFullProjection() {
        Contracts.unreachable();
    }
}
