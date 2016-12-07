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
public final class UpdateStoryAsyncTask extends BaseStoryAsyncTask<Integer, Exception, Void> {
    public UpdateStoryAsyncTask(
        @NonNull final StoryDaoManager storyDaoManager,
        @NonNull final Story story,
        @Nullable final AsyncCallback<Integer, Exception> callback) {
        super(storyDaoManager, callback);
        Contracts.requireNonNull(storyDaoManager, "storyDaoManager == null");
        Contracts.requireNonNull(story, "story == null");

        _story = story;
    }

    @NonNull
    @Override
    protected AsyncResult<Integer, Exception> doInBackground() {
        try {
            return AsyncResult.success(getStoryDaoManager().getStoryDao().update(getStory()));
        } catch (final Exception e) {
            return AsyncResult.error(e);
        }
    }

    @Getter(AccessLevel.PROTECTED)
    @NonNull
    private final Story _story;
}
