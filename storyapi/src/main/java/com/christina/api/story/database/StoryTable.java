package com.christina.api.story.database;

import com.christina.common.contract.Contracts;

public final class StoryTable {
    public static final String NAME = "Stories";

    public static final String COLUMN_ID = "id";

    public static final String COLUMN_NAME = "name";

    public static final String COLUMN_CREATE_DATE = "createDate";

    public static final String COLUMN_MODIFY_DATE = "modifyDate";

    public static final String COLUMN_TEXT = "text";

    public static final String COLUMN_PREVIEW = "preview";

    private StoryTable() {
        Contracts.unreachable();
    }
}
