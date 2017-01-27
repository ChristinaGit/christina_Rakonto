package moe.christina.app.rakonto.core.manager.file;

import android.support.annotation.NonNull;

import io.realm.RealmModel;

import moe.christina.app.rakonto.model.Story;
import moe.christina.app.rakonto.model.StoryFrame;

import java.io.File;

public interface StoryFileManager {
    @NonNull
    File getAssociatedFile(
        @NonNull final Class<? extends RealmModel> modelClass,
        final long id,
        @NonNull final String name);

    @NonNull
    Runnable getDeleteAllAssociatedFilesTask();

    @NonNull
    Runnable getDeleteAssociatedFileTask(@NonNull Story story, @NonNull String name);

    @NonNull
    Runnable getDeleteAssociatedFileTask(@NonNull StoryFrame storyFrame, @NonNull String name);

    @NonNull
    Runnable getDeleteAssociatedFilesTask(@NonNull Story story, boolean deep);

    @NonNull
    Runnable getDeleteAssociatedFilesTask(@NonNull StoryFrame storyFrame, boolean deep);
}
