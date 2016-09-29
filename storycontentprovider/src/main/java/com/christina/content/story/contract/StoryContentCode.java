package com.christina.content.story.contract;

import android.content.UriMatcher;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.christina.common.contract.Contracts;
import com.christina.common.data.UriUtils;

public final class StoryContentCode {
    private static final int ENTITY_CODE_SHIFT = Integer.SIZE / 2;

    private static final int QUERY_MASK = (Integer.MAX_VALUE >> ENTITY_CODE_SHIFT) + 1;

    public static int make(final int entityTypeCode, final int queryCode) {
        return (entityTypeCode << ENTITY_CODE_SHIFT) | queryCode;
    }

    public static int getQueryCode(final int storyCode) {
        return storyCode & QUERY_MASK;
    }

    public static int getEntityTypeCode(final int storyCode) {
        return storyCode >> ENTITY_CODE_SHIFT;
    }

    private StoryContentCode() {
    }

    public static final class Matcher {

        @NonNull
        private static final UriMatcher _URI_MATCHER = _createUriMatcher();

        @NonNull
        private static UriMatcher _createUriMatcher() {
            final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

            uriMatcher.addURI(StoryContentContract.AUTHORITY,
                StoryContract.getStoryPath(UriUtils.NUMBER_PLACEHOLDER), StoryContract.CODE_STORY);
            uriMatcher.addURI(StoryContentContract.AUTHORITY, StoryContract.getStoriesPath(),
                StoryContract.CODE_STORIES);

            uriMatcher.addURI(StoryContentContract.AUTHORITY,
                StoryFrameContract.getStoryFramePath(UriUtils.NUMBER_PLACEHOLDER),
                StoryFrameContract.CODE_STORY_FRAME);
            uriMatcher.addURI(StoryContentContract.AUTHORITY,
                StoryFrameContract.getStoryFramesPath(), StoryFrameContract.CODE_STORY_FRAMES);
            uriMatcher.addURI(StoryContentContract.AUTHORITY,
                StoryFrameContract.getStoryFramesByStoryPath(UriUtils.NUMBER_PLACEHOLDER),
                StoryFrameContract.CODE_STORY_FRAMES_BY_STORY);

            return uriMatcher;
        }

        public static int get(@NonNull final Uri uri) {
            Contracts.requireNonNull(uri, "uri == null");

            return _URI_MATCHER.match(uri);
        }

        private Matcher() {
        }
    }
}
