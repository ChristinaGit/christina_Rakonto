package com.christina.app.story.di.qualifier;

import com.christina.common.contract.Contracts;

public final class ScopeNames {
    private static final String NAME_PREFIX = "scope:";

    public static final String APPLICATION = NAME_PREFIX + "application";

    public static final String SCREEN = NAME_PREFIX + "screen";

    public static final String SUBSCREEN = NAME_PREFIX + "subscreen";

    private ScopeNames() {
        Contracts.unreachable();
    }
}
