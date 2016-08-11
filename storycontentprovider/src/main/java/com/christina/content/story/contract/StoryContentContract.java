package com.christina.content.story.contract;

import android.net.Uri;

import com.christina.common.data.UriSchemes;
import com.christina.common.data.UriUtils;

public final class StoryContentContract {
    public static final String COMPANY_NAME = "com.christina";

    public static final String AUTHORITY = COMPANY_NAME + ".provider";

    public static final String CONTENT_URI_STRING =
        UriSchemes.CONTENT + UriUtils.SCHEMA_SEPARATOR + AUTHORITY;

    public static final Uri CONTENT_URI = Uri.parse(CONTENT_URI_STRING);

    private static int _entityTypeCodeIndexer = 0;

    public static final int ENTITY_CODE_STORY = _entityTypeCodeIndexer++;

    public static final int ENTITY_CODE_STORY_FRAME = _entityTypeCodeIndexer++;

    private StoryContentContract() {
    }
}
