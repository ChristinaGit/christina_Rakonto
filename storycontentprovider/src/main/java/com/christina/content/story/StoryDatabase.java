package com.christina.content.story;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.christina.api.story.database.StoryFrameTable;
import com.christina.api.story.database.StoryTable;
import com.christina.common.contract.Contracts;
import com.christina.common.data.database.SQLiteDatabaseWrapper;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
public final class StoryDatabase extends SQLiteDatabaseWrapper {
    public static final String NAME = "Stories.db";

    public static final int VERSION = 1;

    public StoryDatabase(@NonNull final Context context) {
        this(Contracts.requireNonNull(context, "context == null"), null);
    }

    public StoryDatabase(
        @NonNull final Context context, @Nullable final DatabaseErrorHandler errorHandler) {
        super(
            Contracts.requireNonNull(context, "context == null"),
            NAME,
            null,
            VERSION,
            errorHandler);
    }

    @Override
    public final void onConfigure(final SQLiteDatabase db) {
        super.onConfigure(db);

        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public final void onCreate(final SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);

        db.execSQL(getCreateStoryTableQuery());
        db.execSQL(getCreateStoryFrameTableQuery());
    }

    @Override
    public final void onUpgrade(
        final SQLiteDatabase db, final int oldVersion, final int newVersion) {
    }

    @Getter(value = AccessLevel.PRIVATE, lazy = true)
    @NonNull
    private final String _createStoryFrameTableQuery =
        "CREATE TABLE IF NOT EXISTS " + StoryFrameTable.NAME + "(" +
        StoryFrameTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        StoryFrameTable.COLUMN_STORY_ID + " INTEGER NOT NULL, " +
        StoryFrameTable.COLUMN_TEXT_START_POSITION + " INTEGER NOT NULL, " +
        StoryFrameTable.COLUMN_TEXT_END_POSITION + " INTEGER NOT NULL, " +
        StoryFrameTable.COLUMN_IMAGE + " TEXT, " +
        "FOREIGN KEY (" + StoryFrameTable.COLUMN_STORY_ID + ") REFERENCES " +
        StoryTable.NAME + "(" + StoryTable.COLUMN_ID + "))";

    @Getter(value = AccessLevel.PRIVATE, lazy = true)
    @NonNull
    private final String _createStoryTableQuery =
        "CREATE TABLE IF NOT EXISTS " + StoryTable.NAME + "(" +
        StoryTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        StoryTable.COLUMN_NAME + " TEXT, " +
        StoryTable.COLUMN_CREATE_DATE + " INTEGER NOT NULL, " +
        StoryTable.COLUMN_MODIFY_DATE + " INTEGER NOT NULL, " +
        StoryTable.COLUMN_TEXT + " TEXT, " +
        StoryTable.COLUMN_PREVIEW + " TEXT)";
}
