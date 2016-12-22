package com.christina.app.story.manager.asyncTask.task;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.christina.api.story.model.Story;
import com.christina.app.story.manager.content.StoryDaoManager;
import com.christina.common.AsyncCallback;
import com.christina.common.AsyncResult;
import com.christina.common.contract.Contracts;

import lombok.val;

public final class InsertNewStoryAsyncTask extends BaseStoryAsyncTask<Story, Exception, Void> {
    public InsertNewStoryAsyncTask(
        @NonNull final StoryDaoManager storyDaoManager,
        @Nullable final AsyncCallback<Story, Exception> callback) {
        super(Contracts.requireNonNull(storyDaoManager, "storyDaoManager == null"), callback);
    }

    @NonNull
    @Override
    protected AsyncResult<Story, Exception> doInBackground() {
        AsyncResult<Story, Exception> result;

        try {
            final val storyDao = getStoryDaoManager().getStoryDao();

            final val story = new Story();
            final long currentTime = System.currentTimeMillis();

            story.setCreateDate(currentTime);
            story.setModifyDate(currentTime);

            if (storyDao.insert(story) != Story.NO_ID) {
                result = AsyncResult.success(story);
            } else {
                result = AsyncResult.error();
            }
        } catch (final Exception e) {
            result = AsyncResult.error(e);
        }

        return result;
    }
}
