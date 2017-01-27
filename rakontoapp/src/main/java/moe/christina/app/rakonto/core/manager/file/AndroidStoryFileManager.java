package moe.christina.app.rakonto.core.manager.file;

import android.support.annotation.NonNull;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;

import io.realm.RealmModel;

import moe.christina.app.rakonto.model.Story;
import moe.christina.app.rakonto.model.StoryFrame;
import moe.christina.common.contract.Contracts;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Accessors(prefix = "_")
public final class AndroidStoryFileManager implements StoryFileManager {
    private static final String MODEL_FILES_DIRECTORY_NAME = "stories.files";

    private static final String FILE_NAME_ID_SEPARATOR = "_";

    public AndroidStoryFileManager(@NonNull final File rootDirectory) {
        Contracts.requireNonNull(rootDirectory, "rootDirectory == null");

        _rootDirectory = rootDirectory;
    }

    @NonNull
    @Override
    public File getAssociatedFile(
        @NonNull final Class<? extends RealmModel> modelClass,
        final long id,
        @NonNull final String name) {
        Contracts.requireNonNull(modelClass, "modelClass == null");
        Contracts.requireNonNull(name, "name == null");

        return new File(getModelClassDirectory(modelClass), getAssociatedFileName(id, name));
    }

    @NonNull
    @Override
    public Runnable getDeleteAllAssociatedFilesTask() {
        return new Runnable() {
            @Override
            public void run() {
                FileUtils.deleteQuietly(getModelFilesDirectory());
            }
        };
    }

    @NonNull
    @Override
    public Runnable getDeleteAssociatedFileTask(
        @NonNull final Story story, @NonNull final String name) {
        Contracts.requireNonNull(story, "story == null");
        Contracts.requireNonNull(name, "name == null");

        return getDeleteFileTask(getAssociatedFile(story, name));
    }

    @NonNull
    @Override
    public Runnable getDeleteAssociatedFileTask(
        @NonNull final StoryFrame storyFrame, @NonNull final String name) {
        Contracts.requireNonNull(storyFrame, "storyFrame == null");
        Contracts.requireNonNull(name, "name == null");

        return getDeleteFileTask(getAssociatedFile(storyFrame, name));
    }

    @NonNull
    @Override
    public Runnable getDeleteAssociatedFilesTask(
        @NonNull final Story story, final boolean deep) {
        Contracts.requireNonNull(story, "story == null");

        return getDeleteFilesTask(getAssociatedFiles(story, deep));
    }

    @NonNull
    @Override
    public Runnable getDeleteAssociatedFilesTask(
        @NonNull final StoryFrame storyFrame, final boolean deep) {
        Contracts.requireNonNull(storyFrame, "storyFrame == null");

        return getDeleteFilesTask(getAssociatedFiles(storyFrame, deep));
    }

    @NonNull
    protected File getAssociatedFile(
        @NonNull final StoryFrame storyFrame, @NonNull final String name) {
        Contracts.requireNonNull(storyFrame, "storyFrame == null");
        Contracts.requireNonNull(name, "name == null");

        return getAssociatedFile(storyFrame.getClass(), storyFrame.getId(), name);
    }

    @NonNull
    protected File getAssociatedFile(
        @NonNull final Story story, @NonNull final String name) {
        Contracts.requireNonNull(story, "story == null");
        Contracts.requireNonNull(name, "name == null");

        return getAssociatedFile(story.getClass(), story.getId(), name);
    }

    @NonNull
    protected String getAssociatedFileIdPrefix(final long id) {
        return id + FILE_NAME_ID_SEPARATOR;
    }

    @NonNull
    protected String getAssociatedFileName(
        final long id, @NonNull final String name) {
        Contracts.requireNonNull(name, "name == null");

        return getAssociatedFileIdPrefix(id) + name;
    }

    @NonNull
    protected List<File> getAssociatedFiles(
        @NonNull final Class<? extends RealmModel> modelClass, final long id) {
        Contracts.requireNonNull(modelClass, "modelClass == null");

        final List<File> associatedFiles;

        final val associatedFileIdPrefix = getAssociatedFileIdPrefix(id);

        final val files = getModelClassDirectory(modelClass).listFiles(new FilenameFilter() {
            @Override
            public boolean accept(final File dir, final String name) {
                return name.startsWith(associatedFileIdPrefix);
            }
        });

        if (files != null && files.length > 0) {
            associatedFiles = Arrays.asList(files);
        } else {
            associatedFiles = Collections.emptyList();
        }

        return associatedFiles;
    }

    @NonNull
    protected List<File> getAssociatedFiles(
        @NonNull final Story story, final boolean deep) {
        Contracts.requireNonNull(story, "story == null");

        final val associatedFiles = new ArrayList<File>();

        associatedFiles.addAll(getAssociatedFiles(story.getClass(), story.getId()));

        if (deep) {
            for (final val storyFrame : story.getStoryFrames()) {
                associatedFiles.addAll(getAssociatedFiles(storyFrame, deep));
            }
        }

        return associatedFiles;
    }

    @NonNull
    protected List<File> getAssociatedFiles(
        @NonNull final StoryFrame storyFrame, final boolean deep) {
        Contracts.requireNonNull(storyFrame, "storyFrame == null");

        final val associatedFiles = new ArrayList<File>();

        associatedFiles.addAll(getAssociatedFiles(storyFrame.getClass(), storyFrame.getId()));

        return associatedFiles;
    }

    @NonNull
    protected Runnable getDeleteFileTask(@NonNull final File file) {
        Contracts.requireNonNull(file, "file == null");

        return new Runnable() {
            @Override
            public void run() {
                FileUtils.deleteQuietly(file);
            }
        };
    }

    @NonNull
    protected Runnable getDeleteFilesTask(@NonNull final Iterable<File> files) {
        Contracts.requireNonNull(files, "files == null");

        return new Runnable() {
            @Override
            public void run() {
                for (final val file : files) {
                    FileUtils.deleteQuietly(file);
                }
            }
        };
    }

    @NonNull
    protected File getModelClassDirectory(@NonNull final Class<? extends RealmModel> modelClass) {
        Contracts.requireNonNull(modelClass, "modelClass == null");

        return new File(getModelFilesDirectory(), getModelClassDirectoryName(modelClass));
    }

    @NonNull
    protected String getModelClassDirectoryName(
        @NonNull final Class<? extends RealmModel> modelClass) {
        Contracts.requireNonNull(modelClass, "modelClass == null");

        return modelClass.getSimpleName().toLowerCase(Locale.ENGLISH);
    }

    @NonNull
    protected File getModelFilesDirectory() {
        return new File(getRootDirectory(), MODEL_FILES_DIRECTORY_NAME);
    }

    @Getter(AccessLevel.PROTECTED)
    @NonNull
    private final File _rootDirectory;
}
