package com.christina.app.story.di.storyApplication.module;

import android.content.Context;
import android.support.annotation.NonNull;

import com.christina.app.story.di.qualifier.ForApplication;
import com.christina.app.story.di.storyApplication.StoryApplicationScope;
import com.christina.common.contract.Contracts;

import dagger.Module;
import dagger.Provides;

@Module
@StoryApplicationScope
public final class ApplicationContextModule {
    public ApplicationContextModule(@NonNull final Context applicationContext) {
        Contracts.requireNonNull(applicationContext, "applicationContext == null");

        _applicationContext = applicationContext;
    }

    @Provides
    @StoryApplicationScope
    @ForApplication
    @NonNull
    public final Context provideApplicationContext() {
        return _applicationContext;
    }

    @NonNull
    private final Context _applicationContext;
}
