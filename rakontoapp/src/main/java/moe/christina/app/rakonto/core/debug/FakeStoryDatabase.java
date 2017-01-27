package moe.christina.app.rakonto.core.debug;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;

import io.realm.Realm;
import io.realm.RealmConfiguration;

import moe.christina.app.rakonto.core.manager.file.StoryFileManager;
import moe.christina.app.rakonto.core.utility.StoryTextUtils;
import moe.christina.app.rakonto.model.Story;
import moe.christina.app.rakonto.model.StoryFrame;
import moe.christina.common.ConstantBuilder;
import moe.christina.common.contract.Contracts;
import moe.christina.common.data.realm.RealmIdGenerator;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@Accessors(prefix = "_")
public final class FakeStoryDatabase {
    private static final String _LOG_TAG = ConstantBuilder.logTag(FakeStoryDatabase.class);

    public static final int STORY_COUNT = 75;

    public static final int RANDOM_SEED = 231;

    public static final int CHANCE_BAD_STORY_PREVIEW = 25;

    public static final int CHANCE_EMPTY_STORY_NAME = 35;

    public static final int CHANCE_EMPTY_STORY_TEXT = 25;

    public static final int STORY_NAME_MIN_WORD_COUNT = 2;

    public static final int STORY_NAME_MAX_WORD_COUNT = 5;

    public static final int STORY_NAME_MIN_TEXT_COUNT = 5;

    public static final int STORY_NAME_MAX_TEXT_COUNT = 10;

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

