package com.christina.app.story.core.manager.search;

import android.support.annotation.NonNull;

import com.christina.common.AsyncCallback;

import java.util.List;

public interface StorySearchManager {
    void search(@NonNull String query, @NonNull AsyncCallback<List<String>, Exception> callback);
}
