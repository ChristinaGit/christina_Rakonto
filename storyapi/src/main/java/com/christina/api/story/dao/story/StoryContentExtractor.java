package com.christina.api.story.dao.story;

import android.content.ContentValues;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.christina.api.story.database.StoryTable;
import com.christina.api.story.model.Story;
import com.christina.common.contract.Contracts;
import com.christina.common.data.model.factory.ModelContentExtractor;

public final class StoryContentExtractor implements ModelContentExtractor<Story> {
    @NonNull
    @Override
    public final ContentValues extract(@NonNull final Story model) {
        Contracts.requireNonNull(model, "model == null");

        final ContentValues values = new ContentValues(StoryFullProjection.COLUMN_COUNT);

        values.put(StoryTable.Story.COLUMN_NAME, model.getName());
        values.put(StoryTable.Story.COLUMN_CREATE_DATE, model.getCreateDate());
        values.put(StoryTable.Story.COLUMN_MODIFY_DATE, model.getModifyDate());
        values.put(StoryTable.Story.COLUMN_TEXT, model.getText());

        final Uri previewUri = model.getPreviewUri();
        if (previewUri == null) {
            values.putNull(StoryTable.Story.COLUMN_PREVIEW);
        } else {
            values.put(StoryTable.Story.COLUMN_PREVIEW, previewUri.toString());
        }

        return values;
    }
}
