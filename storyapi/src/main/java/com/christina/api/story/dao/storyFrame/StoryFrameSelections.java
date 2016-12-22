package com.christina.api.story.dao.storyFrame;

import android.support.annotation.NonNull;

import com.christina.api.story.database.StoryFrameTable;
import com.christina.common.contract.Contracts;
import com.christina.common.data.dao.selection.SqlDataSelection;
import com.christina.common.data.dao.selection.SqlDataSelectionBase;

import lombok.experimental.Accessors;

@Accessors(prefix = "_")
public final class StoryFrameSelections {
    @NonNull
    public static SqlDataSelection byStoryId(final long storyId) {
        return SqlDataSelectionBase
            .builder()
            .where(StoryFrameTable.COLUMN_STORY_ID + "=?")
            .with(String.valueOf(storyId))
            .build();
    }

    private StoryFrameSelections() {
        Contracts.unreachable();
    }
}
