package com.christina.app.story.core.manager.content;

import android.support.annotation.NonNull;

import com.christina.api.story.dao.story.StoryDao;
import com.christina.api.story.dao.storyFrame.StoryFrameDao;
import com.christina.common.contract.Contracts;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
public final class StoryDaoManager {
    public StoryDaoManager(
        @NonNull final StoryDao storyDao, @NonNull final StoryFrameDao storyFrameDao) {
        Contracts.requireNonNull(storyDao, "storyDao == null");
        Contracts.requireNonNull(storyFrameDao, "storyFrameDao == null");

        _storyDao = storyDao;
        _storyFrameDao = storyFrameDao;
    }

    @Getter
    @NonNull
    private final StoryDao _storyDao;

    @Getter
    @NonNull
    private final StoryFrameDao _storyFrameDao;
}
