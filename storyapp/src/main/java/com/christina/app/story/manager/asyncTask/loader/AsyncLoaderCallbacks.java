package com.christina.app.story.manager.asyncTask.loader;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;

import com.christina.common.AsyncCallback;
import com.christina.common.AsyncResult;
import com.christina.common.contract.Contracts;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
public class AsyncLoaderCallbacks<TResult, TError>
    implements LoaderCallbacks<AsyncResult<TResult, TError>> {

    public AsyncLoaderCallbacks(
        @NonNull final AsyncCallback<TResult, TError> asyncCallback,
        @NonNull final Loader<AsyncResult<TResult, TError>> loader) {
        Contracts.requireNonNull(asyncCallback, "asyncCallback == null");
        Contracts.requireNonNull(loader, "loader == null");

        _asyncCallback = asyncCallback;
        _loader = loader;
    }

    @Override
    public Loader<AsyncResult<TResult, TError>> onCreateLoader(final int id, final Bundle args) {
        return getLoader();
    }

    @Override
    public void onLoadFinished(
        final Loader<AsyncResult<TResult, TError>> loader,
        final AsyncResult<TResult, TError> data) {
        if (data == null) {
            getAsyncCallback().onError(null);
        } else if (data.isSuccess()) {
            getAsyncCallback().onSuccess(data.getResult());
        } else {
            getAsyncCallback().onError(data.getError());
        }
    }

    @Override
    public void onLoaderReset(final Loader loader) {
    }

    @Getter(AccessLevel.PROTECTED)
    @NonNull
    private final AsyncCallback<TResult, TError> _asyncCallback;

    @Getter(AccessLevel.PROTECTED)
    @NonNull
    private final Loader<AsyncResult<TResult, TError>> _loader;
}
