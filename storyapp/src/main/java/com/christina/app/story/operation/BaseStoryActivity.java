package com.christina.app.story.operation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.christina.api.story.contract.StoryContentContract;
import com.christina.api.story.observer.StoryContentObserver;
import com.christina.app.story.core.StoryContentObserverProvider;

public class BaseStoryActivity extends AppCompatActivity implements StoryContentObserverProvider {
    @NonNull
    @Override
    public final StoryContentObserver getStoryContentObserver() {
        return _storyContentObserver;
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getContentResolver().registerContentObserver(StoryContentContract.CONTENT_URI, true,
            getStoryContentObserver());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        getContentResolver().unregisterContentObserver(getStoryContentObserver());
    }

    @NonNull
    private final StoryContentObserver _storyContentObserver = new StoryContentObserver();
}
