package moe.christina.app.rakonto.core.manager.navigation;

import android.support.annotation.Nullable;

import moe.christina.common.control.manager.navigation.NavigationCallback;

public interface StoryNavigationManager {
    void navigateToEditStory(long storyId);

    void navigateToInsertStory(@Nullable final NavigationCallback navigationCallback);
}
