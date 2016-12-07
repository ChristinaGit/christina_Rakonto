package com.christina.app.story.manager.asyncTask;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.christina.api.story.model.Story;
import com.christina.common.AsyncCallback;
import com.christina.common.data.cursor.dataCursor.DataCursor;

public interface StoryTaskManager {
    void insertStory(@NonNull AsyncCallback<Story, Exception> callback);

    void loadStories(@NonNull AsyncCallback<DataCursor<Story>, Exception> callback);

    void loadStory(long storyId, @NonNull AsyncCallback<Story, Exception> callback);

    void updateStory(@NonNull Story story, @Nullable AsyncCallback<Integer, Exception> callback);

    void updateStory(@NonNull Story story);
}
