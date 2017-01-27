package moe.christina.app.rakonto.model.ui;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

// TODO: 1/24/2017 Rename.
public interface UIStory {
    long getCreateDate();

    long getId();

    long getModifyDate();

    @Nullable
    String getName();

    @Nullable
    String getPreviewUri();

    @NonNull
    List<? extends UIStoryFrame> getStoryFrames();

    @Nullable
    String getText();
}
