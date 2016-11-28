package com.christina.app.story.fragment.storiesViewer;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import com.christina.common.contract.Contracts;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
public final class StoriesViewerSavedState implements Parcelable {
    public static final Creator<StoriesViewerSavedState> CREATOR =
        new Creator<StoriesViewerSavedState>() {
            @Override
            public StoriesViewerSavedState createFromParcel(@NonNull final Parcel source) {
                Contracts.requireNonNull(source, "in == null");

                return new StoriesViewerSavedState(source);
            }

            @Override
            public StoriesViewerSavedState[] newArray(final int size) {
                return new StoriesViewerSavedState[size];
            }
        };

    public StoriesViewerSavedState() {
    }

    @CallSuper
    @Override
    public int describeContents() {
        return 0;
    }

    @CallSuper
    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeInt(_scrollPosition);
    }

    protected StoriesViewerSavedState(@NonNull final Parcel in) {
        Contracts.requireNonNull(in, "in == null");

        _scrollPosition = in.readInt();
    }

    @Getter
    @Setter
    private int _scrollPosition = 0;

    @Getter
    @Setter
    private boolean _scrollPositionRestored = false;
}
