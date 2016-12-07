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
/*package-private*/ final class StoryEditorSavedState implements Parcelable {
    public static final Creator<StoryEditorSavedState> CREATOR = new Creator<StoryEditorSavedState>() {
        @Override
        public StoryEditorSavedState createFromParcel(final Parcel source) {
            return new StoryEditorSavedState(source);
        }

        @Override
        public StoryEditorSavedState[] newArray(final int size) {
            return new StoryEditorSavedState[size];
        }
    };

    public StoryEditorSavedState() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        Contracts.requireNonNull(dest, "dest == null");

        if (_mode != null) {
            dest.writeString(_mode.name());
        } else {
            dest.writeString(null);
        }
        dest.writeLong(_displayedStoryId);
        dest.writeInt(_activePage);
    }

    protected StoryEditorSavedState(@NonNull final Parcel in) {
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
