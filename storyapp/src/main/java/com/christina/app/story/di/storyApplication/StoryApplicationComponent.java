package com.christina.app.story.di.storyApplication;

import com.christina.app.story.debug.FakeStoryDatabase;
import com.christina.app.story.di.storyApplication.module.StoryApplicationDebugModule;
import com.christina.app.story.di.storyApplication.module.StoryApplicationManagerModule;
import com.christina.app.story.di.storyApplication.module.StoryApplicationRealmModule;
import com.christina.app.story.di.storyScreen.StoryScreenComponent;
import com.christina.app.story.di.storyScreen.module.StoryScreenAwareModule;
import com.christina.app.story.di.storyScreen.module.StoryScreenManagerModule;
import com.christina.app.story.di.storyScreen.module.StoryScreenPresenterModule;
import com.christina.app.story.di.storyScreen.module.StoryScreenRxModule;

import dagger.Component;

@Component(modules = {
    StoryApplicationDebugModule.class,
    StoryApplicationRealmModule.class,
    StoryApplicationManagerModule.class})
@StoryApplicationScope
public interface StoryApplicationComponent {
    StoryScreenComponent addStoryScreenComponent(
        StoryScreenAwareModule storyScreenAwareModule,
        StoryScreenPresenterModule storyScreenPresenterModule,
        StoryScreenManagerModule storyScreenManagerModule,
        StoryScreenRxModule storyScreenRxModule);

    FakeStoryDatabase getFakeStoryDatabase();
}
