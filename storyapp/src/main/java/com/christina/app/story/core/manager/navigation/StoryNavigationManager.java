package com.christina.app.story.core.manager.navigation;

import android.support.annotation.Nullable;

import com.christina.common.control.manager.navigation.NavigationCallback;

public interface StoryNavigationManager {
    void navigateToEditStory(long storyId);

    void navigateToInsertStory(@Nullable final NavigationCallback navigationCallback);
}
