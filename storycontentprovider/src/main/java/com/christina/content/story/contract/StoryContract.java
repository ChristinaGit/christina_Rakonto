package com.christina.content.story.contract;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.christina.common.contract.Contracts;
import com.christina.common.data.MimeTypeUtils;
import com.christina.common.data.UriUtils;

public final class StoryContract {
    public final static String TYPE = "story";

    public final static String ITEM_TYPE =
        MimeTypeUtils.combineItemContentType(StoryContentContract.COMPANY_NAME, TYPE);

    public final static String DIR_TYPE =
        MimeTypeUtils.combineDirContentType(StoryContentContract.COMPANY_NAME, TYPE);

    public static final String SEGMENT = "stories";

    public static final int ENTITY_TYPE_CODE = StoryContentContract.ENTITY_CODE_STORY;

    private static int _codeIndexer = 0;

    public static final int CODE_STORY = StoryCode.make(ENTITY_TYPE_CODE, _codeIndexer++);

    public static final int CODE_STORIES = StoryCode.make(ENTITY_TYPE_CODE, _codeIndexer++);

    public static final String TYPE_STORY = ITEM_TYPE;

    public static final String TYPE_STORIES = DIR_TYPE;

    private static final String[] _contentTypesMap = _createContentTypesMap();

    @NonNull
    public static String getType(int code) {
        return _contentTypesMap[StoryCode.getQueryCode(code)];
    }

    @NonNull
    public static String getStoryPath(@NonNull String id) {
        Contracts.requireNonNull(id, "id == null");

        return UriUtils.combine(SEGMENT, id);
    }

    @NonNull
    public static Uri getStoryUri(@NonNull String id) {
        Contracts.requireNonNull(id, "id == null");

        return StoryContentContract.CONTENT_URI.buildUpon().path(getStoryPath(id)).build();
    }

    @NonNull
    public static String getStoriesPath() {
        return SEGMENT;
    }

    @NonNull
    public static Uri getStoriesUri() {
        return StoryContentContract.CONTENT_URI.buildUpon().path(getStoriesPath()).build();
    }

    @NonNull
    public static String extractStoryId(@NonNull Uri uri) {
        Contracts.requireNonNull(uri, "uri == null");

        return uri.getLastPathSegment();
    }

    @NonNull
    private static String[] _createContentTypesMap() {
        final String[] contentTypesMap = new String[_codeIndexer];

        contentTypesMap[StoryCode.getQueryCode(CODE_STORY)] = TYPE_STORY;
        contentTypesMap[StoryCode.getQueryCode(CODE_STORIES)] = TYPE_STORIES;

        return contentTypesMap;
    }

    private StoryContract() {
    }
}
