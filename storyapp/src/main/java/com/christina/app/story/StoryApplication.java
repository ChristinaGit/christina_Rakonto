package com.christina.app.story;

import android.app.Application;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import lombok.experimental.Accessors;

import io.realm.Realm;

import com.christina.app.story.core.manager.file.AndroidStoryFileManager;
import com.christina.app.story.di.StoryApplicationComponentProvider;
import com.christina.app.story.di.storyApplication.DaggerStoryApplicationComponent;
import com.christina.app.story.di.storyApplication.StoryApplicationComponent;
import com.christina.app.story.di.storyApplication.module.StoryApplicationManagerModule;
import com.christina.app.story.di.storyApplication.module.StoryApplicationRealmModule;

/**
 * TODO:
 * <ul>
 * <li>Animate FAB;</li>
 * <li>Implement two-phased loads;</li>
 * <li>Implement card view loading;</li>
 * <li>Implement stories list position restoring;</li>
 * <li>Hide FAB on scroll;</li>
 * <li>Fix fragment state;</li>
 * <li>Add sorting;</li>
 * <li>Add custom provider permissions;</li>
 * <li>Convert layouts to ConstraintLayout;</li>
 * <li>Implement Leave-behinds;</li>
 * <li>Fix landscape nav bar;</li>
 * <li>Add locale for stories;</li>
 * </ul>
 */
@Accessors(prefix = "_")
public final class StoryApplication extends Application
    implements StoryApplicationComponentProvider {
    @NonNull
    @Override
    public final StoryApplicationComponent getStoryApplicationComponent() {
        if (_storyApplicationComponent == null) {
            throw new IllegalStateException("The application has not yet been created.");
        }

        return _storyApplicationComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        enableStrictMode(false);

        Realm.init(getApplicationContext());

        _storyApplicationComponent = DaggerStoryApplicationComponent
            .builder()
            .storyApplicationRealmModule(new StoryApplicationRealmModule())
            .storyApplicationManagerModule(new StoryApplicationManagerModule(new AndroidStoryFileManager(
                getFilesDir())))
            .build();

        getStoryApplicationComponent().getFakeStoryDatabase().create();
    }

    @Nullable
    private StoryApplicationComponent _storyApplicationComponent;

    private void enableStrictMode(final boolean enable) {
        if (enable) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                                           .detectAll()
                                           .penaltyLog()
                                           .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                                       .detectLeakedSqlLiteObjects()
                                       .detectLeakedClosableObjects()
                                       .penaltyLog()
                                       .penaltyDeath()
                                       .build());
        }
    }
}
