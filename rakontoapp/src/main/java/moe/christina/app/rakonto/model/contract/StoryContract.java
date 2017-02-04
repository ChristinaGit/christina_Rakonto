package moe.christina.app.rakonto.model.contract;

import android.net.Uri;
import android.support.annotation.NonNull;

import lombok.val;

import moe.christina.common.contract.Contracts;
import moe.christina.common.utility.MimeTypeUtils;
import moe.christina.common.utility.UriUtils;

public final class StoryContract {
    public static final String TYPE;

    public static final String ITEM_TYPE;

    public static final String DIR_TYPE;

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

        ITEM_TYPE = MimeTypeUtils.combineItemContentType(StoryContentContract.AUTHORITY, TYPE);
        DIR_TYPE = MimeTypeUtils.combineDirContentType(StoryContentContract.AUTHORITY, TYPE);

        TYPE_STORY = ITEM_TYPE;
        TYPE_STORIES = DIR_TYPE;

        ENTITY_TYPE_CODE = StoryContentContract.ENTITY_CODE_STORY;

        int codeIndexer = 0;
        CODE_STORY = StoryContentCode.make(ENTITY_TYPE_CODE, codeIndexer++);
        CODE_STORIES = StoryContentCode.make(ENTITY_TYPE_CODE, codeIndexer++);

        _CONTENT_TYPES_MAP = createContentTypesMap(codeIndexer);
    }

    @NonNull
    public static String getType(final int code) {
        return _CONTENT_TYPES_MAP[StoryContentCode.getQueryCode(code)];
    }

    @NonNull
    public static String getStoryPath(@NonNull final String id) {
        Contracts.requireNonNull(id, "id == null");

        return UriUtils.combinePath(SEGMENT, id);
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
    private static String[] createContentTypesMap(final int count) {
        final val contentTypesMap = new String[count];

        contentTypesMap[StoryContentCode.getQueryCode(CODE_STORY)] = TYPE_STORY;
        contentTypesMap[StoryContentCode.getQueryCode(CODE_STORIES)] = TYPE_STORIES;

        return contentTypesMap;
    }

    private StoryContract() {
        Contracts.unreachable();
    }
}
