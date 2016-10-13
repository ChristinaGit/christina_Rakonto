package com.christina.api.story.dao.story;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.christina.common.contract.Contracts;
import com.christina.api.story.database.StoryTable;

public final class StoryFullProjection {
    public static final String[] PROJECTION;

    private static int _indexer = 0;

    public static final int INDEX_ID = _indexer++;

    public static final int INDEX_NAME = _indexer++;

    public static final int INDEX_CREATE_DATE = _indexer++;

    public static final int INDEX_MODIFY_DATE = _indexer++;

    public static final int INDEX_TEXT = _indexer++;

    public static final int INDEX_PREVIEW = _indexer++;

    public static final int COLUMN_COUNT = _indexer;

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

    static {
        PROJECTION = new String[_indexer];
        PROJECTION[INDEX_ID] = StoryTable.COLUMN_ID;
        PROJECTION[INDEX_NAME] = StoryTable.Story.COLUMN_NAME;
        PROJECTION[INDEX_CREATE_DATE] = StoryTable.Story.COLUMN_CREATE_DATE;
        PROJECTION[INDEX_MODIFY_DATE] = StoryTable.Story.COLUMN_MODIFY_DATE;
        PROJECTION[INDEX_TEXT] = StoryTable.Story.COLUMN_TEXT;
        PROJECTION[INDEX_PREVIEW] = StoryTable.Story.COLUMN_PREVIEW;
    }
}
