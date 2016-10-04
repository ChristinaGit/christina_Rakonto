package com.christina.content.story;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.christina.common.contract.Contracts;
import com.christina.common.data.QueryUtils;
import com.christina.common.data.content.ContentProviderBase;
import com.christina.common.data.database.Database;
import com.christina.content.story.contract.StoryContentCode;
import com.christina.content.story.contract.StoryContract;
import com.christina.content.story.contract.StoryFrameContract;
import com.christina.content.story.database.StoryDatabase;
import com.christina.content.story.database.StoryTable;

public final class StoryContentProvider extends ContentProviderBase {
    @Nullable
    @Override
    public final Cursor query(@NonNull final Uri uri, @Nullable final String[] projection,
        @Nullable final String selection, @Nullable final String[] selectionArgs,
        @Nullable final String sortOrder) {
        Contracts.requireNonNull(uri, "uri == null");

        final Cursor result;

        final int code = StoryContentCode.Matcher.get(uri);
        final int entityCode = StoryContentCode.getEntityTypeCode(code);

        if (StoryContract.ENTITY_TYPE_CODE == entityCode) {
            result = _queryStoryEntity(code, uri, projection, selection, selectionArgs, sortOrder);
        } else if (StoryFrameContract.ENTITY_TYPE_CODE == entityCode) {
            result =
                _queryStoryFrameEntity(code, uri, projection, selection, selectionArgs, sortOrder);
        } else {
            throw new IllegalArgumentException("Unsupported uri: " + uri);
        }

        return result;
    }

    @Nullable
    @Override
    public final String getType(@NonNull final Uri uri) {
        Contracts.requireNonNull(uri, "uri == null");

        final String result;

        final int code = StoryContentCode.Matcher.get(uri);
        final int entityCode = StoryContentCode.getEntityTypeCode(code);

        if (StoryContract.ENTITY_TYPE_CODE == entityCode) {
            result = StoryContract.getType(code);
        } else if (StoryFrameContract.ENTITY_TYPE_CODE == entityCode) {
            result = StoryFrameContract.getType(code);
        } else {
            throw new IllegalArgumentException("Unsupported uri: " + uri);
        }

        return result;
    }

    @Nullable
    @Override
    public final Uri insert(@NonNull final Uri uri, @Nullable final ContentValues values) {
        Contracts.requireNonNull(uri, "uri == null");

        final Uri result;

        final int code = StoryContentCode.Matcher.get(uri);

        if (StoryContract.CODE_STORIES == code) {
            result = _insertStory(values);
        } else if (StoryFrameContract.CODE_STORY_FRAMES == code) {
            result = _insertStoryFrame(values);
        } else {
            throw new IllegalArgumentException("Unsupported uri: " + uri);
        }

        return result;
    }

    @Override
    public final int delete(@NonNull final Uri uri, @Nullable final String selection,
        @Nullable final String[] selectionArgs) {
        Contracts.requireNonNull(uri, "uri == null");

        final int result;

        final int code = StoryContentCode.Matcher.get(uri);
        final int entityCode = StoryContentCode.getEntityTypeCode(code);

        if (StoryContract.ENTITY_TYPE_CODE == entityCode) {
            result = _deleteStoryEntity(code, uri, selection, selectionArgs);
        } else if (StoryFrameContract.ENTITY_TYPE_CODE == entityCode) {
            result = _deleteStoryFrameEntity(code, uri, selection, selectionArgs);
        } else {
            throw new IllegalArgumentException("Unsupported uri: " + uri);
        }

        return result;
    }

    @Override
    public final int update(@NonNull final Uri uri, @Nullable final ContentValues values,
        @Nullable final String selection, @Nullable final String[] selectionArgs) {
        Contracts.requireNonNull(uri, "uri == null");

        final int result;

        final int code = StoryContentCode.Matcher.get(uri);
        final int entityCode = StoryContentCode.getEntityTypeCode(code);

        if (StoryContract.ENTITY_TYPE_CODE == entityCode) {
            result = _updateStoryEntity(code, uri, values, selection, selectionArgs);
        } else if (StoryFrameContract.ENTITY_TYPE_CODE == entityCode) {
            result = _updateStoryFrameEntity(code, uri, values, selection, selectionArgs);
        } else {
            throw new IllegalArgumentException("Unsupported uri: " + uri);
        }

        return result;
    }

