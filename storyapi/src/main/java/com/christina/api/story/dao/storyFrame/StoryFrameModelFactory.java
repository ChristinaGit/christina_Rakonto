package com.christina.api.story.dao.storyFrame;

import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.christina.api.story.model.StoryFrame;
import com.christina.common.contract.Contracts;
import com.christina.common.data.dao.factory.ModelFactory;

import lombok.val;

public final class StoryFrameModelFactory implements ModelFactory<StoryFrame> {
    public StoryFrameModelFactory(@NonNull final StoryFrameFullProjection fullProjection) {
        Contracts.requireNonNull(fullProjection, "fullProjection == null");

        _fullProjection = fullProjection;
    }

    @NonNull
    @Override
    public final StoryFrame create(@NonNull final Cursor argument) {
        Contracts.requireNonNull(argument, "argument == null");

        final val model = create();

        model.setId(_fullProjection.getId(argument));
        model.setStoryId(_fullProjection.getStoryId(argument));
        model.setTextStartPosition(_fullProjection.getTextStartPosition(argument));
        model.setTextEndPosition(_fullProjection.getTextEndPosition(argument));
        final String imageUri = _fullProjection.getImage(argument);
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

    @NonNull
    private final StoryFrameFullProjection _fullProjection;
}
