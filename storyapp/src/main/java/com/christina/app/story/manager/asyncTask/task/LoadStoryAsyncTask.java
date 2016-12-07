package com.christina.app.story.manager.asyncTask.task;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.christina.api.story.model.Story;
import com.christina.app.story.manager.content.StoryDaoManager;
import com.christina.common.AsyncCallback;
import com.christina.common.AsyncResult;
import com.christina.common.contract.Contracts;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
public final class LoadStoryAsyncTask extends BaseStoryAsyncTask<Story, Exception, Void> {
    public LoadStoryAsyncTask(
        @NonNull final StoryDaoManager storyDaoManager,
        final long storyId,
        @Nullable final AsyncCallback<Story, Exception> callback) {
        super(storyDaoManager, callback);
        Contracts.requireNonNull(storyDaoManager, "storyDaoManager == null");

        _storyId = storyId;
    }

    @NonNull
    @Override
    protected AsyncResult<Story, Exception> doInBackground() {
        try {
            return AsyncResult.success(getStoryDaoManager().getStoryDao().get(getStoryId()));
        } catch (final Exception e) {
            return AsyncResult.error(e);
        }
    }

    @Getter(AccessLevel.PROTECTED)
    private final long _storyId;
}
