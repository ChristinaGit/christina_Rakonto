package com.christina.api.story.dao.story;

import android.content.ContentResolver;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.christina.api.story.contract.StoryContract;
import com.christina.api.story.model.Story;
import com.christina.common.contract.Contracts;
import com.christina.common.data.dao.ContentProviderDao;
import com.christina.common.data.dao.factory.ModelCollectionFactory;
import com.christina.common.data.dao.factory.ModelContentExtractor;
import com.christina.common.data.dao.factory.ModelFactory;
import com.christina.common.data.projection.Projection;

import lombok.experimental.Accessors;

@Accessors(prefix = "_")
public final class StoryDao extends ContentProviderDao<Story> {
    public StoryDao(
        @NonNull final ContentResolver contentResolver,
        @NonNull final Projection fullProjection,
        @NonNull final ModelFactory<Story> modelFactory,
        @NonNull final ModelCollectionFactory<Story> modelCollectionFactory,
        @NonNull final ModelContentExtractor<Story> modelContentExtractor) {
        super(
            contentResolver,
            fullProjection,
            modelFactory,
            modelCollectionFactory,
            modelContentExtractor);
    }

    @Nullable
    public Story create() {
        Story story = getModelFactory().create();

        if (insert(story) == Story.NO_ID) {
            story = null;
        }

        return story;
    }

    @Override
    protected final long extractId(@NonNull final Uri modelUri) {
        Contracts.requireNonNull(modelUri, "modelUri == null");

        return Long.parseLong(StoryContract.extractStoryId(modelUri));
    }

    @NonNull
    @Override
    protected final Uri getModelUri() {
        return StoryContract.getStoriesUri();
    }

    @NonNull
    @Override
    protected final Uri getModelUri(final long id) {
        return StoryContract.getStoryUri(String.valueOf(id));
    }
}
