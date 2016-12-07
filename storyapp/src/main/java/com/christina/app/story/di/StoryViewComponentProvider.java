package com.christina.app.story.di;

import android.support.annotation.NonNull;

import com.christina.app.story.di.storyView.StoryViewComponent;

public interface StoryViewComponentProvider {
    @NonNull
    StoryViewComponent getStoryViewComponent();
}
