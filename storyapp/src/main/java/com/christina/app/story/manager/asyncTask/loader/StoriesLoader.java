package com.christina.app.story.manager.asyncTask.loader;

import android.content.Context;
import android.support.annotation.NonNull;

import com.christina.api.story.model.Story;
import com.christina.app.story.manager.content.StoryDaoManager;
import com.christina.common.AsyncResult;
import com.christina.common.ResourceUtils;
import com.christina.common.contract.Contracts;
import com.christina.common.data.cursor.dataCursor.DataCursor;

import lombok.experimental.Accessors;

@Accessors(prefix = "_")
public final class StoriesLoader extends BaseStoryLoader<DataCursor<Story>, Exception> {
    public StoriesLoader(
        @NonNull final Context context, @NonNull final StoryDaoManager storyDaoManager) {
        super(
            Contracts.requireNonNull(context, "context == null"),
            Contracts.requireNonNull(storyDaoManager, "storyDaoManager == null"));
    }

    @Override
    public final AsyncResult<DataCursor<Story>, Exception> loadInBackground() {
        DataCursor<Story> result = null;
        try {
            result = getStoryDaoManager().getStoryDao().getAll().asDataCursor();
            return AsyncResult.success(result);
        } catch (final Exception e) {
            ResourceUtils.quietClose(result);
            return AsyncResult.error(e);
        }
    }
}
