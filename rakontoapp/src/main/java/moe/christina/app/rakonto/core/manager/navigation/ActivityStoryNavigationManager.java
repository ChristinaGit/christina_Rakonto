package moe.christina.app.rakonto.core.manager.navigation;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import lombok.experimental.Accessors;
import lombok.val;

import moe.christina.app.rakonto.screen.activity.storyEditor.StoryEditorActivity;
import moe.christina.common.contract.Contracts;
import moe.christina.common.control.adviser.ResourceAdviser;
import moe.christina.common.control.manager.navigation.ActivityNavigationManager;
import moe.christina.common.control.manager.navigation.NavigationCallback;
import moe.christina.common.control.manager.navigation.NavigationResult;
import moe.christina.common.extension.activity.ObservableActivity;

@Accessors(prefix = "_")
public final class ActivityStoryNavigationManager extends ActivityNavigationManager
    implements StoryNavigationManager {
    protected static final int REQUEST_CODE_INSERT_STORY = 0;

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
