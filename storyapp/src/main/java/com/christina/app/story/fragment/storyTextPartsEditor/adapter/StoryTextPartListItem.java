package com.christina.app.story.fragment.storyTextPartsEditor.adapter;

import android.support.annotation.NonNull;

import com.christina.common.contract.Contracts;

public final class StoryTextPartListItem {
    public StoryTextPartListItem(@NonNull final String rawStoryFrameText) {
        Contracts.requireNonNull(rawStoryFrameText, "rawStoryFrameText == null");

        _rawStoryFrameText = rawStoryFrameText;
    }

    @NonNull
    public final String getRawStoryFrameText() {
        return _rawStoryFrameText;
    }

    @NonNull
    private final String _rawStoryFrameText;
}
