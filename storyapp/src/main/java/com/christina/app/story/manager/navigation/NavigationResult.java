package com.christina.app.story.manager.navigation;

import lombok.experimental.Accessors;

@Accessors(prefix = "_")
public enum NavigationResult {
    SUCCESS,
    CANCELED,
    FAILED,
    UNKNOWN;
}
