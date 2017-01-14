package com.christina.app.story.core.manager.navigation;

import android.support.annotation.Nullable;

public interface StoryNavigator {
    void navigateToEditStory(long storyId);

    void navigateToInsertStory(@Nullable final NavigationCallback navigationCallback);
}
