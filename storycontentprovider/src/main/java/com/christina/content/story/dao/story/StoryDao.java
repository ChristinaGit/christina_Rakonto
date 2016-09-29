package com.christina.content.story.dao.story;

import android.content.ContentResolver;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.christina.common.contract.Contracts;
import com.christina.common.data.dao.ContentProviderDao;
import com.christina.common.data.model.factory.ContentModelCollectionFactory;
import com.christina.common.data.model.factory.ContentModelFactory;
import com.christina.common.data.model.factory.ModelContentExtractor;
import com.christina.content.story.contract.StoryContract;
import com.christina.content.story.model.Story;

public final class StoryDao extends ContentProviderDao<Story> {
    public StoryDao(@NonNull final ContentResolver contentResolver) {
        super(contentResolver, StoryFullProjection.PROJECTION);
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

    @NonNull
    @Override
    protected final ContentModelCollectionFactory<Story> getModelCollectionFactory() {
        if (_modelCollectionFactory == null) {
            _modelCollectionFactory = new StoryCollectionFactory(getModelFactory());
        }

        return _modelCollectionFactory;
    }

    @NonNull
    @Override
    protected final ModelContentExtractor<Story> getModelContentExtractor() {
        if (_modelContentExtractor == null) {
            _modelContentExtractor = new StoryContentExtractor();
        }
        return _modelContentExtractor;
    }

    @NonNull
    @Override
    protected final ContentModelFactory<Story> getModelFactory() {
        if (_modelFactory == null) {
            _modelFactory = new StoryModelFactory();
        }
        return _modelFactory;
    }

    @Nullable
    private ContentModelCollectionFactory<Story> _modelCollectionFactory;

    @Nullable
    private ModelContentExtractor<Story> _modelContentExtractor;

    @Nullable
    private ContentModelFactory<Story> _modelFactory;
}
