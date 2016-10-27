package com.christina.app.story.operation.editStory;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.christina.api.story.model.Story;
import com.christina.common.contract.Contracts;

public final class EditStorySavedState implements Parcelable {
    public static final Creator<EditStorySavedState> CREATOR = new Creator<EditStorySavedState>() {
        @Override
        public EditStorySavedState createFromParcel(final Parcel source) {
            return new EditStorySavedState(source);
        }

        @Override
        public EditStorySavedState[] newArray(final int size) {
            return new EditStorySavedState[size];
        }
    };

    public EditStorySavedState() {
    }

    public final int getActivePage() {
        return _activePage;
    }

    public final void setActivePage(final int activePage) {
        _activePage = activePage;
    }

    @Nullable
    public final EditStoryActivity.Mode getMode() {
        return _mode;
    }

    public final void setMode(@Nullable final EditStoryActivity.Mode mode) {
        _mode = mode;
    }

    public final long getStoryId() {
        return _storyId;
    }

    public final void setStoryId(final long storyId) {
        _storyId = storyId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        Contracts.requireNonNull(dest, "dest == null");

        dest.writeLong(_storyId);
        if (_mode != null) {
            dest.writeString(_mode.name());
        } else {
            dest.writeString(null);
        }
        dest.writeInt(_activePage);
    }

    protected EditStorySavedState(@NonNull final Parcel in) {
        Contracts.requireNonNull(in, "in == null");

        _storyId = in.readLong();
        final String modeName = in.readString();
        if (modeName != null) {
            _mode = EditStoryActivity.Mode.valueOf(modeName);
        } else {
            _mode = null;
        }
        _activePage = in.readInt();
    }

    private int _activePage = 0;

    @Nullable
    private EditStoryActivity.Mode _mode;

    private long _storyId = Story.NO_ID;
}