    public FakeStoryDatabase(
        @NonNull final RealmConfiguration realmConfiguration,
        @NonNull final RealmIdGenerator realmIdGenerator,
        @NonNull final StoryFileManager storyFileManager,
        final boolean networkAvailable,
        final boolean allowImageFilesCompression) {
        Contracts.requireNonNull(realmConfiguration, "realmConfiguration == null");
        Contracts.requireNonNull(realmIdGenerator, "realmIdGenerator == null");
        Contracts.requireNonNull(storyFileManager, "storyFileManager == null");

        _realmConfiguration = realmConfiguration;
        _realmIdGenerator = realmIdGenerator;
        _storyFileManager = storyFileManager;
        _allowImageFilesCompression = allowImageFilesCompression;
        if (networkAvailable) {
            _images = Arrays.asList(NET_IMAGES);
        } else {
            final val picturesDirectory =
                new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_PICTURES);
            final val pictures = FileUtils.listFiles(picturesDirectory,
                                                     new String[]{"png", "jpg", "bmp", "jpeg"},
                                                     true);

            _images = new ArrayList<>(pictures.size());

            for (final val picture : pictures) {
                _images.add(picture.getAbsolutePath());
            }
        }
    }

    public void create() {
        try (final val realm = Realm.getInstance(getRealmConfiguration())) {
            if (realm.isEmpty()) {
                realm.beginTransaction();
                createStories(realm, new Random(RANDOM_SEED));
                realm.commitTransaction();
            }
        }
    }

    private final boolean _allowImageFilesCompression;

    @Getter(AccessLevel.PRIVATE)
    private final List<String> _images;

    @Getter(AccessLevel.PROTECTED)
    @NonNull
    private final RealmConfiguration _realmConfiguration;

    @Getter(AccessLevel.PROTECTED)
    @NonNull
    private final RealmIdGenerator _realmIdGenerator;

    @Getter(AccessLevel.PROTECTED)
    @NonNull
    private final StoryFileManager _storyFileManager;

    private void copyImageFile(
        @NonNull final File originalImageFile, @NonNull final File imageFile)
        throws IOException {
        Contracts.requireNonNull(originalImageFile, "originalImageFile == null");
        Contracts.requireNonNull(imageFile, "imageFile == null");

        if (_allowImageFilesCompression) {
            final val imageBitmap = BitmapFactory.decodeFile(originalImageFile.getAbsolutePath());
            try (final val imageFileStream = FileUtils.openOutputStream(imageFile)) {
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 55, imageFileStream);
            } finally {
                if (imageBitmap != null && !imageBitmap.isRecycled()) {
                    imageBitmap.recycle();
                }
            }
        } else {
            FileUtils.copyFile(originalImageFile, imageFile);
        }
    }

    private void createStories(@NonNull final Realm realm, @NonNull final Random random) {
        Contracts.requireNonNull(realm, "realm == null");
        Contracts.requireNonNull(random, "random == null");

        final long createDate = System.currentTimeMillis();

        for (int i = 0; i < STORY_COUNT; i++) {
            final long id = getRealmIdGenerator().generateNextId(Story.class);
            final val story = realm.createObject(Story.class, id);
            story.setCreateDate(createDate);
            story.setModifyDate(createDate);

            final boolean emptyStoryName = random.nextInt(100) <= CHANCE_EMPTY_STORY_NAME;
            if (!emptyStoryName) {
                story.setName(getSentence(random,
                                          STORY_NAME_MIN_WORD_COUNT,
                                          STORY_NAME_MAX_WORD_COUNT,
                                          false));
            }
            final boolean emptyStoryText = random.nextInt(100) <= CHANCE_EMPTY_STORY_TEXT;
            if (!emptyStoryText) {
                story.setText(getSentence(random,
                                          STORY_NAME_MIN_TEXT_COUNT,
                                          STORY_NAME_MAX_TEXT_COUNT,
                                          true));
            }

            final val originalImageFile = new File(getImage(random));
            final val imageFile = getStoryFileManager().getAssociatedFile(Story.class,
                                                                          story.getId(),
                                                                          Story.FILE_PREVIEW);

            final boolean badStoryPreview = random.nextInt(100) <= CHANCE_BAD_STORY_PREVIEW;
            if (!badStoryPreview) {
                try {
                    copyImageFile(originalImageFile, imageFile);
                } catch (final IOException exception) {
                    Log.e(_LOG_TAG,
                          "Filed to copy file: " + originalImageFile + " to " + imageFile,
                          exception);
                }
            }

            story.setPreviewUri(imageFile.getPath());

            createStoryFrames(realm, random, story);
        }
    }

    private void createStoryFrames(
        @NonNull final Realm realm, @NonNull final Random random, @NonNull final Story story) {
        Contracts.requireNonNull(realm, "realm == null");
        Contracts.requireNonNull(random, "random == null");
        Contracts.requireNonNull(story, "story == null");

        final val storyText = story.getText();

        final val idGenerator = getRealmIdGenerator();
        final val fileManager = getStoryFileManager();

        if (storyText != null) {
            final val framesBoundaries =
                StoryTextUtils.getStoryFramesBoundaries(storyText, Locale.getDefault());

            for (final val frameBoundary : framesBoundaries) {
                final long id = idGenerator.generateNextId(StoryFrame.class);
                final val storyFrame = realm.createObject(StoryFrame.class, id);

                final val originalImageFile = new File(getImage(random));
                final val imageFile = fileManager.getAssociatedFile(StoryFrame.class,
                                                                    storyFrame.getId(),
                                                                    StoryFrame.FILE_IMAGE);

                try {
                    copyImageFile(originalImageFile, imageFile);
                } catch (final IOException exception) {
                    Log.e(_LOG_TAG,
                          "Filed to copy file: " + originalImageFile + " to " + imageFile,
                          exception);
                }

                storyFrame.setImageUri(imageFile.getPath());

                storyFrame.setTextStartPosition(frameBoundary.getStart());
                storyFrame.setTextEndPosition(frameBoundary.getEnd());

                story.getStoryFrames().add(storyFrame);
            }
        }
    }

    @NonNull
    private String getImage(@NonNull final Random random) {
        Contracts.requireNonNull(random, "random == null");

        final val images = getImages();
        return images.get(random.nextInt(images.size() - 1));
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
        result = result.trim();
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
