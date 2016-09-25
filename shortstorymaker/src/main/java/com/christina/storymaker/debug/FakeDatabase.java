package com.christina.storymaker.debug;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.christina.content.story.dao.StoryDao;
import com.christina.content.story.dao.StoryDaoManager;
import com.christina.content.story.dao.StoryFrameDao;
import com.christina.content.story.database.StoryDatabase;
import com.christina.content.story.model.Story;
import com.christina.content.story.model.StoryFrame;

import java.util.Arrays;
import java.util.Random;

public class FakeDatabase {
    public static final int STORY_COUNT = 5;

    private static final String LOG_TAG = FakeDatabase.class.getSimpleName();

    private static final String[] _images = {
        "http://orig04.deviantart.net/da1d/f/2016/142/b/4/untitled_by_lerastyajkina-da3ckp4.png",
        "http://img05.deviantart.net/2d66/i/2015/117/d/1/bloodborne_by_xaimn-d8r8h7u.png",
        "http://pre00.deviantart.net/f00e/th/pre/i/2016/005/8/e/162_365_bloodborne_4_by_snatti89" +
        "-d9mv2qy.png",
        "http://img12.deviantart.net/63ea/i/2015/109/9/7/bloodborne_by_ned_rogers-d7pokca.jpg",
        "http://pre13.deviantart" +
        ".net/7e2c/th/pre/i/2014/179/e/3/bloodborne_concept_art_by_grobelski-d7obo3v.jpg",
        "http://orig09.deviantart" +
        ".net/1900/f/2016/013/3/0/ameliamaria1screen2blurpix2sml_by_banished_shadow-d9ns24h.png"};

    public FakeDatabase() {
        _words = ("The easiest way to get started with Google Custom Search is to create a basic " +
                  "search" +
                  " engine using the Control Panel. You can then download the engine's XML files " +
                  "and modify them to add futher customizations. Since you're experimenting and " +
                  "figuring out some basic concepts, spend only a couple of minutes making your " +
                  "first search engine. Keep it simple so that you can follow what's happening " +
                  "when" +
                  " you start testing it. You can always change it later.").replaceAll("[^a-zA-Z]",
                                                                                       " ")
                                                                           .split(" ");

        Log.d(LOG_TAG, "FakeDatabase " + Arrays.toString(_words));
    }

    public void create(final Context context) {
        if (!Arrays.asList(context.databaseList()).contains(StoryDatabase.NAME)) {
            createStories(new Random(0));
        }
    }

    private final String[] _words;

    private void createStories(final Random random) {
        final StoryDao storyDao = StoryDaoManager.getStoryDao();

        for (int i = 0; i < STORY_COUNT; i++) {
            final Story story = storyDao.create();
            if (story != null) {
                story.setCreateDate();
                story.setModifyDate();
                story.setName(getSentence(random, 2, 5));
                story.setText(getSentence(random, 5,  20));
                story.setPreviewUri(getImage(random));

                storyDao.update(story);

                createStoryFrames(random, story);
            }
        }
    }

    private void createStoryFrames(final Random random, final Story story) {
        final StoryFrameDao storyFrameDao = StoryDaoManager.getStoryFrameDao();

        final String storyText = story.getText();

        for (int i = 0; i < storyText.length(); i += random.nextInt(7)) {
            final StoryFrame storyFrame = storyFrameDao.create();
            if (storyFrame != null) {
                storyFrame.setStoryId(story.getId());
                storyFrame.setImageUri(getImage(random));
                storyFrame.setTextPosition(i);

                storyFrameDao.update(storyFrame);
            }
        }
    }

    private Uri getImage(final Random random) {
        return Uri.parse(_images[random.nextInt(_images.length - 1)]);
    }

    private String getSentence(final Random random, final int minWordCount,
                               final int maxWordCount) {
        final int wordCount = minWordCount + random.nextInt(maxWordCount - minWordCount);

        String result = "";
        for (int i = 0; i < wordCount; i++) {
            result += getWord(random) + " ";
        }

        return result.trim();
    }

    private String getWord(final Random random) {
        return _words[random.nextInt(_words.length - 1)].trim();
    }
}
