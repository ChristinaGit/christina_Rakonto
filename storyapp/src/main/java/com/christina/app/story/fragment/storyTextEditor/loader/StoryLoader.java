package com.christina.app.story.fragment.storyTextEditor.loader;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;

import com.christina.api.story.dao.StoryDaoManager;

public final class StoryLoader extends AsyncTaskLoader<StoryLoaderResult> {
    public StoryLoader(@NonNull final Context context, final long storyId) {
        super(context);

        _storyId = storyId;
    }

    @Override
    public StoryLoaderResult loadInBackground() {
        return new StoryLoaderResult(StoryDaoManager.getStoryDao().get(_storyId));
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

    private final long _storyId;
}
