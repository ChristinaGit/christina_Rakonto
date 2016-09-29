package com.christina.content.story.dao.story;

import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.christina.common.contract.Contracts;
import com.christina.common.data.model.factory.ContentModelFactory;
import com.christina.content.story.model.Story;

public final class StoryModelFactory implements ContentModelFactory<Story> {
    @NonNull
    @Override
    public final Story create(@NonNull final Cursor cursor) {
        Contracts.requireNonNull(cursor, "cursor == null");

        final Story model = create();

        model.setId(StoryFullProjection.getId(cursor));
        model.setName(StoryFullProjection.getName(cursor));
        model.setCreateDate(StoryFullProjection.getCreateDate(cursor));
        model.setModifyDate(StoryFullProjection.getModifyDate(cursor));
        model.setText(StoryFullProjection.getText(cursor));
        final String preview = StoryFullProjection.getPreview(cursor);
        if (preview != null) {
            model.setPreviewUri(Uri.parse(preview));
        } else {
            model.setPreviewUri(null);
        }

        return model;
    }

    @NonNull
    @Override
    public final Story create() {
        return new Story();
    }
}
