package com.christina.app.story.core.manager.message;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;

import com.christina.app.story.R;
import com.christina.common.contract.Contracts;
import com.christina.common.view.observerable.ObservableActivity;

@Accessors(prefix = "_")
public final class ActivityMessageManager implements MessageManager {
    public ActivityMessageManager(@NonNull final ObservableActivity observableActivity) {
        Contracts.requireNonNull(observableActivity, "observableActivity == null");

        _observableActivity = observableActivity;
    }

    @Override
    public void showInfoMessage(@NonNull final String string) {
        final val contentView = getContentView();
        if (contentView != null) {
            Snackbar.make(contentView, string, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showInfoMessage(@StringRes final int stringId) {
        final val contentView = getContentView();
        if (contentView != null) {
            Snackbar.make(contentView, stringId, Snackbar.LENGTH_SHORT).show();
        }
    }

    @NonNull
    protected final AppCompatActivity getActivity() {
        return getObservableActivity().asActivity();
    }

    @Nullable
    protected final View getContentView() {
        if (_contentView == null) {
            _contentView = getActivity().findViewById(R.id.coordinator);
        }

        return _contentView;
    }

    @Getter(AccessLevel.PROTECTED)
    @NonNull
    private final ObservableActivity _observableActivity;

    @Nullable
    private View _contentView;
}
