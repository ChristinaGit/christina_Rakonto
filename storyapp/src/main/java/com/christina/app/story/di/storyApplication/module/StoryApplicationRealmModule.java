package com.christina.app.story.di.storyApplication.module;

import android.support.annotation.NonNull;

import com.christina.app.story.core.RealmIdGenerator;
import com.christina.app.story.di.storyApplication.StoryApplicationScope;
import com.christina.common.contract.Contracts;

import dagger.Module;
import dagger.Provides;

@Module
@StoryApplicationScope
public final class StoryApplicationRealmModule {
    public StoryApplicationRealmModule(@NonNull final RealmIdGenerator realmIdGenerator) {
        Contracts.requireNonNull(realmIdGenerator, "realmIdGenerator == null");

        _realmIdGenerator = realmIdGenerator;
    }

    @Provides
    @StoryApplicationScope
    @NonNull
    public final RealmIdGenerator provideRealmIdGenerator() {
        return _realmIdGenerator;
    }

    @NonNull
    private final RealmIdGenerator _realmIdGenerator;
}
