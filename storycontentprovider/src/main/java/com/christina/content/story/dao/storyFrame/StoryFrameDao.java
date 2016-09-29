package com.christina.content.story.dao.storyFrame;

import android.content.ContentResolver;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.christina.common.contract.Contracts;
import com.christina.common.data.dao.ContentProviderDao;
import com.christina.common.data.model.factory.ContentModelCollectionFactory;
import com.christina.common.data.model.factory.ContentModelFactory;
import com.christina.common.data.model.factory.ModelContentExtractor;
import com.christina.content.story.contract.StoryFrameContract;
import com.christina.content.story.model.StoryFrame;

public final class StoryFrameDao extends ContentProviderDao<StoryFrame> {
    public StoryFrameDao(@NonNull final ContentResolver contentResolver) {
        super(contentResolver, StoryFrameFullProjection.PROJECTION);
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

    @NonNull
    @Override
    protected final ContentModelCollectionFactory<StoryFrame> getModelCollectionFactory() {
        if (_modelCollectionFactory == null) {
            _modelCollectionFactory = new StoryFrameCollectionFactory(getModelFactory());
        }

        return _modelCollectionFactory;
    }

    @NonNull
    @Override
    protected final ModelContentExtractor<StoryFrame> getModelContentExtractor() {
        if (_modelContentExtractor == null) {
            _modelContentExtractor = new StoryFrameContentExtractor();
        }
        return _modelContentExtractor;
    }

    @NonNull
    @Override
    protected final ContentModelFactory<StoryFrame> getModelFactory() {
        if (_modelFactory == null) {
            _modelFactory = new StoryFrameModelFactory();
        }
        return _modelFactory;
    }

    @Nullable
    private ContentModelCollectionFactory<StoryFrame> _modelCollectionFactory;

    @Nullable
    private ModelContentExtractor<StoryFrame> _modelContentExtractor;

    @Nullable
    private ContentModelFactory<StoryFrame> _modelFactory;
}
