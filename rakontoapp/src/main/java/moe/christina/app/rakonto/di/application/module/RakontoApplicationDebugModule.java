package moe.christina.app.rakonto.di.application.module;

import android.support.annotation.NonNull;

import io.realm.RealmConfiguration;

import moe.christina.app.rakonto.core.debug.FakeStoryDatabase;
import moe.christina.app.rakonto.core.manager.file.StoryFileManager;
import moe.christina.app.rakonto.di.application.RakontoApplicationScope;
import moe.christina.common.contract.Contracts;
import moe.christina.common.data.realm.RealmIdGenerator;

import dagger.Module;
import dagger.Provides;

@Module
@RakontoApplicationScope
public final class RakontoApplicationDebugModule {
    @Provides
    @RakontoApplicationScope
    @NonNull
    public final FakeStoryDatabase provideFakeStoryDatabase(
        @NonNull final RealmConfiguration realmConfiguration,
        @NonNull final RealmIdGenerator realmIdGenerator,
        @NonNull final StoryFileManager storyFileManager) {
        Contracts.requireNonNull(realmConfiguration, "realmConfiguration == null");
        Contracts.requireNonNull(realmIdGenerator, "realmIdGenerator == null");
        Contracts.requireNonNull(storyFileManager, "storyFileManager == null");

        return new FakeStoryDatabase(realmConfiguration,
                                     realmIdGenerator,
                                     storyFileManager,
                                     false,
                                     false);
    }
}
