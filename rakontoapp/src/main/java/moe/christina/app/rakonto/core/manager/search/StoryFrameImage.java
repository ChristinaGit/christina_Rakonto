package moe.christina.app.rakonto.core.manager.search;

import android.support.annotation.NonNull;

import lombok.Getter;
import lombok.experimental.Accessors;

import moe.christina.common.contract.Contracts;

@Accessors(prefix = "_")
public final class StoryFrameImage {
    public StoryFrameImage(@NonNull final String previewUri, @NonNull final String uri) {
        Contracts.requireNonNull(previewUri, "previewUri == null");
        Contracts.requireNonNull(uri, "uri == null");

        _previewUri = previewUri;
        _uri = uri;
    }

    @Getter
    @NonNull
    private final String _previewUri;

    @Getter
    @NonNull
    private final String _uri;
}
