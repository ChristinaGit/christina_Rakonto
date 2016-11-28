package com.christina.content.story;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.christina.api.story.database.StoryTable;
import com.christina.common.data.database.SQLiteDatabaseWrapper;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
public final class StoryDatabase extends SQLiteDatabaseWrapper {
    public static final String NAME = "Stories.db";

    public static final int VERSION = 1;

    public StoryDatabase(@NonNull final Context context) {
        this(context, null);
    }

    public StoryDatabase(
        @NonNull final Context context, @Nullable final DatabaseErrorHandler errorHandler) {
        super(context, NAME, null, VERSION, errorHandler);
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
        "CREATE TABLE IF NOT EXISTS " + StoryTable.StoryFrame.NAME + "(" +
        StoryTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        StoryTable.StoryFrame.COLUMN_STORY_ID + " INTEGER NOT NULL, " +
        StoryTable.StoryFrame.COLUMN_TEXT_START_POSITION + " INTEGER NOT NULL, " +
        StoryTable.StoryFrame.COLUMN_TEXT_END_POSITION + " INTEGER NOT NULL, " +
        StoryTable.StoryFrame.COLUMN_IMAGE + " TEXT, " +
        "FOREIGN KEY (" + StoryTable.StoryFrame.COLUMN_STORY_ID + ") REFERENCES " +
        StoryTable.Story.NAME + "(" + StoryTable.COLUMN_ID + "))";

    @Getter(value = AccessLevel.PRIVATE, lazy = true)
    @NonNull
    private final String _createStoryTableQuery =
        "CREATE TABLE IF NOT EXISTS " + StoryTable.Story.NAME + "(" +
        StoryTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        StoryTable.Story.COLUMN_NAME + " TEXT, " +
        StoryTable.Story.COLUMN_CREATE_DATE + " INTEGER NOT NULL, " +
        StoryTable.Story.COLUMN_MODIFY_DATE + " INTEGER NOT NULL, " +
        StoryTable.Story.COLUMN_TEXT + " TEXT, " +
        StoryTable.Story.COLUMN_PREVIEW + " TEXT)";
}
