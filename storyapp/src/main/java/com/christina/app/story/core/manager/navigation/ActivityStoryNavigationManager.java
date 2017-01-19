package com.christina.app.story.core.manager.navigation;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import lombok.experimental.Accessors;
import lombok.val;

import com.christina.app.story.view.activity.storyEditor.StoryEditorActivity;
import com.christina.common.contract.Contracts;
import com.christina.common.control.adviser.ResourceAdviser;
import com.christina.common.control.manager.navigation.ActivityNavigationManager;
import com.christina.common.control.manager.navigation.NavigationCallback;
import com.christina.common.control.manager.navigation.NavigationResult;
import com.christina.common.extension.activity.ObservableActivity;

@Accessors(prefix = "_")
public final class ActivityStoryNavigationManager extends ActivityNavigationManager
    implements StoryNavigationManager {
    protected static int requestCodeIndexer = 0;

    protected static final int REQUEST_CODE_INSERT_STORY = requestCodeIndexer++;

    public ActivityStoryNavigationManager(
        @NonNull final ResourceAdviser resourceAdviser,
        @NonNull final ObservableActivity observableActivity) {
        super(
            Contracts.requireNonNull(resourceAdviser, "resourceAdviser == null"),
            Contracts.requireNonNull(observableActivity, "observableActivity == null"));

        setAutoReleaseCallbacks(true);
    }

    @Override
    public void navigateToEditStory(final long storyId) {
        StoryEditorActivity.startEdit(getActivity(), storyId);
    }

    @Override
    public void navigateToInsertStory(
        @Nullable final NavigationCallback navigationCallback) {
        if (navigationCallback != null) {
            registerNavigationCallback(REQUEST_CODE_INSERT_STORY, navigationCallback);
        }

        StoryEditorActivity.startInsertForResult(getActivity(), REQUEST_CODE_INSERT_STORY);
    }

    @Override
    protected final void onActivityResult(
        final int requestCode,
        final int resultCode,
        @Nullable final Intent data,
        @Nullable final NavigationCallback callback) {
        if (REQUEST_CODE_INSERT_STORY == requestCode) {
            if (callback != null) {
                final val result = getNavigationResult(requestCode, resultCode);
                callback.onNavigationResult(result);
            }
        }
    }

    @NonNull
    private NavigationResult getNavigationResult(final int requestCode, final int resultCode) {
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
