package com.christina.app.story.fragment.fullSingleStory;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.christina.common.contract.Contracts;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
public final class FullSingleStorySavedState implements Parcelable {
    public static final Creator<FullSingleStorySavedState> CREATOR =
        new Creator<FullSingleStorySavedState>() {
            @Override
            public FullSingleStorySavedState createFromParcel(final Parcel source) {
                Contracts.requireNonNull(source, "source == null");

                return new FullSingleStorySavedState(source);
            }

            @Override
            public FullSingleStorySavedState[] newArray(final int size) {
                return new FullSingleStorySavedState[size];
            }
        };

    public FullSingleStorySavedState() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        Contracts.requireNonNull(dest, "dest == null");

        dest.writeLong(_storyId);
    }

    protected FullSingleStorySavedState(@NonNull final Parcel in) {
        Contracts.requireNonNull(in, "in == null");

        _storyId = in.readLong();
    }

    @Getter
    @Setter
    private long _storyId;
}
