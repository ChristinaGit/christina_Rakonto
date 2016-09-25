package com.christina.content.story.contract;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.christina.common.contract.Contracts;
import com.christina.common.data.MimeTypeUtils;
import com.christina.common.data.UriUtils;

public class StoryFrameContract {
    public final static String TYPE = "frame";

    public final static String ITEM_TYPE =
        MimeTypeUtils.combineItemContentType(StoryContentContract.COMPANY_NAME, TYPE);

    public final static String DIR_TYPE =
        MimeTypeUtils.combineDirContentType(StoryContentContract.COMPANY_NAME, TYPE);

    public static final String SEGMENT = "frames";

    public static final int ENTITY_TYPE_CODE = StoryContentContract.ENTITY_CODE_STORY_FRAME;

    private static int _codeIndexer = 0;

    public static final int CODE_STORY_FRAME = StoryContentCode.make(ENTITY_TYPE_CODE, _codeIndexer++);

    public static final int CODE_STORY_FRAMES = StoryContentCode.make(ENTITY_TYPE_CODE, _codeIndexer++);

    public static final int CODE_STORY_FRAMES_BY_STORY =
        StoryContentCode.make(ENTITY_TYPE_CODE, _codeIndexer++);

    public static final String TYPE_STORY_FRAME = ITEM_TYPE;

    public static final String TYPE_STORY_FRAMES = DIR_TYPE;

    private static final String[] _contentTypesMap = _createContentTypesMap();

    @NonNull
    public static String getType(int code) {
        return _contentTypesMap[StoryContentCode.getQueryCode(code)];
    }

    @NonNull
    public static String getStoryFramePath(@NonNull String id) {
        Contracts.requireNonNull(id, "id == null");

        return UriUtils.combine(SEGMENT, id);
    }

    @NonNull
    public static Uri getStoryFrameUri(@NonNull String id) {
        Contracts.requireNonNull(id, "id == null");

        return StoryContentContract.CONTENT_URI.buildUpon().path(getStoryFramePath(id)).build();
    }

    @NonNull
    public static String getStoryFramesPath() {
        return SEGMENT;
    }

    @NonNull
    public static Uri getStoryFramesUri() {
        return StoryContentContract.CONTENT_URI.buildUpon().path(getStoryFramesPath()).build();
    }

    @NonNull
    public static String getStoryFramesByStoryPath(@NonNull String storyId) {
        return UriUtils.combine(StoryContract.SEGMENT, storyId, SEGMENT);
    }

    @NonNull
    public static Uri getStoryFramesByStoryUri(@NonNull String storyId) {
        return StoryContentContract.CONTENT_URI.buildUpon()
                                               .path(getStoryFramesByStoryPath(storyId))
                                               .build();
    }

    @NonNull
    public static String extractStoryFrameId(@NonNull Uri uri) {
        Contracts.requireNonNull(uri, "uri == null");

        return uri.getLastPathSegment();
    }

    @NonNull
    public static String extractStoryId(@NonNull Uri uri) {
        Contracts.requireNonNull(uri, "uri == null");

        return uri.getPathSegments().get(2);
    }

    @NonNull
    private static String[] _createContentTypesMap() {
        final String[] contentTypesMap = new String[_codeIndexer];

        contentTypesMap[StoryContentCode.getQueryCode(CODE_STORY_FRAME)] = TYPE_STORY_FRAME;
        contentTypesMap[StoryContentCode.getQueryCode(CODE_STORY_FRAMES)] = TYPE_STORY_FRAMES;
        contentTypesMap[StoryContentCode.getQueryCode(CODE_STORY_FRAMES_BY_STORY)] = TYPE_STORY_FRAMES;

        return contentTypesMap;
    }

    private StoryFrameContract() {
    }
}
