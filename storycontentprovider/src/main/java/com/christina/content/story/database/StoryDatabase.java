package com.christina.content.story.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.christina.common.data.database.SqlLiteDatabaseWrapper;

public final class StoryDatabase extends SqlLiteDatabaseWrapper {
    public static final String NAME = "Stories.db";

    public static final int VERSION = 1;

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
        db.execSQL(Table.Story.CREATE);
        db.execSQL(Table.StoryFrame.CREATE);
    }

    @Override
    public final void onUpgrade(final SQLiteDatabase db, final int oldVersion,
                                final int newVersion) {
    }
}
