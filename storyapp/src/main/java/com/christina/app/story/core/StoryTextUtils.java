package com.christina.app.story.core;

import android.support.annotation.NonNull;

import com.christina.common.contract.Contracts;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public final class StoryTextUtils {
    private static final int PART_MAX_WORDS_COUNT = 3;

    @NonNull
    public static String cleanup(@NonNull String rawStory) {
        Contracts.requireNonNull(rawStory, "rawStory == null");

        rawStory = rawStory.replaceAll("\\s", " ");
        rawStory = rawStory.replaceAll("[\\p{Cc}\\p{Cf}\\p{Co}\\p{Cn}]", StringUtils.EMPTY);
        rawStory = rawStory.trim();

        return rawStory;
    }

    @NonNull
    public static List<String> defaultSplit(@NonNull final String rawStory) {
        Contracts.requireNonNull(rawStory, "rawStory == null");

        final List<String> defaultSplit = new ArrayList<>();

        final StringBuilder part = new StringBuilder();

        final List<String> sentences = splitBySentences(rawStory);
        for (final String sentence : sentences) {
            final List<String> words = splitByWords(sentence);

            int wordCount = 0;
            for (final String word : words) {
                part.append(word);
                wordCount++;

                if (wordCount == PART_MAX_WORDS_COUNT) {
                    defaultSplit.add(part.toString());
                    part.setLength(0);
                    wordCount = 0;
                }
            }

            if (part.length() > 0) {
                defaultSplit.add(part.toString());
            }
            part.setLength(0);
        }

        return defaultSplit;
    }

    private static boolean isSentenceSeparator(final char ch) {
        return !Character.isLetterOrDigit(ch) && !Character.isSpaceChar(ch);
    }

    @NonNull
    private static List<String> splitByWords(@NonNull final String sentence) {
        Contracts.requireNonNull(sentence, "sentence == null");

        final List<String> words = new ArrayList<>();

        final StringBuilder part = new StringBuilder();

        final int sentenceLength = sentence.length();
        for (int i = 0; i < sentenceLength; i++) {
            final char ch = sentence.charAt(i);

            part.append(ch);
            if (Character.isSpaceChar(ch)) {
                words.add(part.toString());
                part.setLength(0);
            }
        }

        if (part.length() > 0) {
            words.add(part.toString());
        }

        return words;
    }

    @NonNull
    private static List<String> splitBySentences(@NonNull final String rawStory) {
        Contracts.requireNonNull(rawStory, "rawStory == null");

        final List<String> sentences = new ArrayList<>();

        final StringBuilder part = new StringBuilder();

        final int rawStoryLength = rawStory.length();
        for (int i = 0; i < rawStoryLength; i++) {
            final char ch = rawStory.charAt(i);

            part.append(ch);
            if (isSentenceSeparator(ch)) {
                sentences.add(part.toString());
                part.setLength(0);
            }
        }
        if (part.length() > 0) {
            sentences.add(part.toString());
        }

        return sentences;
    }

    private StoryTextUtils() {
    }
}
