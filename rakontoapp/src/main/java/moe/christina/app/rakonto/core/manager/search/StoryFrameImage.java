package moe.christina.app.rakonto.core.manager.search;

import android.support.annotation.NonNull;

import lombok.Getter;
import lombok.experimental.Accessors;

import moe.christina.common.contract.Contracts;

@Accessors(prefix = "_")
public final class StoryFrameImage {
    public StoryFrameImage(@NonNull final String uri, @NonNull final String previewUri) {
        Contracts.requireNonNull(uri, "uri == null");
        Contracts.requireNonNull(previewUri, "previewUri == null");

        _uri = uri;
        _previewUri = previewUri;
    }

    @Getter
    @NonNull
    private final String _previewUri;

    @Getter
    @NonNull
    private final String _uri;
}
