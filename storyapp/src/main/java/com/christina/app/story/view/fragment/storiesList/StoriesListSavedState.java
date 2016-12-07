package com.christina.app.story.view.fragment.storiesList;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import com.christina.common.contract.Contracts;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
/*package-private*/ final class StoriesListSavedState implements Parcelable {
    public static final Creator<StoriesListSavedState> CREATOR =
        new Creator<StoriesListSavedState>() {
            @Override
            public StoriesListSavedState createFromParcel(@NonNull final Parcel source) {
                Contracts.requireNonNull(source, "in == null");

                return new StoriesListSavedState(source);
            }

            @Override
            public StoriesListSavedState[] newArray(final int size) {
                return new StoriesListSavedState[size];
            }
        };

    public StoriesListSavedState() {
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

    protected StoriesListSavedState(@NonNull final Parcel in) {
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
