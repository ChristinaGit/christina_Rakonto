package com.christina.app.story.manager.navigation;

import android.support.annotation.Nullable;

import com.christina.app.story.manager.navigation.editStory.InsertStoryNavigationCallback;

public interface StoryNavigator {
    void navigateToInsertStory(@Nullable final InsertStoryNavigationCallback navigationCallback);
}
