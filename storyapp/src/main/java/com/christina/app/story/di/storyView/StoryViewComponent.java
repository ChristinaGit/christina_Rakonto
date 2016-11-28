package com.christina.app.story.di.storyView;

import com.christina.app.story.di.storyView.module.StoryContentObserverModule;
import com.christina.app.story.di.storyView.module.StoryPresenterModule;
import com.christina.app.story.di.storyView.module.StoryViewManagerModule;
import com.christina.app.story.di.storyViewFragment.StoryViewFragmentComponent;
import com.christina.app.story.operation.editStory.EditStoryActivity;
import com.christina.app.story.view.activity.ViewStoriesActivity;

import dagger.Subcomponent;

@Subcomponent(modules = {
    StoryContentObserverModule.class, StoryPresenterModule.class, StoryViewManagerModule.class})
@StoryViewScope
public interface StoryViewComponent {
    StoryViewFragmentComponent addStoryScreenFragmentComponent();

    void inject(ViewStoriesActivity activity);

    void inject(EditStoryActivity activity);
}
