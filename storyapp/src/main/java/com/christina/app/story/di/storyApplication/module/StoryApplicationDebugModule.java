package com.christina.app.story.di.storyApplication.module;

import android.support.annotation.NonNull;

import io.realm.RealmConfiguration;

import com.christina.app.story.core.manager.file.StoryFileManager;
import com.christina.app.story.debug.FakeStoryDatabase;
import com.christina.app.story.di.storyApplication.StoryApplicationScope;
import com.christina.common.contract.Contracts;
import com.christina.common.data.realm.RealmIdGenerator;

import dagger.Module;
import dagger.Provides;

@Module
@StoryApplicationScope
public final class StoryApplicationDebugModule {
    @Provides
    @StoryApplicationScope
    @NonNull
    public final FakeStoryDatabase provideFakeStoryDatabase(
        @NonNull final RealmConfiguration realmConfiguration,
        @NonNull final RealmIdGenerator realmIdGenerator,
        @NonNull final StoryFileManager storyFileManager) {
        Contracts.requireNonNull(realmConfiguration, "realmConfiguration == null");
        Contracts.requireNonNull(realmIdGenerator, "realmIdGenerator == null");
        Contracts.requireNonNull(storyFileManager, "storyFileManager == null");

        return new FakeStoryDatabase(realmConfiguration, realmIdGenerator, storyFileManager, false);
    }
}
