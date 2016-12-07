package com.christina.api.story.dao.story;

import android.content.ContentValues;
import android.support.annotation.NonNull;

import com.christina.api.story.database.StoryTable;
import com.christina.api.story.model.Story;
import com.christina.common.contract.Contracts;
import com.christina.common.data.dao.factory.ModelContentExtractor;

import lombok.val;

public final class StoryContentExtractor implements ModelContentExtractor<Story> {
    @NonNull
    @Override
    public final ContentValues extract(@NonNull final Story model) {
        Contracts.requireNonNull(model, "model == null");

        final val values = new ContentValues();

        values.put(StoryTable.COLUMN_NAME, model.getName());
        values.put(StoryTable.COLUMN_CREATE_DATE, model.getCreateDate());
        values.put(StoryTable.COLUMN_MODIFY_DATE, model.getModifyDate());
        values.put(StoryTable.COLUMN_TEXT, model.getText());

        final val previewUri = model.getPreviewUri();
        if (previewUri == null) {
            values.putNull(StoryTable.COLUMN_PREVIEW);
        } else {
            values.put(StoryTable.COLUMN_PREVIEW, previewUri.toString());
        }

        return values;
    }
}
