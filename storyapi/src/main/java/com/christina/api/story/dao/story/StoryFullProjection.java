package com.christina.api.story.dao.story;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.christina.api.story.database.StoryTable;
import com.christina.common.contract.Contracts;
import com.christina.common.data.projection.Projection;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
public final class StoryFullProjection implements Projection {
    public StoryFullProjection() {
        int indexer = 0;

        _indexId = indexer++;
        _indexName = indexer++;
        _indexCreateDate = indexer++;
        _indexModifyDate = indexer++;
        _indexText = indexer++;
        _indexPreview = indexer++;

        _columns = new String[indexer];

        _columns[_indexId] = StoryTable.COLUMN_ID;
        _columns[_indexName] = StoryTable.COLUMN_NAME;
        _columns[_indexCreateDate] = StoryTable.COLUMN_CREATE_DATE;
        _columns[_indexModifyDate] = StoryTable.COLUMN_MODIFY_DATE;
        _columns[_indexText] = StoryTable.COLUMN_TEXT;
        _columns[_indexPreview] = StoryTable.COLUMN_PREVIEW;
    }

    public final long getCreateDate(@NonNull final Cursor cursor) {
        Contracts.requireNonNull(cursor, "cursor == null");

        return cursor.getLong(getIndexCreateDate());
    }

    public final long getId(@NonNull final Cursor cursor) {
        Contracts.requireNonNull(cursor, "cursor == null");

        return cursor.getLong(getIndexId());
    }

    public final long getModifyDate(@NonNull final Cursor cursor) {
        Contracts.requireNonNull(cursor, "cursor == null");

        return cursor.getLong(getIndexModifyDate());
    }

    @Nullable
    public final String getName(@NonNull final Cursor cursor) {
        Contracts.requireNonNull(cursor, "cursor == null");

        return cursor.getString(getIndexName());
    }

    @Nullable
    public final String getPreview(@NonNull final Cursor cursor) {
        Contracts.requireNonNull(cursor, "cursor == null");

        return cursor.getString(getIndexPreview());
    }

    @Nullable
    public final String getText(@NonNull final Cursor cursor) {
        Contracts.requireNonNull(cursor, "cursor == null");

        return cursor.getString(getIndexText());
    }

    @Getter(onMethod = @__(@Override))
    @NonNull
    private final String[] _columns;

    @Getter
    private final int _indexCreateDate;

    @Getter
    private final int _indexId;

    @Getter
    private final int _indexModifyDate;

    @Getter
    private final int _indexName;

    @Getter
    private final int _indexPreview;

    @Getter
    private final int _indexText;
}
