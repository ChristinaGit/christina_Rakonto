package moe.christina.app.rakonto.model.ui;

import android.support.annotation.Nullable;

public interface UIStoryFrame {
    long getId();

    @Nullable
    String getImageUri();

    int getTextEndPosition();

    int getTextStartPosition();
}
