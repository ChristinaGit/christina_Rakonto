package com.christina.app.story.manager.message;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;

public final class ActivityMessageManager implements MessageManager {
    public ActivityMessageManager(@Nullable final View snackbarParentView) {
        _snackBarParentView = snackbarParentView;
    }

    @Override
    public void showInfoMessage(@NonNull final String string) {
        if (_snackBarParentView != null) {
            Snackbar.make(_snackBarParentView, string, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showInfoMessage(@StringRes final int stringId) {
        if (_snackBarParentView != null) {
            Snackbar.make(_snackBarParentView, stringId, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Nullable
    private final View _snackBarParentView;
}
