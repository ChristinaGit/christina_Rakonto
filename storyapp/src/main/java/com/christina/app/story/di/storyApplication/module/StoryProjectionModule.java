package com.christina.app.story.di.storyApplication.module;

import android.support.annotation.NonNull;

import com.christina.api.story.dao.story.StoryFullProjection;
import com.christina.api.story.dao.storyFrame.StoryFrameFullProjection;
import com.christina.app.story.di.storyApplication.StoryApplicationScope;

import dagger.Module;
import dagger.Provides;

@Module
@StoryApplicationScope
public final class StoryProjectionModule {
    @Provides
    @StoryApplicationScope
    @NonNull
    public final StoryFrameFullProjection provideStoryFrameFullProjection() {
        return new StoryFrameFullProjection();
    }

    @Provides
    @StoryApplicationScope
    @NonNull
    public final StoryFullProjection provideStoryFullProjection() {
        return new StoryFullProjection();
    }
}
