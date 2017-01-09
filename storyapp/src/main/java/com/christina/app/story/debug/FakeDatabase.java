package com.christina.app.story.debug;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.christina.api.story.dao.story.StoryDao;
import com.christina.api.story.dao.storyFrame.StoryFrameDao;
import com.christina.api.story.model.Story;
import com.christina.api.story.model.StoryFrame;
import com.christina.app.story.core.StoryTextUtils;
import com.christina.common.ConstantBuilder;
import com.christina.common.UriScheme;
import com.christina.common.contract.Contracts;
import com.christina.common.utility.UriUtils;
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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;

@Accessors(prefix = "_")
public final class FakeDatabase {
    private static final String _LOG_TAG = ConstantBuilder.logTag(FakeDatabase.class);

    public static final int STORY_COUNT = 55;

    public static final int RANDOM_SEED = 11;

    public static final int STORY_NAME_MIN_WORD_COUNT = 2;

    public static final int STORY_NAME_MAX_WORD_COUNT = 5;

    public static final int STORY_NAME_MIN_TEXT_COUNT = 5;

    public static final int STORY_NAME_MAX_TEXT_COUNT = 20;

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
        ("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed placerat elit et nulla " +
         "aliquam, et dapibus dolor efficitur. In risus metus, rutrum sit amet convallis ut, " +
         "hendrerit sed purus. Vestibulum facilisis, augue id vulputate accumsan, nunc augue " +
         "mattis risus, a sagittis enim elit non massa. Aenean sit amet sapien vitae massa " +
         "dapibus elementum viverra eget nisi. Praesent non dolor vel risus interdum cursus. " +
         "Donec auctor sem ipsum, quis semper nisi vulputate sed. Integer nec justo a dui laoreet" +
         " facilisis. Nam eget ex eu turpis tristique dictum. Duis interdum enim sit amet lacinia" +
         " bibendum. Curabitur neque augue, condimentum ut tortor vel, ullamcorper pulvinar est. " +
         "Nulla convallis turpis id mi mattis consectetur. Vestibulum ante ipsum primis in " +
         "faucibus orci luctus et ultrices posuere cubilia Curae;" +
         "Mauris eu vestibulum nibh. Maecenas ac augue luctus, semper purus ac, condimentum erat." +
         " Duis quis posuere quam. Ut in nunc aliquet, fermentum risus mollis, vulputate augue. " +
         "Nulla id arcu at velit convallis maximus. Sed eu lorem non sem faucibus venenatis. Duis" +
         " elit enim, commodo a lectus nec, volutpat vestibulum nisi." +
         "Duis congue quis tortor quis viverra. In molestie convallis maximus. Curabitur in elit " +
         "massa. Integer sit amet tincidunt libero. Nam libero nisi, feugiat nec molestie a, " +
         "ultrices ac velit. Aliquam sapien leo, auctor ultrices leo non, cursus facilisis leo. " +
         "Aliquam enim ante, consectetur nec diam sed, finibus finibus arcu. Proin velit turpis, " +
         "commodo sed dolor at, scelerisque efficitur est. Duis massa velit, vehicula ac elit eu," +
         " dictum volutpat lacus." +
         "Ut in accumsan est. Donec et mi ornare, fermentum lorem condimentum, dictum nibh. " +
         "Aenean viverra mi sed suscipit accumsan. Donec sit amet sapien nec arcu porttitor " +
         "bibendum posuere euismod augue. Cum sociis natoque penatibus et magnis dis parturient " +
         "montes, nascetur ridiculus mus. Ut id velit arcu. Nulla auctor aliquet lectus, eget " +
         "volutpat justo viverra lobortis. Quisque nec tortor vitae ante gravida pellentesque at " +
         "eleifend orci. Vivamus vehicula pulvinar dolor, sit amet auctor tellus elementum at. " +
         "Morbi et nulla pretium, congue ex mattis, sagittis tellus. Fusce nulla metus, semper " +
         "sed velit tristique, euismod mollis ipsum. Nulla eleifend orci a mauris accumsan, a " +
         "commodo justo posuere. Nulla pretium leo erat, tincidunt vestibulum metus auctor sit " +
         "amet. Cras dignissim non tellus vel laoreet." +
         "Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. " +
         "Aenean maximus turpis vitae interdum sodales. Quisque sit amet malesuada lacus. Sed " +
         "consequat, tellus a mattis vehicula, eros orci elementum est, sed fringilla diam tellus" +
         " nec tortor. Ut orci risus, sodales vel sapien ut, lacinia pellentesque massa. Aliquam " +
         "urna nisi, rhoncus a lobortis eu, elementum vitae tortor. Sed in felis lacus. Quisque " +
         "ac blandit ex. Proin feugiat maximus augue, vel varius libero. Nam ullamcorper ligula " +
         "id pellentesque rutrum. Curabitur imperdiet ultricies aliquet. Sed porta consectetur " +
         "posuere. Integer et massa venenatis, tempor lectus vel, lobortis nisi.")
            .replaceAll("[^a-zA-Z]+", " ")
            .split(" ");

