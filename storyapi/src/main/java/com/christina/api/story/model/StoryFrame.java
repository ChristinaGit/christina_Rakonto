package com.christina.api.story.model;

import android.net.Uri;
import android.support.annotation.Nullable;

import com.christina.common.data.model.Model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
public final class StoryFrame extends Model {
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
