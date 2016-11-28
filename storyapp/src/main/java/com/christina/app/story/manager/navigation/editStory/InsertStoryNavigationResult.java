package com.christina.app.story.manager.navigation.editStory;

import android.support.annotation.NonNull;

import com.christina.app.story.operation.editStory.EditStoryActivity;

import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;

@Accessors(prefix = "_")
public enum InsertStoryNavigationResult {
    RESULT_INSERT_STORY_FAILED(EditStoryActivity.RESULT_INSERT_STORY_FAILED),
    RESULT_INSERT_STORY_FRAMES_FAILED(EditStoryActivity.RESULT_INSERT_STORY_FRAMES_FAILED),
    RESULT_NOT_FOUND(EditStoryActivity.RESULT_NOT_FOUND),
    RESULT_NO_DATA(EditStoryActivity.RESULT_NO_DATA),
    RESULT_UNSUPPORTED_ACTION(EditStoryActivity.RESULT_UNSUPPORTED_ACTION),
    RESULT_OK(EditStoryActivity.RESULT_OK),
    RESULT_CANCELED(EditStoryActivity.RESULT_CANCELED);

    @NonNull
    public static InsertStoryNavigationResult getByResultCode(final int resultCode) {
        for (final val result : values()) {
            if (result.getResultCode() == resultCode) {
                return result;
            }
        }

        throw new IllegalArgumentException("Unknown result code: " + resultCode);
    }

    @Getter
    private final int _resultCode;

    InsertStoryNavigationResult(final int resultCode) {
        _resultCode = resultCode;
    }
}
