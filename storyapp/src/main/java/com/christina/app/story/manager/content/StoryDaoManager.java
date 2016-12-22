package com.christina.app.story.manager.content;

import android.support.annotation.NonNull;

import com.christina.api.story.model.Story;
import com.christina.api.story.model.StoryFrame;
import com.christina.common.contract.Contracts;
import com.christina.common.data.dao.SqlDao;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
public final class StoryDaoManager {
    public StoryDaoManager(
        @NonNull final SqlDao<Story> storyDao, @NonNull final SqlDao<StoryFrame> storyFrameDao) {
        Contracts.requireNonNull(storyDao, "storyDao == null");
        Contracts.requireNonNull(storyFrameDao, "storyFrameDao == null");

        _storyDao = storyDao;
        _storyFrameDao = storyFrameDao;
    }

    @Getter
    @NonNull
    private final SqlDao<Story> _storyDao;

    @Getter
    @NonNull
    private final SqlDao<StoryFrame> _storyFrameDao;
}
