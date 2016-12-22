package com.christina.app.story.manager.asyncTask.task;

import android.os.AsyncTask;
import android.support.annotation.CallSuper;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.christina.app.story.manager.content.StoryDaoManager;
import com.christina.common.AsyncCallback;
import com.christina.common.AsyncResult;
import com.christina.common.contract.Contracts;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;

@Accessors(prefix = "_")
public abstract class BaseStoryAsyncTask<TResult, TError, TProgress>
    extends AsyncTask<Void, TProgress, AsyncResult<TResult, TError>> {
    protected BaseStoryAsyncTask(
        @NonNull final StoryDaoManager storyDaoManager,
        @Nullable final AsyncCallback<TResult, TError> callback) {
        Contracts.requireNonNull(storyDaoManager, "storyDaoManager == null");

        _storyDaoManager = storyDaoManager;
        _callback = callback;
    }

    @Override
    protected final AsyncResult<TResult, TError> doInBackground(
        final Void... params) {
        return doInBackground();
    }

    @Override
    protected final void onPostExecute(final AsyncResult<TResult, TError> result) {
        super.onPostExecute(Contracts.requireNonNull(result, "result == null"));

        if (result.isSuccess()) {
            onSuccess(result.getResult());
        } else {
            onError(result.getError());
        }
    }

    @CallSuper
    @MainThread
    protected void onError(@Nullable final TError error) {
        Contracts.requireMainThread();

        final val callback = getCallback();
        if (callback != null) {
            callback.onError(error);
        }
    }

    @CallSuper
    @MainThread
    protected void onSuccess(@Nullable final TResult result) {
        Contracts.requireMainThread();

        final val callback = getCallback();
        if (callback != null) {
            callback.onSuccess(result);
        }
    }

    @WorkerThread
    @NonNull
    protected abstract AsyncResult<TResult, TError> doInBackground();

    @Getter(AccessLevel.PRIVATE)
    @Nullable
    private final AsyncCallback<TResult, TError> _callback;

    @Getter(AccessLevel.PROTECTED)
    @NonNull
    private final StoryDaoManager _storyDaoManager;
}
