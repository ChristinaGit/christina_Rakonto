package com.christina.app.story.core.delegate;

import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.view.View;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.val;

import com.christina.app.story.R;
import com.christina.common.utility.AnimationViewUtils;
import com.christina.common.view.ContentLoaderProgressBar;

@Accessors(prefix = "_")
public class LoadingViewDelegate {
    public final void invalidateContentView() {
        final val contentView = getContentView();
        if (contentView != null) {
            if (isContentVisible()) {
                AnimationViewUtils.animateSetVisibility(contentView,
                                                        View.VISIBLE,
                                                        R.anim.fade_in_long);
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
    public void hideAll() {
        setContentVisible(false);
        setLoadingVisible(false);
    }

    @CallSuper
    public void invalidateViews() {
        invalidateContentView();
        invalidateLoadingView();
    }

    @CallSuper
    public void showContent() {
        setContentVisible(true);
        setLoadingVisible(false);
    }

    @CallSuper
    public void showLoading() {
        setContentVisible(false);
        setLoadingVisible(true);
    }

    @CallSuper
    protected void onContentVisibilityChanged() {
        invalidateContentView();
    }

    @CallSuper
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
    private ContentLoaderProgressBar _loadingView;

    @Getter
    private boolean _loadingVisible;
}
