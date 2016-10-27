package com.christina.app.story.fragment.singleStory;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.christina.common.contract.Contracts;

public final class BaseSingleStorySavedState implements Parcelable {
    public static final Creator<BaseSingleStorySavedState> CREATOR =
        new Creator<BaseSingleStorySavedState>() {
            @Override
            public BaseSingleStorySavedState createFromParcel(final Parcel source) {
                Contracts.requireNonNull(source, "source == null");

                return new BaseSingleStorySavedState(source);
            }

            @Override
            public BaseSingleStorySavedState[] newArray(final int size) {
                return new BaseSingleStorySavedState[size];
            }
        };

    public BaseSingleStorySavedState() {
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
    }

    protected BaseSingleStorySavedState(@NonNull final Parcel in) {
        Contracts.requireNonNull(in, "in == null");
    }

    private long _storyId;
}
