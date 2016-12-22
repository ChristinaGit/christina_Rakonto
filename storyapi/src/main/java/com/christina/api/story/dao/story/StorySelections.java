package com.christina.api.story.dao.story;

import com.christina.common.contract.Contracts;

import lombok.experimental.Accessors;

@Accessors(prefix = "_")
public final class StorySelections {
    private StorySelections() {
        Contracts.unreachable();
    }
}
