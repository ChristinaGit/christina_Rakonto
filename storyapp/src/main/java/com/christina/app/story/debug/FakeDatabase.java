package com.christina.app.story.debug;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.christina.api.story.dao.story.StoryDao;
import com.christina.api.story.dao.storyFrame.StoryFrameDao;
import com.christina.api.story.model.Story;
import com.christina.api.story.model.StoryFrame;
import com.christina.app.story.core.StoryTextUtils;
import com.christina.common.UriSchemes;
import com.christina.common.UriUtils;
import com.christina.content.story.StoryDatabase;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import lombok.val;

public class FakeDatabase {
    public static final int STORY_COUNT = 25;

    private static final String[] NET_IMAGES = {
        "http://orig04.deviantart.net/da1d/f/2016/142/b/4/untitled_by_lerastyajkina-da3ckp4.png",
        "http://img05.deviantart.net/2d66/i/2015/117/d/1/bloodborne_by_xaimn-d8r8h7u.png",
        "http://pre00.deviantart.net/f00e/th/pre/i/2016/005/8/e/162_365_bloodborne_4_by_snatti89" +
        "-d9mv2qy.png",
        "http://img12.deviantart.net/63ea/i/2015/109/9/7/bloodborne_by_ned_rogers-d7pokca.jpg",
        "http://pre13.deviantart" +
        ".net/7e2c/th/pre/i/2014/179/e/3/bloodborne_concept_art_by_grobelski-d7obo3v.jpg",
        "http://orig09.deviantart" +
        ".net/1900/f/2016/013/3/0/ameliamaria1screen2blurpix2sml_by_banished_shadow-d9ns24h.png"};

    private static final String[] WORDS =
        ("The easiest way to get started with Google Custom Search is to create a basic search" +
         " engine using the Control Panel. You can then download the engine's XML files " +
         "and modify them to add futher customizations. Since you're experimenting and " +
         "figuring out some basic concepts, spend only a couple of minutes making your " +
         "first search engine. Keep it simple so that you can follow what's happening " +
         "when you start testing it. You can always change it later.")
            .replaceAll("[^a-zA-Z]*", " ")
            .split(" ");

    public FakeDatabase(
        @NonNull final StoryDao storyDao,
        @NonNull final StoryFrameDao storyFrameDao,
        final boolean networkAvailable) {
        _storyDao = storyDao;
        _storyFrameDao = storyFrameDao;
        if (networkAvailable) {
            _images = Arrays.asList(NET_IMAGES);
        } else {
            final File picturesDirectory =
                new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_PICTURES);
            final File[] pictures = picturesDirectory.listFiles(new FileFilter() {
                @Override
                public boolean accept(final File pathname) {
                    return pathname.isFile();
                }
            });

            _images = new ArrayList<>();
            CollectionUtils.collect(Arrays.asList(pictures), new Transformer<File, String>() {
                @Override
                public String transform(final File input) {
                    return UriSchemes.FILE.getSchemeName() +
                           UriUtils.SCHEMA_SEPARATOR +
                           input.getAbsolutePath();
                }
            }, _images);
        }
    }

    public void create(final Context context) {
        if (!Arrays.asList(context.databaseList()).contains(StoryDatabase.NAME)) {
            createStories(new Random(2));
        }
    }

    private final List<String> _images;

    @NonNull
    private final StoryDao _storyDao;

    @NonNull
    private final StoryFrameDao _storyFrameDao;

    private void createStories(final Random random) {
        final val storyDao = _storyDao;

        for (int i = 0; i < STORY_COUNT; i++) {
            final val story = new Story();
            story.setCreateDate(System.currentTimeMillis());
            story.setModifyDate(System.currentTimeMillis());
            story.setName(getSentence(random, 2, 5, false));
            story.setText(getSentence(random, 5, 20, true));
            story.setPreviewUri(getImage(random));

            storyDao.insert(story);

            createStoryFrames(random, story);
        }
    }

    private void createStoryFrames(final Random random, final Story story) {
        final val storyFrameDao = _storyFrameDao;

        final String storyText = story.getText();

        if (storyText != null) {
            final val defaultSplit = StoryTextUtils.defaultSplit(storyText);

            int startPosition = 0;
            int endPosition = 0;
            for (final val textFrame : defaultSplit) {
                startPosition += endPosition;
                endPosition += textFrame.length();

                final val storyFrame = new StoryFrame();
                storyFrame.setStoryId(story.getId());
                storyFrame.setImageUri(getImage(random));

                storyFrame.setTextStartPosition(startPosition);
                storyFrame.setTextEndPosition(endPosition);

                storyFrameDao.insert(storyFrame);
            }
        }
    }

    private Uri getImage(final Random random) {
        return Uri.parse(_images.get(random.nextInt(_images.size() - 1)));
    }

    private String getSentence(
        final Random random,
        final int minWordCount,
        final int maxWordCount,
        final boolean addPeriod) {
        final int wordCount = minWordCount + random.nextInt(maxWordCount - minWordCount);

        String result = "";
        for (int i = 0; i < wordCount; i++) {
            result += getWord(random) + " ";
        }
        result = StringUtils.capitalize(result.trim());
        if (addPeriod) {
            result += ".";
        }
        result = result.replaceAll("\\s", " ");
        return result;
    }

    private String getWord(final Random random) {
        return WORDS[random.nextInt(WORDS.length - 1)].trim();
    }
}
