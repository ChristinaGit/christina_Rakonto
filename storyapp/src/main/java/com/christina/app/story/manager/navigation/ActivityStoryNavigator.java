package com.christina.app.story.manager.navigation;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.christina.app.story.manager.navigation.editStory.InsertStoryNavigationCallback;
import com.christina.app.story.manager.navigation.editStory.InsertStoryNavigationResult;
import com.christina.app.story.operation.editStory.EditStoryActivity;
import com.christina.common.view.activity.ActivityResultListener;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;

@Accessors(prefix = "_")
public final class ActivityStoryNavigator implements StoryNavigator, ActivityResultListener {
    protected static int requestCodeIndexer = 0;

    protected static final int REQUEST_CODE_INSERT_STORY = requestCodeIndexer++;

    public ActivityStoryNavigator(@NonNull final Activity activity) {
        _activity = activity;
    }

    @Override
    public void navigateToInsertStory(
        @Nullable final InsertStoryNavigationCallback navigationCallback) {
        if (navigationCallback != null) {
            getCallbacks().append(REQUEST_CODE_INSERT_STORY, navigationCallback);
        }

        EditStoryActivity.startInsertForResult(getActivity(), REQUEST_CODE_INSERT_STORY);
    }

    @Override
    public void onActivityResultIntoActivity(
        final int requestCode, final int resultCode, @Nullable final Intent data) {
        final SparseArray<Object> callbacks = getCallbacks();
        final Object callback = callbacks.get(requestCode);

        if (REQUEST_CODE_INSERT_STORY == requestCode) {
            if (callback instanceof InsertStoryNavigationCallback) {
                final val navigationCallback = (InsertStoryNavigationCallback) callback;

                final val result = InsertStoryNavigationResult.getByResultCode(resultCode);
                navigationCallback.onInsertStoryNavigationResult(result);
            }
        }

        callbacks.remove(REQUEST_CODE_INSERT_STORY);
    }

    @Getter(value = AccessLevel.PRIVATE)
    @NonNull
    private final Activity _activity;

    @Getter(value = AccessLevel.PRIVATE, lazy = true)
    @NonNull
    private final SparseArray<Object> _callbacks = new SparseArray<>();
}
