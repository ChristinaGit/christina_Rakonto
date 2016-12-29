package com.christina.app.story.di.qualifier;

import com.christina.common.contract.Contracts;

public final class StoryFactoryNames {
    private static final String NAME_PREFIX = "story_factory:";

    public static final String STORY = NAME_PREFIX + "story";

    public static final String STORY_FRAME = NAME_PREFIX + "story_frame";

    private StoryFactoryNames() {
        Contracts.unreachable();
    }
}
