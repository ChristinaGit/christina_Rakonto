package moe.christina.app.rakonto.core.adpter.storiesList;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import lombok.Getter;
import lombok.experimental.Accessors;

import com.bumptech.glide.request.RequestListener;

import moe.christina.common.contract.Contracts;
import moe.christina.common.extension.delegate.loading.LoadingViewDelegate;

@Accessors(prefix = "_")
/*package-private*/ final class StoryViewCache {
    public StoryViewCache(
        @NonNull final StoryCardViewTarget viewTarget,
        @NonNull final LoadingViewDelegate loadingViewDelegate,
        @NonNull final RequestListener<String, Bitmap> loadingListener) {
        Contracts.requireNonNull(viewTarget, "viewTarget == null");
        Contracts.requireNonNull(loadingViewDelegate, "loadingViewDelegate == null");
        Contracts.requireNonNull(loadingListener, "loadingListener == null");

        _viewTarget = viewTarget;
        _loadingViewDelegate = loadingViewDelegate;
        _loadingListener = loadingListener;
    }

    @Getter
    @NonNull
    private final RequestListener<String, Bitmap> _loadingListener;

    @Getter
    @NonNull
    private final LoadingViewDelegate _loadingViewDelegate;

    @Getter
    @NonNull
    private final StoryCardViewTarget _viewTarget;
}
