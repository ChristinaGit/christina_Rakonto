package com.christina.app.story.adpter.storyEditorPages;

import android.support.v4.app.Fragment;

import com.christina.common.pattern.factory.TransitionFactory;

public interface StoryEditorPageFactory extends TransitionFactory<Fragment, Integer> {
    int getPageCount();
}
