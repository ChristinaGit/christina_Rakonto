package com.christina.app.story.adpter.storyFrames;

import android.support.annotation.NonNull;

import com.christina.api.story.model.StoryFrame;
import com.christina.common.contract.Contracts;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
public final class StoryFrameListItem {
    public StoryFrameListItem(@NonNull final StoryFrame storyFrame) {
        Contracts.requireNonNull(storyFrame, "storyFrame == null");

        _storyFrame = storyFrame;
    }

    @Getter
    @NonNull
    private final StoryFrame _storyFrame;
}
