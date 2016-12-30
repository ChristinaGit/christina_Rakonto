package com.christina.app.story.core.delegate;

import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.View;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.val;

@Accessors(prefix = "_")
public class LoadingViewDelegate {
    public final void invalidateContentView() {
        final val contentView = getContentView();
        if (contentView != null) {
            if (isContentVisible()) {
                contentView.setVisibility(View.VISIBLE);
            } else {
                contentView.setVisibility(View.INVISIBLE);
            }
        }
    }

    public final void invalidateLoadingView() {
        final val loadingView = getLoadingView();
        if (loadingView != null) {
            if (isLoadingVisible()) {
                loadingView.show();
            } else {
                loadingView.hide();
            }
        }
    }

    public final void setContentVisible(final boolean visible) {
        if (_contentVisible != visible) {
            _contentVisible = visible;

            onContentVisibilityChanged();
        }
    }

    public final void setLoadingVisible(final boolean visible) {
        if (_loadingVisible != visible) {
            _loadingVisible = visible;

            onLoadingVisibilityChanged();
        }
    }

    @CallSuper
    public void invalidateViews() {
        invalidateContentView();
        invalidateLoadingView();
    }

    protected void onContentVisibilityChanged() {
        invalidateContentView();
    }

    protected void onLoadingVisibilityChanged() {
        invalidateLoadingView();
    }

    @Getter
    @Setter
    @Nullable
    private View _contentView;

    @Getter
    private boolean _contentVisible;

    @Getter
    @Setter
    @Nullable
    private ContentLoadingProgressBar _loadingView;

    @Getter
    private boolean _loadingVisible;
}
