package com.christina.api.story.dao.storyFrame;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.christina.api.story.database.StoryFrameTable;
import com.christina.common.contract.Contracts;
import com.christina.common.data.projection.Projection;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
public final class StoryFrameFullProjection implements Projection {
    public StoryFrameFullProjection() {
        int indexer = 0;

        _indexId = indexer++;
        _indexStoryId = indexer++;
        _indexTextStartPosition = indexer++;
        _indexTextEndPosition = indexer++;
        _indexImage = indexer++;

        _columns = new String[indexer];

        _columns[_indexId] = StoryFrameTable.COLUMN_ID;
        _columns[_indexStoryId] = StoryFrameTable.COLUMN_STORY_ID;
        _columns[_indexTextStartPosition] = StoryFrameTable.COLUMN_TEXT_START_POSITION;
        _columns[_indexTextEndPosition] = StoryFrameTable.COLUMN_TEXT_END_POSITION;
        _columns[_indexImage] = StoryFrameTable.COLUMN_IMAGE;
    }

    public final long getId(@NonNull final Cursor cursor) {
        Contracts.requireNonNull(cursor, "cursor == null");

        return cursor.getLong(getIndexId());
    }

    @Nullable
    public final String getImage(@NonNull final Cursor cursor) {
        Contracts.requireNonNull(cursor, "cursor == null");

        return cursor.getString(getIndexImage());
    }

    public final long getStoryId(@NonNull final Cursor cursor) {
        Contracts.requireNonNull(cursor, "cursor == null");

        return cursor.getLong(getIndexStoryId());
    }

    public final int getTextEndPosition(@NonNull final Cursor cursor) {
        Contracts.requireNonNull(cursor, "cursor == null");

        return cursor.getInt(getIndexTextEndPosition());
    }

    public final int getTextStartPosition(@NonNull final Cursor cursor) {
        Contracts.requireNonNull(cursor, "cursor == null");

        return cursor.getInt(getIndexTextStartPosition());
    }

    @Getter(onMethod = @__(@Override))
    @NonNull
    private final String[] _columns;

    @Getter
    private final int _indexId;

    @Getter
    private final int _indexImage;

    @Getter
    private final int _indexStoryId;

    @Getter
    private final int _indexTextEndPosition;

    @Getter
    private final int _indexTextStartPosition;
}
