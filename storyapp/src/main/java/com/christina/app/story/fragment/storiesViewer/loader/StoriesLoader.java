package com.christina.app.story.fragment.storiesViewer.loader;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.christina.api.story.dao.story.StoryDao;
import com.christina.api.story.database.StoryTable;
import com.christina.api.story.model.Story;
import com.christina.common.ConstantBuilder;
import com.christina.common.contract.Contracts;
import com.christina.common.data.cursor.dataCursor.DataCursor;

public final class StoriesLoader extends AsyncTaskLoader<StoriesLoaderResult> {
    private static final String _LOG_TAG = ConstantBuilder.logTag(StoriesLoader.class);

    public StoriesLoader(@NonNull final Context context, @NonNull final StoryDao storyDao) {
        super(context);
        Contracts.requireNonNull(context, "context == null");
        Contracts.requireNonNull(storyDao, "storyDao == null");

        _storyDao = storyDao;
    }

    @Override
    public final StoriesLoaderResult loadInBackground() {
        final DataCursor<Story> stories = _storyDao.getAll().asDataCursor();

        final int count;
        if (stories != null) {
            count = stories.getCount();

            for (int i = 0; i < count;i++){
                stories.moveToPosition(i);
                final Story data = stories.getData();
                Log.d(_LOG_TAG, "loadInBackground " + data.getId());
                Log.d(_LOG_TAG, "loadInBackground " + data.getName());
                Log.d(_LOG_TAG, "loadInBackground " + stories.getString(stories.getColumnIndex(
                    StoryTable.Story.COLUMN_NAME)));
            }
        }


        return new StoriesLoaderResult(stories);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        forceLoad();
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();

        cancelLoad();
    }

    @NonNull
    private final StoryDao _storyDao;
}
