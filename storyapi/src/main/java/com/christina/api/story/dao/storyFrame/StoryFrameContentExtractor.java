package com.christina.api.story.dao.storyFrame;

import android.content.ContentValues;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.christina.api.story.database.StoryFrameTable;
import com.christina.api.story.model.StoryFrame;
import com.christina.common.contract.Contracts;
import com.christina.common.data.dao.factory.ModelContentExtractor;

import lombok.val;

public final class StoryFrameContentExtractor implements ModelContentExtractor<StoryFrame> {
    @NonNull
    @Override
    public final ContentValues extract(@NonNull final StoryFrame model) {
        Contracts.requireNonNull(model, "model == null");

        final val values = new ContentValues();

        values.put(StoryFrameTable.COLUMN_STORY_ID, model.getStoryId());
        values.put(StoryFrameTable.COLUMN_TEXT_START_POSITION, model.getTextStartPosition());
        values.put(StoryFrameTable.COLUMN_TEXT_END_POSITION, model.getTextEndPosition());
        final Uri imageUri = model.getImageUri();
        if (imageUri == null) {
            values.putNull(StoryFrameTable.COLUMN_IMAGE);
        } else {
            values.put(StoryFrameTable.COLUMN_IMAGE, imageUri.toString());
        }

        return values;
    }
}
