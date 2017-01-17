package com.christina.app.story.di.storyApplication.module;

import android.support.annotation.NonNull;

import lombok.val;

import io.realm.Realm;
import io.realm.RealmConfiguration;

import com.christina.app.story.di.storyApplication.StoryApplicationScope;
import com.christina.common.contract.Contracts;
import com.christina.common.data.realm.RealmIdGenerator;

import dagger.Module;
import dagger.Provides;

@Module
@StoryApplicationScope
public final class StoryApplicationRealmModule {
    @Provides
    @StoryApplicationScope
    @NonNull
    public final RealmConfiguration provideRealmConfiguration() {
        return new RealmConfiguration.Builder().name("stories.realm").schemaVersion(1).build();
    }

    @Provides
    @StoryApplicationScope
    @NonNull
    public final RealmIdGenerator provideRealmIdGenerator(
        @NonNull final RealmConfiguration realmConfiguration) {
        Contracts.requireNonNull(realmConfiguration, "realmConfiguration == null");

        final val realmIdGenerator = new RealmIdGenerator();
        try (final val realm = Realm.getInstance(realmConfiguration)) {
            realmIdGenerator.initialize(realm);
        }
        return realmIdGenerator;
    }
}
