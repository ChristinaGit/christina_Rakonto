package com.christina.app.story.di.storyApplication.module;

import android.content.ContentResolver;
import android.content.Context;
import android.support.annotation.NonNull;

import com.christina.app.story.di.qualifier.ForApplication;
import com.christina.app.story.di.storyApplication.StoryApplicationScope;
import com.christina.common.contract.Contracts;

import dagger.Module;
import dagger.Provides;

@Module
@StoryApplicationScope
public final class ApplicationServiceModule {
    @Provides
    @StoryApplicationScope
    @NonNull
    public final ContentResolver provideContentResolver(
        @ForApplication @NonNull final Context context) {
        Contracts.requireNonNull(context, "context == null");

        return context.getContentResolver();
    }
}
