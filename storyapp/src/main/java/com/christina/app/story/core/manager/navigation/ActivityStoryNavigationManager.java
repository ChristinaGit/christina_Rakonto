package com.christina.app.story.core.manager.navigation;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;

import com.christina.app.story.core.manager.common.ReleasableManager;
import com.christina.app.story.view.activity.storyEditor.StoryEditorActivity;
import com.christina.common.adviser.ResourceAdviser;
import com.christina.common.contract.Contracts;
import com.christina.common.event.generic.EventHandler;
import com.christina.common.view.observerable.ObservableActivity;
import com.christina.common.view.observerable.eventArgs.ActivityResultEventArgs;

@Accessors(prefix = "_")
public final class ActivityStoryNavigationManager extends ReleasableManager
    implements StoryNavigationManager {
    protected static int requestCodeIndexer = 0;

    protected static final int REQUEST_CODE_INSERT_STORY = requestCodeIndexer++;

    public ActivityStoryNavigationManager(
        @NonNull final ResourceAdviser resourceAdviser,
        @NonNull final ObservableActivity observableActivity) {
        super(Contracts.requireNonNull(resourceAdviser, "resourceAdviser == null"));
        Contracts.requireNonNull(observableActivity, "observableActivity == null");

        _observableActivity = observableActivity;
    }

    @Override
    public void navigateToEditStory(final long storyId) {
        StoryEditorActivity.startEdit(getActivity(), storyId);
    }

    @Override
    public void navigateToInsertStory(
        @Nullable final NavigationCallback navigationCallback) {
        if (navigationCallback != null) {
            getCallbacks().append(REQUEST_CODE_INSERT_STORY, navigationCallback);
        }

        StoryEditorActivity.startInsertForResult(getActivity(), REQUEST_CODE_INSERT_STORY);
    }

    @NonNull
    protected final AppCompatActivity getActivity() {
        return getObservableActivity().asActivity();
    }

    @Override
    protected void onAcquireResources() {
        getObservableActivity().getActivityResultEvent().addHandler(_activityReulstHandler);
    }

    @Override
    protected void onReleaseResources() {
        getObservableActivity().getActivityResultEvent().removeHandler(_activityReulstHandler);
    }

    @NonNull
    private final EventHandler<ActivityResultEventArgs> _activityReulstHandler =
        new EventHandler<ActivityResultEventArgs>() {
            @Override
            public void onEvent(@NonNull final ActivityResultEventArgs eventArgs) {
                final int requestCode = eventArgs.getRequestCode();
                final int resultCode = eventArgs.getResultCode();

                final val callbacks = getCallbacks();
                final val callback = callbacks.get(requestCode);

                if (REQUEST_CODE_INSERT_STORY == requestCode) {
                    if (callback instanceof NavigationCallback) {
                        final val navigationCallback = (NavigationCallback) callback;

                        final val result = getNavigationResult(requestCode, resultCode);
                        navigationCallback.onNavigationResult(result);
                    }

                    callbacks.remove(REQUEST_CODE_INSERT_STORY);
                }
            }
        };

    @Getter(value = AccessLevel.PRIVATE)
    @NonNull
    private final SparseArray<Object> _callbacks = new SparseArray<>();

    @Getter(value = AccessLevel.PROTECTED)
    @NonNull
    private final ObservableActivity _observableActivity;

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
