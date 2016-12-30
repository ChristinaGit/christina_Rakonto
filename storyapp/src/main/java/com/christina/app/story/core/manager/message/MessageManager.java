package com.christina.app.story.core.manager.message;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

public interface MessageManager {
    void showInfoMessage(@NonNull String string);

    void showInfoMessage(@StringRes int stringId);
}
