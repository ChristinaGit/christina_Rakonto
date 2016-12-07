package com.christina.app.story.manager.navigation;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.christina.app.story.manager.navigation.editStory.InsertStoryNavigationCallback;
import com.christina.app.story.view.activity.storyEditor.StoryEditorActivity;
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
    public void navigateToEditStory(final long storyId) {
        StoryEditorActivity.startEdit(getActivity(), storyId);
    }

    @Override
    public void navigateToInsertStory(
        @Nullable final InsertStoryNavigationCallback navigationCallback) {
        if (navigationCallback != null) {
            getCallbacks().append(REQUEST_CODE_INSERT_STORY, navigationCallback);
        }

        StoryEditorActivity.startInsertForResult(getActivity(), REQUEST_CODE_INSERT_STORY);
    }

    @Override
    public void onActivityResultIntoActivity(
        final int requestCode, final int resultCode, @Nullable final Intent data) {
        final val callbacks = getCallbacks();
        final val callback = callbacks.get(requestCode);

        if (REQUEST_CODE_INSERT_STORY == requestCode) {
            if (callback instanceof InsertStoryNavigationCallback) {
                final val navigationCallback = (InsertStoryNavigationCallback) callback;

                final val result = getNavigationResult(requestCode, resultCode);
                navigationCallback.onInsertStoryNavigationResult(result);
            }

            callbacks.remove(REQUEST_CODE_INSERT_STORY);
        }
    }

    @Getter(value = AccessLevel.PRIVATE)
    @NonNull
    private final Activity _activity;

    @Getter(value = AccessLevel.PRIVATE, lazy = true)
    @NonNull
    private final SparseArray<Object> _callbacks = new SparseArray<>();

    @NonNull
    private NavigationResult getNavigationResult(
        final int requestCode, final int resultCode) {
        final NavigationResult navigationResult;

        if (requestCode == REQUEST_CODE_INSERT_STORY) {
            if (resultCode == StoryEditorActivity.RESULT_OK) {
                navigationResult = NavigationResult.SUCCESS;
            } else if (resultCode == StoryEditorActivity.RESULT_CANCELED) {
                navigationResult = NavigationResult.CANCELED;
            } else if (resultCode == StoryEditorActivity.RESULT_INSERT_STORY_FAILED ||
                       resultCode == StoryEditorActivity.RESULT_INSERT_STORY_FRAMES_FAILED ||
                       resultCode == StoryEditorActivity.RESULT_NOT_FOUND ||
                       resultCode == StoryEditorActivity.RESULT_NO_DATA ||
                       resultCode == StoryEditorActivity.RESULT_UNSUPPORTED_ACTION) {
                navigationResult = NavigationResult.FAILED;
            } else {
                navigationResult = NavigationResult.UNKNOWN;
            }
        } else {
            navigationResult = NavigationResult.UNKNOWN;
        }

        return navigationResult;
    }
}
