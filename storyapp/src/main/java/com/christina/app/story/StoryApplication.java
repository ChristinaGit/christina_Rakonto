package com.christina.app.story;

import android.app.Application;
import android.os.StrictMode;
import android.support.annotation.NonNull;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;

import io.realm.Realm;

import com.christina.app.story.core.RealmIdGenerator;
import com.christina.app.story.core.manager.data.AndroidRealmManger;
import com.christina.app.story.core.manager.resource.ResourceManager;
import com.christina.app.story.debug.FakeDatabase;
import com.christina.app.story.di.StoryApplicationComponentProvider;
import com.christina.app.story.di.storyApplication.DaggerStoryApplicationComponent;
import com.christina.app.story.di.storyApplication.StoryApplicationComponent;
import com.christina.app.story.di.storyApplication.module.StoryApplicationRealmModule;
import com.christina.common.event.Events;
import com.christina.common.event.notice.NoticeEvent;

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
 * </ul>
 */
@Accessors(prefix = "_")
public final class StoryApplication extends Application
    implements StoryApplicationComponentProvider {
    @Override
    public void onCreate() {
        super.onCreate();

        enableStrictMode(false);

        Realm.init(getApplicationContext());

        // TODO: 1/11/2017 Remove debug database.
        createDebugDatabase();
    }

    @NonNull
    @Getter(value = AccessLevel.PRIVATE)
    private final RealmIdGenerator _realmIdGenerator = new RealmIdGenerator();

    @NonNull
    @Getter(onMethod = @__(@Override))
    private final StoryApplicationComponent _storyApplicationComponent =
        DaggerStoryApplicationComponent
            .builder()
            .storyApplicationRealmModule(new StoryApplicationRealmModule(getRealmIdGenerator()))
            .build();

    private void createDebugDatabase() {
        final val realmConfiguration = new AndroidRealmManger(new ResourceManager() {
            @NonNull
            @Override
            public NoticeEvent getAcquireResourcesEvent() {
                return Events.createNoticeEvent();
            }

            @NonNull
            @Override
            public NoticeEvent getReleaseResourcesEvent() {
                return Events.createNoticeEvent();
            }
        }, getRealmIdGenerator()).getRealmConfiguration();

        try (final val realm = Realm.getInstance(realmConfiguration)) {
            getRealmIdGenerator().initialize(realm);
        }

        new FakeDatabase(Realm.getInstance(realmConfiguration),
                         getRealmIdGenerator(),
                         false).create();
    }

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
