package com.christina.api.story.model;

import android.net.Uri;
import android.support.annotation.Nullable;

import com.christina.common.data.model.Model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
public final class Story extends Model {
    @Getter
    @Setter
    private long _createDate;

    @Getter
    @Setter
    private long _modifyDate;

    @Getter
    @Setter
    @Nullable
    private String _name;

    @Getter
    @Setter
    @Nullable
    private Uri _previewUri;

    @Getter
    @Setter
    @Nullable
    private String _text;
}
