package com.christina.app.story.model.contract;

import android.content.UriMatcher;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.christina.common.contract.Contracts;
import com.christina.common.utility.UriUtils;

public final class StoryContentCode {
    private static final int _ENTITY_CODE_SHIFT;

    private static final int _QUERY_MASK;

    static {
        _ENTITY_CODE_SHIFT = Integer.SIZE / 2;
        _QUERY_MASK = Integer.MAX_VALUE >>> _ENTITY_CODE_SHIFT - 1;
    }

    public static int make(final int entityTypeCode, final int queryCode) {
        return entityTypeCode << _ENTITY_CODE_SHIFT | queryCode;
    }

    public static int getQueryCode(final int storyCode) {
        return storyCode & _QUERY_MASK;
    }

    public static int getEntityTypeCode(final int storyCode) {
        return storyCode >> _ENTITY_CODE_SHIFT;
    }

    private StoryContentCode() {
        Contracts.unreachable();
    }

    public static final class Matcher {

        @NonNull
        private static final UriMatcher _URI_MATCHER;

        static {
            _URI_MATCHER = _createUriMatcher();
        }

        @NonNull
        private static UriMatcher _createUriMatcher() {
            final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

            uriMatcher.addURI(
                StoryContentContract.AUTHORITY,
                StoryContract.getStoryPath(UriUtils.NUMBER_PLACEHOLDER),
                StoryContract.CODE_STORY);
            uriMatcher.addURI(
                StoryContentContract.AUTHORITY,
                StoryContract.getStoriesPath(),
                StoryContract.CODE_STORIES);

            uriMatcher.addURI(
                StoryContentContract.AUTHORITY,
                StoryFrameContract.getStoryFramePath(UriUtils.NUMBER_PLACEHOLDER),
                StoryFrameContract.CODE_STORY_FRAME);
            uriMatcher.addURI(
                StoryContentContract.AUTHORITY,
                StoryFrameContract.getStoryFramesPath(),
                StoryFrameContract.CODE_STORY_FRAMES);
            uriMatcher.addURI(
                StoryContentContract.AUTHORITY,
                StoryFrameContract.getStoryFramesByStoryPath(UriUtils.NUMBER_PLACEHOLDER),
                StoryFrameContract.CODE_STORY_FRAMES_BY_STORY);

            return uriMatcher;
        }

        public static int get(@NonNull final Uri uri) {
            Contracts.requireNonNull(uri, "uri == null");

            return _URI_MATCHER.match(uri);
        }

        private Matcher() {
            Contracts.unreachable();
        }
    }
}
