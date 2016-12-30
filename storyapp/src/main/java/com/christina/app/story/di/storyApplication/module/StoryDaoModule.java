package com.christina.app.story.di.storyApplication.module;

import android.content.ContentResolver;
import android.support.annotation.NonNull;

import com.christina.api.story.dao.story.StoryDao;
import com.christina.api.story.dao.story.StoryFullProjection;
import com.christina.api.story.dao.storyFrame.StoryFrameDao;
import com.christina.api.story.dao.storyFrame.StoryFrameFullProjection;
import com.christina.api.story.model.Story;
import com.christina.api.story.model.StoryFrame;
import com.christina.app.story.di.qualifier.StoryContentExtractorNames;
import com.christina.app.story.di.qualifier.StoryFactoryNames;
import com.christina.app.story.di.storyApplication.StoryApplicationScope;
import com.christina.app.story.core.manager.content.StoryDaoManager;
import com.christina.common.contract.Contracts;
import com.christina.common.data.dao.factory.ModelCollectionFactory;
import com.christina.common.data.dao.factory.ModelContentExtractor;
import com.christina.common.data.dao.factory.ModelFactory;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
@StoryApplicationScope
public final class StoryDaoModule {
    @Provides
    @StoryApplicationScope
    @NonNull
    public final StoryDao provideStoryDao(
        @NonNull final ContentResolver contentResolver,
        @NonNull final StoryFullProjection fullProjection,
        @Named(StoryFactoryNames.STORY) @NonNull final ModelFactory<Story> modelFactory,
        @Named(StoryFactoryNames.STORY) @NonNull
        final ModelCollectionFactory<Story> modelCollectionFactory,
        @Named(StoryContentExtractorNames.STORY) @NonNull
        final ModelContentExtractor<Story> modelContentExtractor) {
        Contracts.requireNonNull(contentResolver, "contentResolver == null");
        Contracts.requireNonNull(fullProjection, "fullProjection == null");
        Contracts.requireNonNull(modelFactory, "modelFactory == null");
        Contracts.requireNonNull(modelCollectionFactory, "modelCollectionFactory == null");
        Contracts.requireNonNull(modelContentExtractor, "modelContentExtractor == null");

        return new StoryDao(
            contentResolver,
            fullProjection,
            modelFactory,
            modelCollectionFactory,
            modelContentExtractor);
    }

    @Provides
    @StoryApplicationScope
    @NonNull
    public final StoryDaoManager provideStoryDaoManager(
        @NonNull final StoryDao storyDao, @NonNull final StoryFrameDao storyFrameDao) {
        Contracts.requireNonNull(storyDao, "storyDao == null");
        Contracts.requireNonNull(storyFrameDao, "storyFrameDao == null");

        return new StoryDaoManager(storyDao, storyFrameDao);
    }

    @Provides
    @StoryApplicationScope
    @NonNull
    public final StoryFrameDao provideStoryFrameDao(
        @NonNull final ContentResolver contentResolver,
        @NonNull final StoryFrameFullProjection fullProjection,
        @Named(StoryFactoryNames.STORY_FRAME) @NonNull final ModelFactory<StoryFrame> modelFactory,
        @Named(StoryFactoryNames.STORY_FRAME) @NonNull
        final ModelCollectionFactory<StoryFrame> modelCollectionFactory,
        @Named(StoryContentExtractorNames.STORY_FRAME) @NonNull
        final ModelContentExtractor<StoryFrame> modelContentExtractor) {
        Contracts.requireNonNull(contentResolver, "contentResolver == null");
        Contracts.requireNonNull(fullProjection, "fullProjection == null");
        Contracts.requireNonNull(modelFactory, "modelFactory == null");
        Contracts.requireNonNull(modelCollectionFactory, "modelCollectionFactory == null");
        Contracts.requireNonNull(modelContentExtractor, "modelContentExtractor == null");

        return new StoryFrameDao(
            contentResolver,
            fullProjection,
            modelFactory,
            modelCollectionFactory,
            modelContentExtractor);
    }
}
