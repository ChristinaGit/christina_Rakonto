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
import com.christina.content.story.contract.StoryContract;
import com.christina.content.story.database.StoryTable;
import com.christina.content.story.model.Story;

public final class StoryDao extends ContentProviderDao<Story> {
    public StoryDao(@NonNull ContentResolver contentResolver) {
        super(contentResolver, _FullProjection.PROJECTION);
    }

    @NonNull
    @Override
    protected final Story createModel() {
        return new Story();
    }

    @NonNull
    @Override
    protected final Story createModel(@NonNull final Cursor cursor) {
        Contracts.requireNonNull(cursor, "cursor == null");

        final Story model = createModel();

        model.setId(_FullProjection.getId(cursor));
        model.setName(_FullProjection.getName(cursor));
        model.setCreateDate(_FullProjection.getCreateDate(cursor));
        model.setModifyDate(_FullProjection.getModifyDate(cursor));
        model.setText(_FullProjection.getText(cursor));
        final String preview = _FullProjection.getPreview(cursor);
        if (preview != null) {
            model.setPreviewUri(Uri.parse(preview));
        } else {
            model.setPreviewUri(null);
        }

        return model;
    }

    @NonNull
    @Override
    protected final Story[] createModelArray(
        @IntRange(from = 0, to = Integer.MAX_VALUE) final int size) {
        Contracts.requireInRange(size, 0, Integer.MAX_VALUE);

        return new Story[size];
    }

    @NonNull
    @Override
    protected final ContentValues getContentValues(@NonNull final Story model) {
        Contracts.requireNonNull(model, "model == null");

        final ContentValues values = new ContentValues(_FullProjection.COLUMN_COUNT);

        values.put(StoryTable.Story.COLUMN_NAME, model.getName());
        values.put(StoryTable.Story.COLUMN_CREATE_DATE, model.getCreateDate());
        values.put(StoryTable.Story.COLUMN_MODIFY_DATE, model.getModifyDate());
        values.put(StoryTable.Story.COLUMN_TEXT, model.getText());

        final Uri previewUri = model.getPreviewUri();
        if (previewUri == null) {
            values.putNull(StoryTable.Story.COLUMN_PREVIEW);
        } else {
            values.put(StoryTable.Story.COLUMN_PREVIEW, previewUri.toString());
        }

        return values;
    }

    @Override
    protected final long extractId(@NonNull final Uri modelUri) {
        Contracts.requireNonNull(modelUri, "modelUri == null");

        return Long.parseLong(StoryContract.extractStoryId(modelUri));
    }

    @NonNull
    @Override
    protected final Uri getModelUri() {
        return StoryContract.getStoriesUri();
    }

    @NonNull
    @Override
    protected final Uri getModelUri(final long id) {
        return StoryContract.getStoryUri(String.valueOf(id));
    }

    private final static class _FullProjection {
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
            return cursor.getLong(INDEX_ID);
        }

        @Nullable
        public static String getName(@NonNull final Cursor cursor) {
            return cursor.getString(INDEX_NAME);
        }

        public static long getCreateDate(@NonNull final Cursor cursor) {
            return cursor.getLong(INDEX_CREATE_DATE);
        }

        public static long getModifyDate(@NonNull final Cursor cursor) {
            return cursor.getLong(INDEX_MODIFY_DATE);
        }

        @Nullable
        public static String getText(@NonNull final Cursor cursor) {
            return cursor.getString(INDEX_TEXT);
        }

        @Nullable
        public static String getPreview(@NonNull final Cursor cursor) {
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
}
