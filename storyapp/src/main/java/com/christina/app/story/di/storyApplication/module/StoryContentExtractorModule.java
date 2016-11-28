package com.christina.app.story.di.storyApplication.module;

import android.support.annotation.NonNull;

import com.christina.api.story.dao.story.StoryContentExtractor;
import com.christina.api.story.dao.storyFrame.StoryFrameContentExtractor;
import com.christina.api.story.model.Story;
import com.christina.api.story.model.StoryFrame;
import com.christina.app.story.di.qualifier.ForStory;
import com.christina.app.story.di.qualifier.ForStoryFrame;
import com.christina.app.story.di.storyApplication.StoryApplicationScope;
import com.christina.common.data.dao.factory.ModelContentExtractor;

import dagger.Module;
import dagger.Provides;

@Module
@StoryApplicationScope
public final class StoryContentExtractorModule {
    @Provides
    @StoryApplicationScope
    @ForStory
    @NonNull
    public final ModelContentExtractor<Story> provideStoryContentExtractor() {
        return new StoryContentExtractor();
    }

    @Provides
    @StoryApplicationScope
    @ForStoryFrame
    @NonNull
    public final ModelContentExtractor<StoryFrame> provideStoryFrameContentExtractor() {
        return new StoryFrameContentExtractor();
    }
}
