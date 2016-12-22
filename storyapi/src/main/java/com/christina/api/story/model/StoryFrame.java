package com.christina.api.story.model;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.christina.common.contract.Contracts;
import com.christina.common.data.model.Model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
public final class StoryFrame extends Model {
    public StoryFrame() {
    }

    public StoryFrame(@NonNull final StoryFrame storyFrame) {
        super(Contracts.requireNonNull(storyFrame, "storyFrame == null"));

        _imageUri = storyFrame._imageUri;
        _storyId = storyFrame._storyId;
        _textEndPosition = storyFrame._textEndPosition;
        _textStartPosition = storyFrame._textStartPosition;
    }

    @Getter
    @Setter
    @Nullable
    private Uri _imageUri;

    @Getter
    @Setter
    private long _storyId;

    @Getter
    @Setter
    private int _textEndPosition;

    @Getter
    @Setter
    private int _textStartPosition;
}
