package moe.christina.app.rakonto.core.utility;

import android.support.annotation.NonNull;

import lombok.val;

import moe.christina.app.rakonto.core.StoryFrameBoundary;
import moe.christina.common.contract.Contracts;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class StoryTextUtils {
    private static final int STORY_FRAME_MAX_FORD_COUNT = 3;

    @NonNull
    public static List<StoryFrameBoundary> getStoryFramesBoundaries(
        @NonNull final String rawStory, @NonNull Locale locale) {
        Contracts.requireNonNull(rawStory, "rawStory == null");

        final val storyFramesBoundaries = new ArrayList<StoryFrameBoundary>();

        final val sentenceIterator = BreakIterator.getSentenceInstance(locale);
        sentenceIterator.setText(rawStory);

        final val wordIterator = BreakIterator.getWordInstance(locale);
        wordIterator.setText(rawStory);

        int wordCount = 0;

        int frameStart = 0;
        int frameEnd = 0;

        int boundary = wordIterator.first();
        while (boundary != BreakIterator.DONE) {
            boundary = wordIterator.next();

            final boolean isSentenceBoundary =
                boundary != BreakIterator.DONE && sentenceIterator.isBoundary(boundary);

            if (!isSentenceBoundary && wordCount <= STORY_FRAME_MAX_FORD_COUNT) {
                if (boundary != BreakIterator.DONE) {
                    wordCount++;

                    frameEnd = boundary;
                } else {
                    if (frameStart != frameEnd) {
                        storyFramesBoundaries.add(new StoryFrameBoundary(frameStart, frameEnd));
                    }
                }
            } else {
                wordCount = 0;

                frameEnd = boundary;

                storyFramesBoundaries.add(new StoryFrameBoundary(frameStart, frameEnd));

                frameStart = boundary;
            }
        }

        return storyFramesBoundaries;
    }

    @NonNull
    public static List<String> getStoryFramesTexts(
        @NonNull final String rawStory,
        @NonNull final List<StoryFrameBoundary> storyFramesBoundaries) {
        Contracts.requireNonNull(rawStory, "rawStory == null");
        Contracts.requireNonNull(storyFramesBoundaries, "storyFramesBoundaries == null");

        final val storyFramesTexts = new ArrayList<String>();

        for (final val storyFrameBoundary : storyFramesBoundaries) {
            storyFramesTexts.add(rawStory.substring(storyFrameBoundary.getStart(),
                                                    storyFrameBoundary.getEnd()));
        }
        return storyFramesTexts;
    }

    private StoryTextUtils() {
        Contracts.unreachable();
    }
}
