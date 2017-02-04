package moe.christina.app.rakonto.model.contract;

import android.net.Uri;
import android.support.annotation.NonNull;

import lombok.val;

import moe.christina.common.contract.Contracts;
import moe.christina.common.utility.MimeTypeUtils;
import moe.christina.common.utility.UriUtils;

public final class StoryFrameContract {
    public static final String TYPE;

    public static final String ITEM_TYPE;

    public static final String DIR_TYPE;

    public static final String SEGMENT;

    public static final int ENTITY_TYPE_CODE;

    public static final int CODE_STORY_FRAME;

    public static final int CODE_STORY_FRAMES;

    public static final int CODE_STORY_FRAMES_BY_STORY;

    public static final String TYPE_STORY_FRAME;

    public static final String TYPE_STORY_FRAMES;

    private static final String[] _CONTENT_TYPES_MAP;

    static {
        TYPE = "frame";
        SEGMENT = "frames";

        ITEM_TYPE = MimeTypeUtils.combineItemContentType(StoryContentContract.AUTHORITY, TYPE);
        DIR_TYPE = MimeTypeUtils.combineDirContentType(StoryContentContract.AUTHORITY, TYPE);

        TYPE_STORY_FRAME = ITEM_TYPE;
        TYPE_STORY_FRAMES = DIR_TYPE;

        ENTITY_TYPE_CODE = StoryContentContract.ENTITY_CODE_STORY_FRAME;

        int codeIndexer = 0;
        CODE_STORY_FRAME = StoryContentCode.make(ENTITY_TYPE_CODE, codeIndexer++);
        CODE_STORY_FRAMES = StoryContentCode.make(ENTITY_TYPE_CODE, codeIndexer++);
        CODE_STORY_FRAMES_BY_STORY = StoryContentCode.make(ENTITY_TYPE_CODE, codeIndexer++);

        _CONTENT_TYPES_MAP = createContentTypesMap(codeIndexer);
    }

    @NonNull
    public static String getType(final int code) {
        return _CONTENT_TYPES_MAP[StoryContentCode.getQueryCode(code)];
    }

    @NonNull
    public static String getStoryFramePath(@NonNull final String id) {
        Contracts.requireNonNull(id, "id == null");

        return UriUtils.combinePath(SEGMENT, id);
    }

    @NonNull
    public static Uri getStoryFrameUri(@NonNull final String id) {
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
    public static String getStoryFramesByStoryPath(@NonNull final String storyId) {
        return UriUtils.combinePath(StoryContract.SEGMENT, storyId, SEGMENT);
    }

    @NonNull
    public static Uri getStoryFramesByStoryUri(@NonNull final String storyId) {
        return StoryContentContract.CONTENT_URI
            .buildUpon()
            .path(getStoryFramesByStoryPath(storyId))
            .build();
    }

    @NonNull
    public static String extractStoryFrameId(@NonNull final Uri uri) {
        Contracts.requireNonNull(uri, "uri == null");

        return uri.getLastPathSegment();
    }

    @NonNull
    public static String extractStoryId(@NonNull final Uri uri) {
        Contracts.requireNonNull(uri, "uri == null");

        return uri.getPathSegments().get(2);
    }

    @NonNull
    private static String[] createContentTypesMap(final int count) {
        final val contentTypesMap = new String[count];

        contentTypesMap[StoryContentCode.getQueryCode(CODE_STORY_FRAME)] = TYPE_STORY_FRAME;
        contentTypesMap[StoryContentCode.getQueryCode(CODE_STORY_FRAMES)] = TYPE_STORY_FRAMES;
        contentTypesMap[StoryContentCode.getQueryCode(CODE_STORY_FRAMES_BY_STORY)] =
            TYPE_STORY_FRAMES;

        return contentTypesMap;
    }

    private StoryFrameContract() {
        Contracts.unreachable();
    }
}
