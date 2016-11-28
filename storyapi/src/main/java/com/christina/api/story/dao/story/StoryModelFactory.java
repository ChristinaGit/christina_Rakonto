package com.christina.api.story.dao.story;

import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.christina.api.story.model.Story;
import com.christina.common.contract.Contracts;
import com.christina.common.data.dao.factory.ModelFactory;

import lombok.val;

public final class StoryModelFactory implements ModelFactory<Story> {
    public StoryModelFactory(@NonNull final StoryFullProjection fullProjection) {
        Contracts.requireNonNull(fullProjection, "fullProjection == null");

        _fullProjection = fullProjection;
    }

    @NonNull
    @Override
    public final Story create(@NonNull final Cursor argument) {
        Contracts.requireNonNull(argument, "argument == null");

        final val model = create();

        model.setId(_fullProjection.getId(argument));
        model.setName(_fullProjection.getName(argument));
        model.setCreateDate(_fullProjection.getCreateDate(argument));
        model.setModifyDate(_fullProjection.getModifyDate(argument));
        model.setText(_fullProjection.getText(argument));
        final String preview = _fullProjection.getPreview(argument);
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

    @NonNull
    private final StoryFullProjection _fullProjection;
}
