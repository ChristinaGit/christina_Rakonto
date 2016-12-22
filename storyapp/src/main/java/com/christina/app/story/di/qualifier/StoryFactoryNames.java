package com.christina.app.story.di.qualifier;

import com.christina.common.contract.Contracts;

public final class StoryFactoryNames {
    public static final String STORY = "story";

    public static final String STORY_FRAME = "story_frame";

    private StoryFactoryNames() {
        Contracts.unreachable();
    }
}
