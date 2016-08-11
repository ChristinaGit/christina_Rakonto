package com.christina.content.story.dao;

import android.content.ContentResolver;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.christina.common.contract.Contracts;

public final class DaoManager {
    @Nullable
    private static ContentResolver _contentResolver;

    public static void initialize(@NonNull Context context) {
        Contracts.requireNonNull(context, "context == null");

        if (_contentResolver != null) {
            throw new IllegalStateException("DaoManager already initialized.");
        }

        _contentResolver = context.getContentResolver();
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
            throw new IllegalStateException("DaoManager not initialized.");
        }

        return _contentResolver;
    }

    private DaoManager() {
    }

    private static final class _StoryDaoInstanceHolder {
        public static final StoryDao INSTANCE = new StoryDao(_getContentResolver());
    }

    private static final class _StoryFrameDaoInstanceHolder {
        public static final StoryFrameDao INSTANCE = new StoryFrameDao(_getContentResolver());
    }
}
