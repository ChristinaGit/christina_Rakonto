package com.christina.app.story.fragment.storyTextPartsEditor.loader;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;

import com.christina.api.story.dao.StoryDaoManager;
import com.christina.api.story.model.Story;
import com.christina.api.story.model.StoryFrame;

import java.util.List;

public final class StoryLoader extends AsyncTaskLoader<StoryLoaderResult> {
    public StoryLoader(@NonNull final Context context, final long storyId) {
        super(context);

        _storyId = storyId;
    }

    @Override
    public StoryLoaderResult loadInBackground() {
        final Story story = StoryDaoManager.getStoryDao().get(_storyId);
        final List<StoryFrame> storyFrames =
            StoryDaoManager.getStoryFrameDao().getByStoryId(_storyId).asList();
        return new StoryLoaderResult(story, storyFrames);
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

    private final long _storyId;
}
