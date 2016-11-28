package com.christina.app.story.fragment.storyFramesEditor.adapter;

import android.support.annotation.NonNull;

import com.christina.api.story.model.StoryFrame;
import com.christina.common.contract.Contracts;

public final class StoryFrameListItem {
    public StoryFrameListItem(@NonNull final StoryFrame storyFrame) {
        Contracts.requireNonNull(storyFrame, "storyFrame == null");

        _storyFrame = storyFrame;
    }

    @NonNull
    public final StoryFrame getStoryFrame() {
        return _storyFrame;
    }

    @NonNull
    private final StoryFrame _storyFrame;
}
