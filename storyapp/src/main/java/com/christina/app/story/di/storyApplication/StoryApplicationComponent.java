package com.christina.app.story.di.storyApplication;

import com.christina.app.story.di.storyApplication.module.StoryApplicationRealmModule;
import com.christina.app.story.di.storyScreen.StoryScreenComponent;
import com.christina.app.story.di.storyScreen.module.StoryScreenManagerModule;
import com.christina.app.story.di.storyScreen.module.StoryScreenPresenterModule;
import com.christina.app.story.di.storyScreen.module.StoryScreenRxModule;

import dagger.Component;

@Component(modules = {StoryApplicationRealmModule.class})
@StoryApplicationScope
public interface StoryApplicationComponent {
    StoryScreenComponent addStoryScreenComponent(
        StoryScreenPresenterModule storyScreenPresenterModule,
        StoryScreenManagerModule storyScreenManagerModule,
        StoryScreenRxModule storyScreenRxModule);
}
