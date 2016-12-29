package com.christina.app.story.di.qualifier;

import com.christina.common.contract.Contracts;

public final class StoryContentExtractorNames {
    private static final String NAME_PREFIX = "story_content_extractor:";

    public static final String STORY = NAME_PREFIX + "story";

    public static final String STORY_FRAME = NAME_PREFIX + "story_frame";

    private StoryContentExtractorNames() {
        Contracts.unreachable();
    }
}
