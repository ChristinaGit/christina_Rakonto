package com.christina.api.story.dao;

import android.content.ContentResolver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.christina.common.contract.Contracts;
import com.christina.api.story.dao.story.StoryDao;
import com.christina.api.story.dao.storyFrame.StoryFrameDao;

public final class StoryDaoManager {
    @Nullable
    private static ContentResolver _contentResolver;

    public static void initialize(@NonNull final ContentResolver contentResolver) {
        Contracts.requireNonNull(contentResolver, "contentResolver == null");

        if (_contentResolver != null) {
            throw new IllegalStateException("StoryDaoManager is already initialized.");
        }

        _contentResolver = contentResolver;
    }

    @NonNull
    public static StoryDao getStoryDao() {
        return _StoryDaoInstanceHolder.INSTANCE;
    }

    @NonNull
    public static StoryFrameDao getStoryFrameDao() {
        return _StoryFrameDaoInstanceHolder.INSTANCE;
    }

    @NonNull
    private static ContentResolver _getContentResolver() {
        if (_contentResolver == null) {
            throw new IllegalStateException("StoryDaoManager not initialized.");
        }

        return _contentResolver;
    }

    private StoryDaoManager() {
    }

    private static final class _StoryDaoInstanceHolder {
        public static final StoryDao INSTANCE = new StoryDao(_getContentResolver());
    }

    private static final class _StoryFrameDaoInstanceHolder {
        public static final StoryFrameDao INSTANCE = new StoryFrameDao(_getContentResolver());
    }
}
