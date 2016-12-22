package com.christina.app.story.di.storyApplication.module;

import android.support.annotation.NonNull;

import com.christina.api.story.dao.story.StoryContentExtractor;
import com.christina.api.story.dao.storyFrame.StoryFrameContentExtractor;
import com.christina.api.story.model.Story;
import com.christina.api.story.model.StoryFrame;
import com.christina.app.story.di.qualifier.StoryContentExtractorNames;
import com.christina.app.story.di.storyApplication.StoryApplicationScope;
import com.christina.common.data.dao.factory.ModelContentExtractor;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
@StoryApplicationScope
public final class StoryContentExtractorModule {
    @Named(StoryContentExtractorNames.STORY)
    @Provides
    @StoryApplicationScope
    @NonNull
    public final ModelContentExtractor<Story> provideStoryContentExtractor() {
        return new StoryContentExtractor();
    }

    @Named(StoryContentExtractorNames.STORY_FRAME)
    @Provides
    @StoryApplicationScope
    @NonNull
    public final ModelContentExtractor<StoryFrame> provideStoryFrameContentExtractor() {
        return new StoryFrameContentExtractor();
    }
}
