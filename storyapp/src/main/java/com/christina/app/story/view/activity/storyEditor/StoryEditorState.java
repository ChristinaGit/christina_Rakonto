package com.christina.app.story.view.activity.storyEditor;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.christina.api.story.model.Story;
import com.christina.common.contract.Contracts;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
/*package-private*/ final class StoryEditorState implements Parcelable {
    public static final Creator<StoryEditorState> CREATOR =
        new Creator<StoryEditorState>() {
            @Override
            public StoryEditorState createFromParcel(final Parcel source) {
                return new StoryEditorState(source);
            }

            @Override
            public StoryEditorState[] newArray(final int size) {
                return new StoryEditorState[size];
            }
        };

    public StoryEditorState() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        Contracts.requireNonNull(dest, "dest == null");

        final String modeName;
        if (_mode != null) {
            modeName = _mode.name();
        } else {
            modeName = null;
        }
        dest.writeString(modeName);
        dest.writeLong(_displayedStoryId);
        dest.writeInt(_activePage);
    }

    protected StoryEditorState(@NonNull final Parcel in) {
        Contracts.requireNonNull(in, "in == null");

        final String modeName = in.readString();
        if (modeName != null) {
            _mode = StoryEditorActivity.Mode.valueOf(modeName);
        } else {
            _mode = null;
        }
        _displayedStoryId = in.readLong();
        _activePage = in.readInt();
    }

    @Getter
    @Setter
    private int _activePage = 0;

    @Getter
    @Setter
    private long _displayedStoryId = Story.NO_ID;

    @Getter
    @Setter
    @Nullable
    private StoryEditorActivity.Mode _mode;
}
