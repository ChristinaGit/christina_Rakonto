package com.christina.app.story.di.storyApplication.module;

import android.content.ContentResolver;
import android.support.annotation.NonNull;

import com.christina.api.story.dao.story.StoryDao;
import com.christina.api.story.dao.story.StoryFullProjection;
import com.christina.api.story.dao.storyFrame.StoryFrameDao;
import com.christina.api.story.dao.storyFrame.StoryFrameFullProjection;
import com.christina.api.story.model.Story;
import com.christina.api.story.model.StoryFrame;
import com.christina.app.story.di.qualifier.ForStory;
import com.christina.app.story.di.qualifier.ForStoryFrame;
import com.christina.app.story.di.storyApplication.StoryApplicationScope;
import com.christina.app.story.manager.content.StoryDaoManager;
import com.christina.common.contract.Contracts;
import com.christina.common.data.dao.factory.ModelCollectionFactory;
import com.christina.common.data.dao.factory.ModelContentExtractor;
import com.christina.common.data.dao.factory.ModelFactory;

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
        @ForStory @NonNull final ModelFactory<Story> modelFactory,
        @ForStory @NonNull final ModelCollectionFactory<Story> modelCollectionFactory,
        @ForStory @NonNull final ModelContentExtractor<Story> modelContentExtractor) {
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
        @ForStoryFrame @NonNull final ModelFactory<StoryFrame> modelFactory,
        @ForStoryFrame @NonNull final ModelCollectionFactory<StoryFrame> modelCollectionFactory,
        @ForStoryFrame @NonNull final ModelContentExtractor<StoryFrame> modelContentExtractor) {
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
