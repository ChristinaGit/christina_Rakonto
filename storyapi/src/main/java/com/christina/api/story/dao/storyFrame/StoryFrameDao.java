package com.christina.api.story.dao.storyFrame;

import android.content.ContentResolver;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.christina.api.story.contract.StoryFrameContract;
import com.christina.api.story.model.StoryFrame;
import com.christina.common.contract.Contracts;
import com.christina.common.data.dao.ContentProviderDao;
import com.christina.common.data.dao.factory.ModelCollectionFactory;
import com.christina.common.data.dao.factory.ModelContentExtractor;
import com.christina.common.data.dao.factory.ModelFactory;
import com.christina.common.data.dao.result.CollectionResult;
import com.christina.common.data.projection.Projection;

import lombok.experimental.Accessors;

@Accessors(prefix = "_")
public final class StoryFrameDao extends ContentProviderDao<StoryFrame> {
    public StoryFrameDao(
        @NonNull final ContentResolver contentResolver,
        @NonNull final Projection fullProjection,
        @NonNull final ModelFactory<StoryFrame> modelFactory,
        @NonNull final ModelCollectionFactory<StoryFrame> modelCollectionFactory,
        @NonNull final ModelContentExtractor<StoryFrame> modelContentExtractor) {
        super(
            contentResolver,
            fullProjection,
            modelFactory,
            modelCollectionFactory,
            modelContentExtractor);
    }

    @Nullable
    public final CollectionResult<StoryFrame> getByStoryId(final long storyId) {
        return select(StoryFrameContract.getStoryFramesByStoryUri(String.valueOf(storyId)));
    }

    @Nullable
    public StoryFrame create(final long storyId) {
        StoryFrame storyFrame = getModelFactory().create();
        storyFrame.setStoryId(storyId);

        if (insert(storyFrame) == StoryFrame.NO_ID) {
            storyFrame = null;
        }

        return storyFrame;
    }

    @Override
    protected final long extractId(@NonNull final Uri modelUri) {
        Contracts.requireNonNull(modelUri, "modelUri == null");

        return Long.parseLong(StoryFrameContract.extractStoryFrameId(modelUri));
    }

    @NonNull
    @Override
    protected final Uri getModelUri() {
        return StoryFrameContract.getStoryFramesUri();
    }

    @NonNull
    @Override
    protected final Uri getModelUri(final long id) {
        return StoryFrameContract.getStoryFrameUri(String.valueOf(id));
    }
}
