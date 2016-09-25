package com.christina.content.story.model;

import android.net.Uri;
import android.support.annotation.Nullable;

import com.christina.common.data.model.Model;

public final class Story extends Model {
    public Story() {
    }

    public final long getCreateDate() {
        return _createDate;
    }

    public final void setCreateDate(final long createDate) {
        _createDate = createDate;
    }

    public final long getModifyDate() {
        return _modifyDate;
    }

    public final void setModifyDate(final long modifyDate) {
        _modifyDate = modifyDate;
    }

    @Nullable
    public final String getName() {
        return _name;
    }

    public final void setName(@Nullable final String name) {
        _name = name;
    }

    @Nullable
    public final Uri getPreviewUri() {
        return _previewUri;
    }

    public final void setPreviewUri(@Nullable final Uri previewUri) {
        _previewUri = previewUri;
    }

    @Nullable
    public final String getText() {
        return _text;
    }

    public final void setText(@Nullable final String text) {
        _text = text;
    }

    public final void setCreateDate() {
        setCreateDate(System.currentTimeMillis());
    }

    public final void setModifyDate() {
        setModifyDate(System.currentTimeMillis());
    }

    private long _createDate;

    private long _modifyDate;

    @Nullable
    private String _name;

    @Nullable
    private Uri _previewUri;

    @Nullable
    private String _text;
}
