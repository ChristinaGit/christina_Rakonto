package com.christina.api.story.util;

import android.support.annotation.NonNull;

import com.christina.api.story.model.Story;
import com.christina.common.contract.Contracts;

import org.apache.commons.collections4.Predicate;

public final class StoryPredicate {
    @NonNull
    public static Predicate<Story> idEquals(final long id) {
        return new Predicate<Story>() {
            @Override
            public boolean evaluate(final Story object) {
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

    private StoryPredicate() {
        Contracts.unreachable();
    }
}
