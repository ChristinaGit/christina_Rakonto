package com.christina.api.story.dao.storyFrame;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.christina.common.contract.Contracts;
import com.christina.common.data.model.factory.ContentModelFactory;
import com.christina.common.data.model.factory.DefaultModelCollectionFactory;
import com.christina.api.story.model.StoryFrame;

public final class StoryFrameCollectionFactory extends DefaultModelCollectionFactory<StoryFrame> {
    public StoryFrameCollectionFactory(
        @NonNull final ContentModelFactory<StoryFrame> modelFactory) {
        super(modelFactory);
    }

    @NonNull
    @Override
    public final StoryFrame[] createArray(
        @IntRange(from = 0, to = Integer.MAX_VALUE) final int size) {
        Contracts.requireInRange(size, 0, Integer.MAX_VALUE);

        return new StoryFrame[size];
    }
}
