package com.christina.content.story;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.christina.common.data.database.SqlLiteDatabaseWrapper;
import com.christina.api.story.database.StoryTable;

public final class StoryDatabase extends SqlLiteDatabaseWrapper {
    public static final String NAME = "Stories.db";

    public static final int VERSION = 1;

    public static final String CREATE_STORY_TABLE =
        "CREATE TABLE IF NOT EXISTS " + StoryTable.Story.NAME + "(" +
        StoryTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        StoryTable.Story.COLUMN_NAME + " TEXT, " +
        StoryTable.Story.COLUMN_CREATE_DATE + " INTEGER NOT NULL, " +
        StoryTable.Story.COLUMN_MODIFY_DATE + " INTEGER NOT NULL, " +
        StoryTable.Story.COLUMN_TEXT + " TEXT, " +
        StoryTable.Story.COLUMN_PREVIEW + " TEXT)";

    public static final String CREATE_STORY_FRAME_TABLE =
        "CREATE TABLE IF NOT EXISTS " + StoryTable.StoryFrame.NAME + "(" +
        StoryTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        StoryTable.StoryFrame.COLUMN_STORY_ID + " INTEGER NOT NULL, " +
        StoryTable.StoryFrame.COLUMN_TEXT_POSITION + " INTEGER NOT NULL, " +
        StoryTable.StoryFrame.COLUMN_IMAGE + " TEXT)";

    public StoryDatabase(@NonNull final Context context) {
        this(context, null);
    }

    public StoryDatabase(@NonNull final Context context,
        @Nullable final DatabaseErrorHandler errorHandler) {
        super(context, NAME, null, VERSION, errorHandler);
    }

    @Override
    public final void onConfigure(final SQLiteDatabase db) {
        super.onConfigure(db);

        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public final void onCreate(final SQLiteDatabase db) {
        db.execSQL(CREATE_STORY_TABLE);
        db.execSQL(CREATE_STORY_FRAME_TABLE);
    }

    @Override
    public final void onUpgrade(final SQLiteDatabase db, final int oldVersion,
        final int newVersion) {
    }
}
