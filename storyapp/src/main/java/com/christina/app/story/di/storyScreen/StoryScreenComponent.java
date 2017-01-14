package com.christina.app.story.di.storyScreen;

import com.christina.app.story.di.storyScreen.module.StoryScreenManagerModule;
import com.christina.app.story.di.storyScreen.module.StoryScreenPresenterModule;
import com.christina.app.story.di.storyScreen.module.StoryScreenRxModule;
import com.christina.app.story.di.storySubscreen.StorySubscreenComponent;
import com.christina.app.story.di.storySubscreen.module.StorySubscreenManagerModule;
import com.christina.app.story.di.storySubscreen.module.StorySubscreenPresenterModule;
import com.christina.app.story.di.storySubscreen.module.StorySubscreenRxModule;
import com.christina.app.story.view.activity.storiesViewer.StoriesViewerActivity;
import com.christina.app.story.view.activity.storyEditor.StoryEditorActivity;

import dagger.Subcomponent;

@Subcomponent(modules = {
    StoryScreenPresenterModule.class, StoryScreenManagerModule.class, StoryScreenRxModule.class})
@StoryScreenScope
public interface StoryScreenComponent {
    StorySubscreenComponent addStorySubscreenComponent(
        StorySubscreenManagerModule storySubscreenManagerModule,
        StorySubscreenPresenterModule storySubscreenPresenterModule,
        StorySubscreenRxModule storySubscreenRxModule);

    void inject(StoriesViewerActivity activity);

    void inject(StoryEditorActivity activity);
}
