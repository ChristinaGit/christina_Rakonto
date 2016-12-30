package com.christina.app.story.core.manager.message;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.christina.common.contract.Contracts;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;

@Accessors(prefix = "_")
public final class ActivityMessageManager implements MessageManager {
    public ActivityMessageManager(@NonNull final Activity activity) {
        Contracts.requireNonNull(activity, "activity == null");

        _activity = activity;
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

    @Nullable
    protected final View getContentView() {
        if (_contentView == null) {
            _contentView = getActivity().findViewById(android.R.id.content);
        }

        return _contentView;
    }

    @Getter(AccessLevel.PROTECTED)
    @NonNull
    private final Activity _activity;

    @Nullable
    private View _contentView;
}
