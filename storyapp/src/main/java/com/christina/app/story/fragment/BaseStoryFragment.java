package com.christina.app.story.fragment;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.christina.api.story.observer.StoryContentObserver;
import com.christina.app.story.core.StoryContentObserverProvider;

public abstract class BaseStoryFragment extends Fragment {
    @Nullable
    public final StoryContentObserver getStoryContentObserver() {
        final StoryContentObserver observer;

        if (_storyContentObserver != null) {
            observer = _storyContentObserver;
        } else {
            final Activity activity = getActivity();
            if (activity instanceof StoryContentObserverProvider) {
                observer = ((StoryContentObserverProvider) activity).getStoryContentObserver();
            } else {
                observer = null;
            }
        }

        return observer;
    }

    public final void setStoryContentObserver(
        @Nullable final StoryContentObserver storyContentObserver) {
        _storyContentObserver = storyContentObserver;
    }

    @Nullable
    private StoryContentObserver _storyContentObserver;
}