    @NonNull
    @Override
    protected final Database onCreateDatabase(@NonNull final Context context) {
        return new StoryDatabase(context);
    }

    private int _deleteStories(@Nullable final String selection,
        @Nullable final String[] selectionArgs) {
        _deleteStoryFrames(
            StoryTable.StoryFrame.COLUMN_STORY_ID + " IN (SELECT " + StoryTable.COLUMN_ID +
            " FROM " + StoryTable.Story.NAME + " WHERE " + selection + " )", selectionArgs);

        final long[] changesIds = _getStoryChangesIds(selection, selectionArgs);

        final int result = getDatabase().delete(StoryTable.Story.NAME, selection, selectionArgs);

        _notifyStoryChanges(changesIds);

        return result;
    }

    private int _deleteStory(@NonNull final String id) {
        Contracts.requireNonNull(id, "id == null");

        return _deleteStories(StoryTable.COLUMN_ID + "=?", new String[]{id});
    }

    private int _deleteStoryEntity(final int code, @NonNull final Uri uri,
        @Nullable final String selection, @Nullable final String[] selectionArgs) {
        Contracts.requireNonNull(uri, "uri == null");

        int result = 0;

        if (StoryContract.CODE_STORY == code) {
            result += _deleteStory(StoryContract.extractStoryId(uri));
        } else if (StoryContract.CODE_STORIES == code) {
            result += _deleteStories(selection, selectionArgs);
        } else {
            throw new IllegalArgumentException("Unsupported uri: " + uri);
        }

        return result;
    }

    private int _deleteStoryFrame(@NonNull final String id) {
        Contracts.requireNonNull(id, "id == null");

        return _deleteStoryFrames(StoryTable.COLUMN_ID + "=?", new String[]{id});
    }

    private int _deleteStoryFrameEntity(final int code, @NonNull final Uri uri,
        @Nullable final String selection, @Nullable final String[] selectionArgs) {
        Contracts.requireNonNull(uri, "uri == null");

        int result = 0;

        if (StoryFrameContract.CODE_STORY_FRAME == code) {
            result += _deleteStoryFrame(StoryFrameContract.extractStoryFrameId(uri));
        } else if (StoryFrameContract.CODE_STORY_FRAMES == code) {
            result += _deleteStoryFrames(selection, selectionArgs);
        } else {
            throw new IllegalArgumentException("Unsupported uri: " + uri);
        }

        return result;
    }

    private int _deleteStoryFrames(@Nullable final String selection,
        @Nullable final String[] selectionArgs) {
        final long[] changesIds = _getStoryFrameChangesIds(selection, selectionArgs);

        final int result =
            getDatabase().delete(StoryTable.StoryFrame.NAME, selection, selectionArgs);

        _notifyStoryFrameChanges(changesIds);

        return result;
    }

    @Nullable
    private long[] _getStoryChangesIds(@Nullable final String selection,
        @Nullable final String[] selectionArgs) {
        long[] result = null;
        try (final Cursor cursor = getDatabase().query(StoryTable.Story.NAME,
            new String[]{StoryTable.COLUMN_ID}, selection, selectionArgs)) {
            if (cursor != null) {
                result = new long[cursor.getCount()];

                for (int i = 0; cursor.moveToNext(); i++) {
                    result[i] = cursor.getLong(0);
                }
            }
        }
        return result;
    }