    public FakeDatabase(
        @NonNull final StoryDao storyDao,
        @NonNull final StoryFrameDao storyFrameDao,
        final boolean networkAvailable) {
        Contracts.requireNonNull(storyDao, "storyDao == null");
        Contracts.requireNonNull(storyFrameDao, "storyFrameDao == null");

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
                    return UriScheme.FILE.getSchemeName() +
                           UriUtils.SCHEMA_SEPARATOR +
                           input.getAbsolutePath();
                }
            }, _images);
        }
    }

    public void create(@NonNull final Context context) {
        Contracts.requireNonNull(context, "context == null");

        if (!Arrays.asList(context.databaseList()).contains(StoryDatabase.NAME)) {
            createStories(new Random(RANDOM_SEED));
        }
    }

    @Getter(AccessLevel.PRIVATE)
    private final List<String> _images;

    @Getter(AccessLevel.PRIVATE)
    @NonNull
    private final StoryDao _storyDao;

    @Getter(AccessLevel.PRIVATE)
    @NonNull
    private final StoryFrameDao _storyFrameDao;

    private void createStories(@NonNull final Random random) {
        Contracts.requireNonNull(random, "random == null");

        final val storyDao = getStoryDao();

        final long createDate = System.currentTimeMillis();

        for (int i = 0; i < STORY_COUNT; i++) {
            final val story = new Story();
            story.setCreateDate(createDate);
            story.setModifyDate(createDate);
            story.setName(getSentence(random,
                                      STORY_NAME_MIN_WORD_COUNT,
                                      STORY_NAME_MAX_WORD_COUNT,
                                      false));
            story.setText(getSentence(random,
                                      STORY_NAME_MIN_TEXT_COUNT,
                                      STORY_NAME_MAX_TEXT_COUNT,
                                      true));
            story.setPreviewUri(getImage(random));

            if (storyDao.insert(story) != Story.NO_ID) {
                Log.d(_LOG_TAG, "Inserted: " + story);
            } else {
                Log.d(_LOG_TAG, "Failed to insert: " + story);
            }

            createStoryFrames(random, story);
        }
    }

    private void createStoryFrames(@NonNull final Random random, @NonNull final Story story) {
        Contracts.requireNonNull(random, "random == null");
        Contracts.requireNonNull(story, "story == null");

        final val storyFrameDao = getStoryFrameDao();

        final val storyText = story.getText();

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

                if (storyFrameDao.insert(storyFrame) != StoryFrame.NO_ID) {
                    Log.d(_LOG_TAG, "Inserted: " + storyFrame);
                } else {
                    Log.d(_LOG_TAG, "Failed to insert: " + storyFrame);
                }
            }
        }
    }

    @NonNull
    private Uri getImage(@NonNull final Random random) {
        Contracts.requireNonNull(random, "random == null");

        final val images = getImages();
        return Uri.parse(images.get(random.nextInt(images.size() - 1)));
    }

    @NonNull
    private String getSentence(
        @NonNull final Random random,
        final int minWordCount,
        final int maxWordCount,
        final boolean addPeriod) {
        Contracts.requireNonNull(random, "random == null");
        Contracts.require(minWordCount < maxWordCount);

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

    @NonNull
    private String getWord(@NonNull final Random random) {
        Contracts.requireNonNull(random, "random == null");

        return WORDS[random.nextInt(WORDS.length - 1)].trim();
    }
}
