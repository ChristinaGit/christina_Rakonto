package com.christina.app.story.manager.asyncTask.loader;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;

import com.christina.app.story.manager.content.StoryDaoManager;
import com.christina.common.AsyncResult;
import com.christina.common.contract.Contracts;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
public abstract class BaseStoryLoader<TResult, TError>
    extends AsyncTaskLoader<AsyncResult<TResult, TError>> {
    public BaseStoryLoader(
        @NonNull final Context context, @NonNull final StoryDaoManager storyDaoManager) {
        super(Contracts.requireNonNull(context, "context == null"));
        Contracts.requireNonNull(storyDaoManager, "storyDaoManager == null");

        _storyDaoManager = storyDaoManager;
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

    @Getter(AccessLevel.PROTECTED)
    @NonNull
    private final StoryDaoManager _storyDaoManager;
}
