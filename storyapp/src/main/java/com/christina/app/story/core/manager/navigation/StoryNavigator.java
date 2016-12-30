package com.christina.app.story.core.manager.navigation;

import android.support.annotation.Nullable;

import com.christina.app.story.core.manager.navigation.editStory.InsertStoryNavigationCallback;

public interface StoryNavigator {
    void navigateToEditStory(long storyId);

    void navigateToInsertStory(@Nullable final InsertStoryNavigationCallback navigationCallback);
}
