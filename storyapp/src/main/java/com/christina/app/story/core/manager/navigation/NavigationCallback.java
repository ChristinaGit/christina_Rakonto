package com.christina.app.story.core.manager.navigation;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface NavigationCallback<TResult, TData> {
    void onNavigationResult(@NonNull TResult result, @Nullable TData data);
}
