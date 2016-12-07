package com.christina.app.story.manager.asyncTask;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;

import com.christina.api.story.model.Story;
import com.christina.app.story.manager.asyncTask.loader.AsyncLoaderCallbacks;
import com.christina.app.story.manager.asyncTask.loader.StoriesLoader;
import com.christina.app.story.manager.asyncTask.task.InsertNewStoryAsyncTask;
import com.christina.app.story.manager.asyncTask.task.LoadStoryAsyncTask;
import com.christina.app.story.manager.asyncTask.task.UpdateStoryAsyncTask;
import com.christina.app.story.manager.content.StoryDaoManager;
import com.christina.common.AsyncCallback;
import com.christina.common.contract.Contracts;
import com.christina.common.data.cursor.dataCursor.DataCursor;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;

@Accessors(prefix = "_")
public final class ActivityStoryTaskManager implements StoryTaskManager {
    private static int _loaderIndexer = 0;

    private static final int LOADER_ID_STORIES = _loaderIndexer++;

    public ActivityStoryTaskManager(
        @NonNull final Context context,
        @NonNull final LoaderManager loaderManager,
        @NonNull final StoryDaoManager storyDaoManager) {
        Contracts.requireNonNull(context, "context == null");
        Contracts.requireNonNull(loaderManager, "loaderManager == null");
        Contracts.requireNonNull(storyDaoManager, "storyDaoManager == null");

        _context = context;
        _loaderManager = loaderManager;
        _storyDaoManager = storyDaoManager;
    }

    @Override
    public void insertStory(
        @NonNull final AsyncCallback<Story, Exception> callback) {
        Contracts.requireNonNull(callback, "callback == null");

        getInsertNewStoryAsyncTask(callback).execute();
    }

    @Override
    public void loadStories(
        @NonNull final AsyncCallback<DataCursor<Story>, Exception> callback) {
        Contracts.requireNonNull(callback, "callback == null");

        final val asyncLoaderCallbacks = new AsyncLoaderCallbacks<>(callback, getStoriesLoader());
        getLoaderManager().restartLoader(LOADER_ID_STORIES, null, asyncLoaderCallbacks);
    }

    @Override
    public void loadStory(
        final long storyId, @NonNull final AsyncCallback<Story, Exception> callback) {
        Contracts.requireNonNull(callback, "callback == null");

        getLoadStoryAsyncTask(storyId, callback).execute();
    }

    @Override
    public void updateStory(
        @NonNull final Story story, @Nullable final AsyncCallback<Integer, Exception> callback) {
        Contracts.requireNonNull(story, "story == null");

        getUpdateStoryAsyncTask(story, callback).execute();
    }

    @Override
    public void updateStory(@NonNull final Story story) {
        Contracts.requireNonNull(story, "story == null");

        updateStory(story, null);
    }

    @NonNull
    protected final InsertNewStoryAsyncTask getInsertNewStoryAsyncTask(
        @NonNull final AsyncCallback<Story, Exception> callback) {
        Contracts.requireNonNull(callback, "callback == null");

        return new InsertNewStoryAsyncTask(getStoryDaoManager(), callback);
    }

    @NonNull
    protected final LoadStoryAsyncTask getLoadStoryAsyncTask(
        final long storyId, @NonNull final AsyncCallback<Story, Exception> callback) {
        Contracts.requireNonNull(callback, "callback == null");

        return new LoadStoryAsyncTask(getStoryDaoManager(), storyId, callback);
    }

    @NonNull
    protected final StoriesLoader getStoriesLoader() {
        return new StoriesLoader(getContext(), getStoryDaoManager());
    }

    protected final UpdateStoryAsyncTask getUpdateStoryAsyncTask(
        @NonNull final Story story, @Nullable final AsyncCallback<Integer, Exception> callback) {
        Contracts.requireNonNull(story, "story == null");

        return new UpdateStoryAsyncTask(getStoryDaoManager(), story, callback);
    }

    @Getter(AccessLevel.PROTECTED)
    @NonNull
    private final Context _context;

    @Getter(AccessLevel.PROTECTED)
    @NonNull
    private final LoaderManager _loaderManager;

    @Getter(AccessLevel.PROTECTED)
    @NonNull
    private final StoryDaoManager _storyDaoManager;
}
