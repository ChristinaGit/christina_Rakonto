package com.christina.app.story.manager.content;

import android.content.ContentResolver;
import android.support.annotation.NonNull;

import com.christina.api.story.contract.StoryContentContract;
import com.christina.api.story.observer.StoryContentObserver;
import com.christina.common.contract.Contracts;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
public final class StoryContentObserverManager {
    public StoryContentObserverManager(
        @NonNull final ContentResolver contentResolver,
        @NonNull final StoryContentObserver storyContentObserver) {
        Contracts.requireNonNull(contentResolver, "contentResolver == null");
        Contracts.requireNonNull(storyContentObserver, "storyContentObserver == null");

        _contentResolver = contentResolver;
        _storyContentObserver = storyContentObserver;
    }

    public void registerStoryContentObserver() {
        getContentResolver().registerContentObserver(StoryContentContract.CONTENT_URI,
                                                     true,
                                                     getStoryContentObserver());
    }

    public void unregisterStoryContentObserver() {
        getContentResolver().unregisterContentObserver(getStoryContentObserver());
    }

    @Getter(AccessLevel.PROTECTED)
    @NonNull
    private final ContentResolver _contentResolver;

    @Getter(AccessLevel.PROTECTED)
    @NonNull
    private final StoryContentObserver _storyContentObserver;
}
