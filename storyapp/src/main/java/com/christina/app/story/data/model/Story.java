package com.christina.app.story.data.model;

import android.support.annotation.Nullable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
@ToString(doNotUseGetters = true)
@Accessors(prefix = "_")
public class Story implements RealmModel {
    public static final String ID = "_id";

    public static final String CREATE_DATE = "_createDate";

    public static final String MODIFY_DATE = "_modifyDate";

    public static final String NAME = "_name";

    public static final String PREVIEW_URI = "_previewUri";

    public static final String STORY_FRAMES = "_storyFrames";

    public static final String TEXT = "_text";

    @Getter
    @Setter
    private long _createDate;

    @Getter
    @Setter
    @PrimaryKey
    private long _id;

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
    private String _previewUri;

    @Getter
    @Setter
    @Nullable
    private RealmList<StoryFrame> _storyFrames;

    @Getter
    @Setter
    @Nullable
    private String _text;
}
