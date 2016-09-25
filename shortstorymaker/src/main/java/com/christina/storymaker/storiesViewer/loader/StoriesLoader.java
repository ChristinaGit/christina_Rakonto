package com.christina.storymaker.storiesViewer.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.support.annotation.NonNull;

import com.christina.content.story.dao.StoryDaoManager;

public final class StoriesLoader extends AsyncTaskLoader<StoriesLoaderResult> {
    public StoriesLoader(@NonNull final Context context) {
        super(context);
    }

    @Override
    public final StoriesLoaderResult loadInBackground() {
        return new StoriesLoaderResult(StoryDaoManager.getStoryDao().get());
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        forceLoad();
    }
}
