package com.christina.app.story.fragment.storiesViewer.adapter;

import android.net.Uri;
import android.support.annotation.Nullable;

import com.christina.common.view.recyclerView.StableListItem;

public interface StoryListItem extends StableListItem {
    @Nullable
    String getStoryName();

    @Nullable
    Uri getStoryPreviewUri();

    @Nullable
    String getStoryText();
}
