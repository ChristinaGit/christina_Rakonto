package com.christina.api.story.util;

import android.support.annotation.NonNull;

import org.apache.commons.collections4.Predicate;

public final class StoryPredicate {
    private StoryPredicate() {
    }

    public static final class Story {
        @NonNull
        public static Predicate<com.christina.api.story.model.Story> idEquals(final long id) {
            return new Predicate<com.christina.api.story.model.Story>() {
                @Override
                public boolean evaluate(final com.christina.api.story.model.Story object) {
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

        private Story() {
        }
    }

    public static final class StoryFrame {
        @NonNull
        public static Predicate<com.christina.api.story.model.StoryFrame> idEquals(final long id) {
            return new Predicate<com.christina.api.story.model.StoryFrame>() {
                @Override
                public boolean evaluate(final com.christina.api.story.model.StoryFrame object) {
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
        public static Predicate<com.christina.api.story.model.StoryFrame> storyIdEquals(
            final long storyId) {
            return new Predicate<com.christina.api.story.model.StoryFrame>() {
                @Override
                public boolean evaluate(final com.christina.api.story.model.StoryFrame object) {
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

        private StoryFrame() {
        }
    }
}
