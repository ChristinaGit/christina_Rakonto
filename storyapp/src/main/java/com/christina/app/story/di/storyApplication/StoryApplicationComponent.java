package com.christina.app.story.di.storyApplication;

import com.christina.api.story.dao.story.StoryDao;
import com.christina.api.story.dao.storyFrame.StoryFrameDao;
import com.christina.app.story.di.storyApplication.module.ApplicationContextModule;
import com.christina.app.story.di.storyApplication.module.ApplicationServiceModule;
import com.christina.app.story.di.storyApplication.module.StoryContentExtractorModule;
import com.christina.app.story.di.storyApplication.module.StoryDaoModule;
import com.christina.app.story.di.storyApplication.module.StoryFactoryModule;
import com.christina.app.story.di.storyApplication.module.StoryProjectionModule;
import com.christina.app.story.di.storyView.StoryViewComponent;
import com.christina.app.story.di.storyView.module.StoryContentObserverModule;
import com.christina.app.story.di.storyView.module.StoryViewManagerModule;
import com.christina.app.story.di.storyView.module.StoryViewPresenterModule;

import dagger.Component;

@Component(modules = {
    StoryProjectionModule.class,
    StoryFactoryModule.class,
    StoryContentExtractorModule.class,
    StoryDaoModule.class,
    ApplicationServiceModule.class,
    ApplicationContextModule.class})
@StoryApplicationScope
public interface StoryApplicationComponent {
    StoryViewComponent addStoryViewComponent(
        StoryContentObserverModule storyContentObserverModule,
        StoryViewPresenterModule storyViewPresenterModule,
        StoryViewManagerModule storyViewManagerModule);

    // TODO: 11/26/2016 Remove
    StoryDao getStoryDao();

    // TODO: 11/26/2016 Remove
    StoryFrameDao getStoryFrameDao();
}
