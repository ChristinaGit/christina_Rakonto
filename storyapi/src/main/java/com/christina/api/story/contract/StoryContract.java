package com.christina.api.story.contract;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.christina.common.contract.Contracts;
import com.christina.common.utility.MimeTypeUtils;
import com.christina.common.utility.UriUtils;

import lombok.val;

public final class StoryContract {
    public final static String TYPE;

    public final static String ITEM_TYPE;

    public final static String DIR_TYPE;

    public static final String SEGMENT;

    public static final int ENTITY_TYPE_CODE;

    public static final int CODE_STORY;

    public static final int CODE_STORIES;

    public static final String TYPE_STORY;

    public static final String TYPE_STORIES;

    private static final String[] _CONTENT_TYPES_MAP;

    static {
        TYPE = "story";
        SEGMENT = "stories";

        ITEM_TYPE = MimeTypeUtils.combineItemContentType(StoryContentContract.COMPANY_NAME, TYPE);
        DIR_TYPE = MimeTypeUtils.combineDirContentType(StoryContentContract.COMPANY_NAME, TYPE);

        TYPE_STORY = ITEM_TYPE;
        TYPE_STORIES = DIR_TYPE;

        ENTITY_TYPE_CODE = StoryContentContract.ENTITY_CODE_STORY;

        int codeIndexer = 0;
        CODE_STORY = StoryContentCode.make(ENTITY_TYPE_CODE, codeIndexer++);
        CODE_STORIES = StoryContentCode.make(ENTITY_TYPE_CODE, codeIndexer++);

        _CONTENT_TYPES_MAP = _createContentTypesMap(codeIndexer);
    }

    @NonNull
    public static String getType(final int code) {
        return _CONTENT_TYPES_MAP[StoryContentCode.getQueryCode(code)];
    }

    @NonNull
    public static String getStoryPath(@NonNull final String id) {
        Contracts.requireNonNull(id, "id == null");

        return UriUtils.combine(SEGMENT, id);
    }

    @NonNull
    public static Uri getStoryUri(@NonNull final String id) {
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
    public static String extractStoryId(@NonNull final Uri uri) {
        Contracts.requireNonNull(uri, "uri == null");

        return uri.getLastPathSegment();
    }

    @NonNull
    private static String[] _createContentTypesMap(final int count) {
        final val contentTypesMap = new String[count];

        contentTypesMap[StoryContentCode.getQueryCode(CODE_STORY)] = TYPE_STORY;
        contentTypesMap[StoryContentCode.getQueryCode(CODE_STORIES)] = TYPE_STORIES;

        return contentTypesMap;
    }

    private StoryContract() {
        Contracts.unreachable();
    }
}
