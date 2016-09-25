package com.christina.content.story.dao;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.christina.common.contract.Contracts;
import com.christina.common.data.dao.ContentProviderDao;
import com.christina.content.story.contract.StoryFrameContract;
import com.christina.content.story.database.StoryTable;
import com.christina.content.story.model.StoryFrame;

public final class StoryFrameDao extends ContentProviderDao<StoryFrame> {
    public StoryFrameDao(@NonNull ContentResolver contentResolver) {
        super(contentResolver, _FullProjection.PROJECTION);
    }

    @NonNull
    @Override
    protected final StoryFrame createModel() {
        return new StoryFrame();
    }

    @NonNull
    @Override
    protected final StoryFrame createModel(@NonNull final Cursor cursor) {
        Contracts.requireNonNull(cursor, "cursor == null");

        final StoryFrame model = createModel();

        model.setId(_FullProjection.getId(cursor));
        model.setStoryId(_FullProjection.getStoryId(cursor));
        model.setTextPosition(_FullProjection.getTextPosition(cursor));
        final String imageUri = _FullProjection.getImage(cursor);
        if (imageUri != null) {
            model.setImageUri(Uri.parse(imageUri));
        } else {
            model.setImageUri(null);
        }

        return model;
    }

    @NonNull
    @Override
    protected final StoryFrame[] createModelArray(
        @IntRange(from = 0, to = Integer.MAX_VALUE) final int size) {
        Contracts.requireInRange(size, 0, Integer.MAX_VALUE);

        return new StoryFrame[size];
    }

    @NonNull
    @Override
    protected final ContentValues getContentValues(@NonNull final StoryFrame model) {
        Contracts.requireNonNull(model, "model == null");

        final ContentValues values = new ContentValues(_FullProjection.COLUMN_COUNT);

        values.put(StoryTable.StoryFrame.COLUMN_STORY_ID, model.getStoryId());
        values.put(StoryTable.StoryFrame.COLUMN_TEXT_POSITION, model.getTextPosition());
        final Uri imageUri = model.getImageUri();
        if (imageUri == null) {
            values.putNull(StoryTable.StoryFrame.COLUMN_IMAGE);
        } else {
            values.put(StoryTable.StoryFrame.COLUMN_IMAGE, imageUri.toString());
        }

        return values;
    }

    @Override
    protected final long extractId(@NonNull final Uri modelUri) {
        Contracts.requireNonNull(modelUri, "modelUri == null");

        return Long.parseLong(StoryFrameContract.extractStoryFrameId(modelUri));
    }

    @NonNull
    @Override
    protected final Uri getModelUri() {
        return StoryFrameContract.getStoryFramesUri();
    }

    @NonNull
    @Override
    protected final Uri getModelUri(final long id) {
        return StoryFrameContract.getStoryFrameUri(String.valueOf(id));
    }

    private final static class _FullProjection {
        public static final String[] PROJECTION;

        private static int _indexer = 0;

        public static final int INDEX_ID = _indexer++;

        public static final int INDEX_STORY_ID = _indexer++;

        public static final int INDEX_TEXT_POSITION = _indexer++;

        public static final int INDEX_IMAGE = _indexer++;

        public static final int COLUMN_COUNT = _indexer;

        public static long getId(@NonNull final Cursor cursor) {
            return cursor.getLong(INDEX_ID);
        }

        public static long getStoryId(@NonNull final Cursor cursor) {
            return cursor.getLong(INDEX_STORY_ID);
        }

        public static int getTextPosition(@NonNull final Cursor cursor) {
            return cursor.getInt(INDEX_TEXT_POSITION);
        }

        @Nullable
        public static String getImage(@NonNull final Cursor cursor) {
            return cursor.getString(INDEX_IMAGE);
        }

        static {
            PROJECTION = new String[_indexer];
            PROJECTION[INDEX_ID] = StoryTable.COLUMN_ID;
            PROJECTION[INDEX_STORY_ID] = StoryTable.StoryFrame.COLUMN_STORY_ID;
            PROJECTION[INDEX_TEXT_POSITION] = StoryTable.StoryFrame.COLUMN_TEXT_POSITION;
            PROJECTION[INDEX_IMAGE] = StoryTable.StoryFrame.COLUMN_IMAGE;
        }
    }
}
