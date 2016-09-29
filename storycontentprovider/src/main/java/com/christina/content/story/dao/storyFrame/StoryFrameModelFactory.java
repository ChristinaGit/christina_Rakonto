package com.christina.content.story.dao.storyFrame;

import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.christina.common.contract.Contracts;
import com.christina.common.data.model.factory.ContentModelFactory;
import com.christina.content.story.model.StoryFrame;

public final class StoryFrameModelFactory implements ContentModelFactory<StoryFrame> {
    @NonNull
    @Override
    public final StoryFrame create(@NonNull final Cursor cursor) {
        Contracts.requireNonNull(cursor, "cursor == null");

        final StoryFrame model = create();

        model.setId(StoryFrameFullProjection.getId(cursor));
        model.setStoryId(StoryFrameFullProjection.getStoryId(cursor));
        model.setTextPosition(StoryFrameFullProjection.getTextPosition(cursor));
        final String imageUri = StoryFrameFullProjection.getImage(cursor);
        if (imageUri != null) {
            model.setImageUri(Uri.parse(imageUri));
        } else {
            model.setImageUri(null);
        }

        return model;
    }

    @NonNull
    @Override
    public final StoryFrame create() {
        return new StoryFrame();
    }
}
