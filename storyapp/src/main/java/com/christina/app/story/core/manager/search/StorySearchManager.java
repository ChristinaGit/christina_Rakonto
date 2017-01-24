package com.christina.app.story.core.manager.search;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import java.util.List;

public interface StorySearchManager {
    @WorkerThread
    @Nullable
    List<String> search(@NonNull String query)
        throws Exception;
}
