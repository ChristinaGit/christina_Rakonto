package com.christina.app.story.view.fragment.storiesList;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import com.christina.common.contract.Contracts;

@Accessors(prefix = "_")
/*package-private*/ final class StoriesListState implements Parcelable {
    public static final Creator<StoriesListState> CREATOR = new Creator<StoriesListState>() {
        @Override
        public StoriesListState createFromParcel(@NonNull final Parcel source) {
            Contracts.requireNonNull(source, "in == null");

            return new StoriesListState(source);
        }

        @Override
        public StoriesListState[] newArray(final int size) {
            return new StoriesListState[size];
        }
    };

    public StoriesListState() {
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

    protected StoriesListState(@NonNull final Parcel in) {
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
