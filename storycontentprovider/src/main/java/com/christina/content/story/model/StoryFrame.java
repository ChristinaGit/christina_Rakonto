package com.christina.content.story.model;

import android.net.Uri;
import android.support.annotation.Nullable;

import com.christina.common.data.model.Model;

public final class StoryFrame extends Model {
    public StoryFrame() {
    }

    public final long getStoryId() {
        return _storyId;
    }

    public final void setStoryId(final long storyId) {
        _storyId = storyId;
    }

    public final int getTextPosition() {
        return _textPosition;
    }

    public final void setTextPosition(final int textPosition) {
        _textPosition = textPosition;
    }

    @Nullable
    public final Uri getImageUri() {
        return _imageUri;
    }

    public final void setImageUri(@Nullable final Uri imageUri) {
        _imageUri = imageUri;
    }

    private long _storyId;

    private int _textPosition;

    @Nullable
    private Uri _imageUri;
}
