package com.christina.app.story.fragment.storiesViewer.loader;

import android.support.annotation.Nullable;

import com.christina.api.story.model.Story;
import com.christina.common.data.cursor.dataCursor.DataCursor;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
public final class StoriesLoaderResult {
    public StoriesLoaderResult(@Nullable final DataCursor<Story> stories) {
        _stories = stories;
    }

    @Getter
    @Nullable
    private final DataCursor<Story> _stories;
}
