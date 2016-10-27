package com.christina.api.story.dao.storyFrame;

import android.content.ContentValues;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.christina.api.story.database.StoryTable;
import com.christina.api.story.model.StoryFrame;
import com.christina.common.contract.Contracts;
import com.christina.common.data.model.factory.ModelContentExtractor;

public final class StoryFrameContentExtractor implements ModelContentExtractor<StoryFrame> {
    @NonNull
    @Override
    public final ContentValues extract(@NonNull final StoryFrame model) {
        Contracts.requireNonNull(model, "model == null");

        final ContentValues values = new ContentValues(StoryFrameFullProjection.COLUMN_COUNT);

        values.put(StoryTable.StoryFrame.COLUMN_STORY_ID, model.getStoryId());
        values.put(StoryTable.StoryFrame.COLUMN_TEXT_POSITION, model.getTextPosition());
        final Uri imageUri = model.getImageUri();
        if (imageUri == null) {
            values.putNull(StoryTable.StoryFrame.COLUMN_IMAGE);
        } else {
            values.put(StoryTable.StoryFrame.COLUMN_IMAGE, imageUri.toString());
        }

        return values;
    }
}
