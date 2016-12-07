package com.christina.api.story.util;

import android.support.annotation.NonNull;

import com.christina.api.story.model.StoryFrame;
import com.christina.common.contract.Contracts;

import org.apache.commons.collections4.Predicate;

public final class StoryFramePredicate {
    @NonNull
    public static Predicate<StoryFrame> idEquals(final long id) {
        return new Predicate<StoryFrame>() {
            @Override
            public boolean evaluate(final StoryFrame object) {
                final boolean result;

                if (object != null) {
                    result = object.getId() == id;
                } else {
                    result = false;
                }

                return result;
            }
        };
    }

    @NonNull
    public static Predicate<StoryFrame> storyIdEquals(
        final long storyId) {
        return new Predicate<StoryFrame>() {
            @Override
            public boolean evaluate(final StoryFrame object) {
                final boolean result;

                if (object != null) {
                    result = object.getStoryId() == storyId;
                } else {
                    result = false;
                }

                return result;
            }
        };
    }

    private StoryFramePredicate() {
        Contracts.unreachable();
    }
}
