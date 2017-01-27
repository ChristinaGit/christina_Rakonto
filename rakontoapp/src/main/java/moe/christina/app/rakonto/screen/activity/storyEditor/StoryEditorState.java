package moe.christina.app.rakonto.screen.activity.storyEditor;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import moe.christina.common.contract.Contracts;
import moe.christina.common.utility.ParcelUtils;

@Accessors(prefix = "_")
/*package-private*/ final class StoryEditorState implements Parcelable {
    public static final Creator<StoryEditorState> CREATOR = new Creator<StoryEditorState>() {
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

        if (_storyId != null) {
            ParcelUtils.writeBoolean(dest, true);
            dest.writeLong(_storyId);
        } else {
            ParcelUtils.writeBoolean(dest, false);
        }

        dest.writeInt(_activePage);
    }

    protected StoryEditorState(@NonNull final Parcel in) {
        Contracts.requireNonNull(in, "in == null");

        final String modeName = in.readString();
        if (modeName != null) {
            _mode = StoryEditorMode.valueOf(modeName);
        } else {
            _mode = null;
        }

        if (ParcelUtils.readBoolean(in)) {
            _storyId = in.readLong();
        } else {
            _storyId = null;
        }

        _activePage = in.readInt();
    }

    @Getter
    @Setter
    private int _activePage = 0;

    @Getter
    @Setter
    @Nullable
    private StoryEditorMode _mode;

    @Getter
    @Setter
    @Nullable
    private Long _storyId;
}
