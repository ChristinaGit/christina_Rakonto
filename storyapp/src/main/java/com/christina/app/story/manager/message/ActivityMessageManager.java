package com.christina.app.story.manager.message;

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
    public ActivityMessageManager(
        @NonNull final SnackbarParentViewProvider snackbarParentViewProvider) {
        Contracts.requireNonNull(snackbarParentViewProvider, "snackbarParentViewProvider == null");

        _snackbarParentViewProvider = snackbarParentViewProvider;
    }

    @Override
    public void showInfoMessage(@NonNull final String string) {
        final val snackbarParentView = getSnackbarParentView();
        if (snackbarParentView != null) {
            Snackbar.make(snackbarParentView, string, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showInfoMessage(@StringRes final int stringId) {
        final val snackbarParentView = getSnackbarParentView();
        if (snackbarParentView != null) {
            Snackbar.make(snackbarParentView, stringId, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Nullable
    protected final View getSnackbarParentView() {
        return getSnackbarParentViewProvider().getSnackbarParentView();
    }

    @Getter(AccessLevel.PROTECTED)
    @NonNull
    private final SnackbarParentViewProvider _snackbarParentViewProvider;

    public interface SnackbarParentViewProvider {
        @Nullable
        View getSnackbarParentView();
    }
}
