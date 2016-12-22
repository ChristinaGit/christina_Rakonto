package com.christina.app.story.di.storyApplication.module;

import android.support.annotation.NonNull;

import com.christina.api.story.dao.story.StoryFullProjection;
import com.christina.api.story.dao.story.StoryModelFactory;
import com.christina.api.story.dao.storyFrame.StoryFrameFullProjection;
import com.christina.api.story.dao.storyFrame.StoryFrameModelFactory;
import com.christina.api.story.model.Story;
import com.christina.api.story.model.StoryFrame;
import com.christina.app.story.di.qualifier.StoryFactoryNames;
import com.christina.app.story.di.storyApplication.StoryApplicationScope;
import com.christina.common.contract.Contracts;
import com.christina.common.data.dao.factory.ClassModelCollectionFactory;
import com.christina.common.data.dao.factory.ModelCollectionFactory;
import com.christina.common.data.dao.factory.ModelFactory;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
@StoryApplicationScope
public final class StoryFactoryModule {
    @Provides
    @Named(StoryFactoryNames.STORY)
    @StoryApplicationScope
    @NonNull
    public final ModelCollectionFactory<Story> provideStoryCollectionFactory(
        @Named(StoryFactoryNames.STORY) @NonNull final ModelFactory<Story> storyFactory) {
        Contracts.requireNonNull(storyFactory, "storyFactory == null");

        return new ClassModelCollectionFactory<>(Story.class, storyFactory);
    }

    @Provides
    @Named(StoryFactoryNames.STORY)
    @StoryApplicationScope
    @NonNull
    public final ModelFactory<Story> provideStoryFactory(
        @NonNull final StoryFullProjection fullProjection) {
        Contracts.requireNonNull(fullProjection, "fullProjection == null");

        return new StoryModelFactory(fullProjection);
    }

    @Provides
    @Named(StoryFactoryNames.STORY_FRAME)
    @StoryApplicationScope
    @NonNull
    public final ModelCollectionFactory<StoryFrame> provideStoryFrameCollectionFactory(
        @Named(StoryFactoryNames.STORY_FRAME) @NonNull
        final ModelFactory<StoryFrame> storyFrameFactory) {
        Contracts.requireNonNull(storyFrameFactory, "storyFrameFactory == null");

        return new ClassModelCollectionFactory<>(StoryFrame.class, storyFrameFactory);
    }

    @Provides
    @Named(StoryFactoryNames.STORY_FRAME)
    @StoryApplicationScope
    @NonNull
    public final ModelFactory<StoryFrame> provideStoryFrameFactory(
        @NonNull final StoryFrameFullProjection fullProjection) {
        Contracts.requireNonNull(fullProjection, "fullProjection == null");

        return new StoryFrameModelFactory(fullProjection);
    }
}
