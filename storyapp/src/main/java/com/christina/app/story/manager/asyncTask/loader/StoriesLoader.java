package com.christina.app.story.manager.asyncTask.loader;

import android.content.Context;
import android.support.annotation.NonNull;

import com.christina.api.story.model.Story;
import com.christina.app.story.manager.content.StoryDaoManager;
import com.christina.common.AsyncResult;
import com.christina.common.contract.Contracts;
import com.christina.common.data.cursor.dataCursor.DataCursor;

import lombok.experimental.Accessors;

@Accessors(prefix = "_")
public final class StoriesLoader extends BaseStoryLoader<DataCursor<Story>, Exception> {
    public StoriesLoader(
        @NonNull final Context context, @NonNull final StoryDaoManager storyDaoManager) {
        super(context, storyDaoManager);
        Contracts.requireNonNull(context, "context == null");
        Contracts.requireNonNull(storyDaoManager, "storyDaoManager == null");
    }

    @Override
    public final AsyncResult<DataCursor<Story>, Exception> loadInBackground() {
        try {
            return AsyncResult.success(getStoryDaoManager().getStoryDao().getAll().asDataCursor());
        } catch (final Exception e) {
            return AsyncResult.error(e);
        }
    }
}