    @Nullable
    private long[] _getStoryFrameChangesIds(@Nullable final String selection,
        @Nullable final String[] selectionArgs) {
        long[] result = null;
        try (final Cursor cursor = getDatabase().query(StoryTable.StoryFrame.NAME,
            new String[]{StoryTable.COLUMN_ID}, selection, selectionArgs)) {
            if (cursor != null) {
                result = new long[cursor.getCount()];

                for (int i = 0; cursor.moveToNext(); i++) {
                    result[i] = cursor.getLong(0);
                }
            }
        }
        return result;
    }

    @Nullable
    private Uri _insertStory(@Nullable final ContentValues values) {
        Uri result = null;

        if (values != null) {
            final long id = getDatabase().insert(StoryTable.Story.NAME, values);

            if (id >= 0) {
                result = StoryContract.getStoryUri(String.valueOf(id));

                notifyChange(result);
            }
        }

        return result;
    }

    @Nullable
    private Uri _insertStoryFrame(@Nullable final ContentValues values) {
        Uri result = null;

        if (values != null) {
            final long id = getDatabase().insert(StoryTable.StoryFrame.NAME, values);

            if (id >= 0) {
                result = StoryFrameContract.getStoryFrameUri(String.valueOf(id));

                notifyChange(result);
            }
        }

        return result;
    }

    private void _notifyStoryChanges(@Nullable final long... storyIds) {
        if (storyIds != null) {
            for (final long storyId : storyIds) {
                final String id = String.valueOf(storyId);
                final Uri notifyUri = StoryContract.getStoryUri(id);

                notifyChange(notifyUri);
            }
        }
    }

    private void _notifyStoryFrameChanges(@Nullable final long... storyFrameIds) {
        if (storyFrameIds != null) {
            for (final long storyFrameId : storyFrameIds) {
                final String id = String.valueOf(storyFrameId);
                final Uri notifyUri = StoryFrameContract.getStoryFrameUri(id);

                notifyChange(notifyUri);
            }
        }
    }

    @Nullable
    private Cursor _queryStories(@Nullable final String[] projection,
        @Nullable final String selection, @Nullable final String[] selectionArgs,
        @Nullable final String sortOrder) {
        return getDatabase().query(StoryTable.Story.NAME, projection, selection, selectionArgs,
            sortOrder);
    }

    @Nullable
    private Cursor _queryStory(@NonNull final String id, @Nullable final String[] projection,
        @Nullable final String sortOrder) {
        Contracts.requireNonNull(id, "id == null");

        return _queryStories(projection, StoryTable.COLUMN_ID + "=?", new String[]{id}, sortOrder);
    }

    @Nullable
    private Cursor _queryStoryEntity(final int code, @NonNull final Uri uri,
        @Nullable final String[] projection, @Nullable final String selection,
        @Nullable final String[] selectionArgs, @Nullable final String sortOrder) {
        Contracts.requireNonNull(uri, "uri == null");

        final Cursor result;

        if (StoryContract.CODE_STORY == code) {
            final String id = StoryContract.extractStoryId(uri);
            result = _queryStory(id, projection, sortOrder);
        } else if (StoryContract.CODE_STORIES == code) {
            result = _queryStories(projection, selection, selectionArgs, sortOrder);
        } else {
            throw new IllegalArgumentException("Unsupported uri: " + uri);
        }

        return result;
    }

    @Nullable
    private Cursor _queryStoryFrame(@NonNull final String id, @Nullable final String[] projection,
        @Nullable final String sortOrder) {
        Contracts.requireNonNull(id, "id == null");

        return _queryStoryFrames(projection, StoryTable.COLUMN_ID + "=?", new String[]{id},
            sortOrder);
    }

    @Nullable
    private Cursor _queryStoryFrameEntity(final int code, @NonNull final Uri uri,
        @Nullable final String[] projection, @Nullable final String selection,
        @Nullable final String[] selectionArgs, @Nullable final String sortOrder) {
        Contracts.requireNonNull(uri, "uri == null");

        final Cursor result;

        if (StoryFrameContract.CODE_STORY_FRAME == code) {
            final String id = StoryFrameContract.extractStoryFrameId(uri);
            result = _queryStoryFrame(id, projection, sortOrder);
        } else if (StoryFrameContract.CODE_STORY_FRAMES == code) {
            result = _queryStoryFrames(projection, selection, selectionArgs, sortOrder);
        } else if (StoryFrameContract.CODE_STORY_FRAMES_BY_STORY == code) {
            final String storyId = StoryFrameContract.extractStoryId(uri);
            result =
                _queryStoryFramesByStory(storyId, projection, selection, selectionArgs, sortOrder);
        } else {
            throw new IllegalArgumentException("Unsupported uri: " + uri);
        }

        return result;
    }

