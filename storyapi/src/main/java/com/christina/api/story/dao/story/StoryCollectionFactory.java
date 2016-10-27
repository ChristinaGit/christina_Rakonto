package com.christina.api.story.dao.story;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.christina.api.story.model.Story;
import com.christina.common.contract.Contracts;
import com.christina.common.data.model.factory.ContentModelFactory;
import com.christina.common.data.model.factory.DefaultModelCollectionFactory;

public final class StoryCollectionFactory extends DefaultModelCollectionFactory<Story> {
    public StoryCollectionFactory(@NonNull final ContentModelFactory<Story> modelFactory) {
        super(modelFactory);
    }

    @NonNull
    @Override
    public final Story[] createArray(@IntRange(from = 0, to = Integer.MAX_VALUE) final int size) {
        Contracts.requireInRange(size, 0, Integer.MAX_VALUE);

        return new Story[size];
    }
}
