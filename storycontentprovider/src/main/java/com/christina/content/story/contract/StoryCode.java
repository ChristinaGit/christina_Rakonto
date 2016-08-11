package com.christina.content.story.contract;

import android.content.UriMatcher;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.christina.common.contract.Contracts;

public final class StoryCode {
    private static final int ENTITY_CODE_SHIFT = Integer.SIZE / 2;

    private static final int QUERY_MASK = 0x0000_FFFF;

    private static final int ENTITY_MASK = ~QUERY_MASK;

    @NonNull
    private static final UriMatcher _uriMatcher = _createUriMatcher();

    public static int make(int entityTypeCode, int queryCode) {
        return (entityTypeCode << ENTITY_CODE_SHIFT) & queryCode;
    }

    public static int getQueryCode(int storyCode) {
        return storyCode & QUERY_MASK;
    }

    public static int getEntityTypeCode(int storyCode) {
        return storyCode & ENTITY_MASK;
    }

    public static int get(@NonNull Uri uri) {
        Contracts.requireNonNull(uri, "uri == null");

        return _uriMatcher.match(uri);
    }

    @NonNull
    private static UriMatcher _createUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(StoryContentContract.AUTHORITY, StoryContract.getStoryPath("#"),
                          StoryContract.CODE_STORY);
        uriMatcher.addURI(StoryContentContract.AUTHORITY, StoryContract.getStoriesPath(),
                          StoryContract.CODE_STORIES);

        uriMatcher.addURI(StoryContentContract.AUTHORITY, StoryFrameContract.getStoryFramePath("#"),
                          StoryFrameContract.CODE_STORY_FRAME);
        uriMatcher.addURI(StoryContentContract.AUTHORITY, StoryFrameContract.getStoryFramesPath(),
                          StoryFrameContract.CODE_STORY_FRAMES);
        uriMatcher.addURI(StoryContentContract.AUTHORITY,
                          StoryFrameContract.getStoryFramesByStoryPath("#"),
                          StoryFrameContract.CODE_STORY_FRAMES_BY_STORY);

        return uriMatcher;
    }

    private StoryCode() {
    }
}
