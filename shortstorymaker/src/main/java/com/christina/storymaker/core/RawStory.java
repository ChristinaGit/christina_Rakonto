package com.christina.storymaker.core;

import android.support.annotation.NonNull;

import java.util.SortedSet;
import java.util.TreeSet;

public final class RawStory {
    @NonNull
    public final SortedSet<Integer> getSplits() {
        return _splits;
    }

    @NonNull
    public final StringBuilder getText() {
        return _text;
    }

    @NonNull
    private final SortedSet<Integer> _splits = new TreeSet<>();

    @NonNull
    private final StringBuilder _text = new StringBuilder();
}
