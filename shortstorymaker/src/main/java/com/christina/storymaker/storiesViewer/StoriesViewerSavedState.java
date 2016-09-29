package com.christina.storymaker.storiesViewer;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import com.christina.common.contract.Contracts;

public class StoriesViewerSavedState implements Parcelable {
    public static final Creator<StoriesViewerSavedState> CREATOR =
        new Creator<StoriesViewerSavedState>() {
            @Override
            public StoriesViewerSavedState createFromParcel(@NonNull final Parcel in) {
                Contracts.requireNonNull(in, "in == null");

                return new StoriesViewerSavedState(in);
            }

            @Override
            public StoriesViewerSavedState[] newArray(final int size) {
                return new StoriesViewerSavedState[size];
            }
        };

    public StoriesViewerSavedState() {
    }

    public final int getScrollPosition() {
        return _scrollPosition;
    }

    public final void setScrollPosition(final int scrollPosition) {
        _scrollPosition = scrollPosition;
    }

    public final boolean isScrollPositionRestored() {
        return _scrollPositionRestored;
    }

    public final void setScrollPositionRestored(final boolean scrollPositionRestored) {
        _scrollPositionRestored = scrollPositionRestored;
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

    private int _scrollPosition = 0;

    private boolean _scrollPositionRestored = false;
}