    @Nullable
    private Cursor _queryStoryFrames(@Nullable final String[] projection,
        @Nullable final String selection, @Nullable final String[] selectionArgs,
        @Nullable final String sortOrder) {
        return getDatabase().query(StoryTable.StoryFrame.NAME, projection, selection, selectionArgs,
            sortOrder);
    }

    @Nullable
    private Cursor _queryStoryFramesByStory(@NonNull final String storyId,
        @Nullable final String[] projection, @Nullable String selection,
        @Nullable final String[] selectionArgs, @Nullable final String sortOrder) {
        Contracts.requireNonNull(storyId, "storyId == null");

        selection +=
            QueryUtils.appendWhereEquals(selection, StoryTable.StoryFrame.COLUMN_STORY_ID, storyId);
        return _queryStoryFrames(projection, selection, selectionArgs, sortOrder);
    }

    private int _updateStories(@Nullable final ContentValues values,
        @Nullable final String selection, @Nullable final String[] selectionArgs) {
        final long[] changesIds = _getStoryChangesIds(selection, selectionArgs);

        final int result =
            getDatabase().update(StoryTable.Story.NAME, values, selection, selectionArgs);

        _notifyStoryChanges(changesIds);

        return result;
    }

    private int _updateStory(@NonNull final String id, @Nullable final ContentValues values) {
        Contracts.requireNonNull(id, "id == null");

        return _updateStories(values, StoryTable.COLUMN_ID + "=?", new String[]{id});
    }

    private int _updateStoryEntity(final int code, @NonNull final Uri uri,
        @Nullable final ContentValues values, @Nullable final String selection,
        @Nullable final String[] selectionArgs) {
        Contracts.requireNonNull(uri, "uri == null");

        int result = 0;

        if (StoryContract.CODE_STORY == code) {
            result += _updateStory(StoryContract.extractStoryId(uri), values);
        } else if (StoryContract.CODE_STORIES == code) {
            result += _updateStories(values, selection, selectionArgs);
        } else {
            throw new IllegalArgumentException("Unsupported uri: " + uri);
        }

        return result;
    }

    private int _updateStoryFrame(@NonNull final String id, @Nullable final ContentValues values) {
        Contracts.requireNonNull(id, "id == null");

        return _updateStoryFrames(values, StoryTable.COLUMN_ID + "=?", new String[]{id});
    }

    private int _updateStoryFrameEntity(final int code, @NonNull final Uri uri,
        @Nullable final ContentValues values, @Nullable final String selection,
        @Nullable final String[] selectionArgs) {
        Contracts.requireNonNull(uri, "uri == null");

        int result = 0;

        if (StoryFrameContract.CODE_STORY_FRAME == code) {
            result += _updateStoryFrame(StoryFrameContract.extractStoryFrameId(uri), values);
        } else if (StoryFrameContract.CODE_STORY_FRAMES == code) {
            result += _updateStoryFrames(values, selection, selectionArgs);
        } else {
            throw new IllegalArgumentException("Unsupported uri: " + uri);
        }

        return result;
    }

    private int _updateStoryFrames(@Nullable final ContentValues values,
        @Nullable final String selection, @Nullable final String[] selectionArgs) {
        final long[] changesIds = _getStoryFrameChangesIds(selection, selectionArgs);

        final int result =
            getDatabase().update(StoryTable.StoryFrame.NAME, values, selection, selectionArgs);

        _notifyStoryFrameChanges(changesIds);

        return result;
    }
}
