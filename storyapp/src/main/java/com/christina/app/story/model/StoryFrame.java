package com.christina.app.story.model;

import android.support.annotation.Nullable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

import com.christina.app.story.model.ui.UIStoryFrame;

@RealmClass
@ToString(doNotUseGetters = true)
@Accessors(prefix = "_")
public class StoryFrame implements RealmModel, UIStoryFrame {
    public static final String ID = "_id";

    public static final String IMAGE_URI = "_imageUri";

    public static final String TEXT_END_POSITION = "_textEndPosition";

    public static final String TEXT_START_POSITION = "_textStartPosition";

    public static final String FILE_IMAGE = "image";

    @Getter(onMethod = @__(@Override))
    @Setter
    @PrimaryKey
    private long _id;

    @Getter(onMethod = @__(@Override))
    @Setter
    @Nullable
    private String _imageUri;

    @Getter(onMethod = @__(@Override))
    @Setter
    private int _textEndPosition;

    @Getter(onMethod = @__(@Override))
    @Setter
    private int _textStartPosition;
}
