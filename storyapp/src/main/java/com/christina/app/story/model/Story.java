package com.christina.app.story.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

import com.christina.app.story.model.ui.UIStory;

@RealmClass
@ToString(doNotUseGetters = true)
@Accessors(prefix = "_")
public class Story implements RealmModel, UIStory {
    public static final String ID = "_id";

    public static final String CREATE_DATE = "_createDate";

    public static final String MODIFY_DATE = "_modifyDate";

    public static final String NAME = "_name";

    public static final String PREVIEW_URI = "_previewUri";

    public static final String STORY_FRAMES = "_storyFrames";

    public static final String TEXT = "_text";

    public static final String FILE_PREVIEW = "preview";

    @Getter(onMethod = @__(@Override))
    @Setter
    private long _createDate;

    @Getter(onMethod = @__(@Override))
    @Setter
    @PrimaryKey
    private long _id;

    @Getter(onMethod = @__(@Override))
    @Setter
    private long _modifyDate;

    @Getter(onMethod = @__(@Override))
    @Setter
    @Nullable
    private String _name;

    @Getter(onMethod = @__(@Override))
    @Setter
    @Nullable
    private String _previewUri;

    @Getter(onMethod = @__(@Override))
    @Setter
    @NonNull
    private RealmList<StoryFrame> _storyFrames;

    @Getter(onMethod = @__(@Override))
    @Setter
    @Nullable
    private String _text;
}
