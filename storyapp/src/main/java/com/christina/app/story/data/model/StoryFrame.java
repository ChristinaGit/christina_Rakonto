package com.christina.app.story.data.model;

import android.support.annotation.Nullable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
@ToString(doNotUseGetters = true)
@Accessors(prefix = "_")
public class StoryFrame implements RealmModel {
    public static final String ID = "_id";

    public static final String STORY_ID = "_storyId";

    public static final String IMAGE_URI = "_imageUri";

    public static final String TEXT_END_POSITION = "_textEndPosition";

    public static final String TEXT_START_POSITION = "_textStartPosition";

    @Getter
    @Setter
    @PrimaryKey
    private long _id;

    @Getter
    @Setter
    @Nullable
    private String _imageUri;

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
