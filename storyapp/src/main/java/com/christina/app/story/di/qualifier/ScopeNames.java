package com.christina.app.story.di.qualifier;

import com.christina.common.contract.Contracts;

public final class ScopeNames {
    private static final String NAME_PREFIX = "scope:";

    public static final String APPLICATION = NAME_PREFIX + "application";

    public static final String ACTIVITY = NAME_PREFIX + "activity";

    public static final String FRAGMENT = NAME_PREFIX + "fragment";

    private ScopeNames() {
        Contracts.unreachable();
    }
}
