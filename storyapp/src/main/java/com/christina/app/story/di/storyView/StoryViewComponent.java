package com.christina.app.story.di.storyView;

import com.christina.app.story.di.storyView.module.StoryContentObserverModule;
import com.christina.app.story.di.storyView.module.StoryViewManagerModule;
import com.christina.app.story.di.storyView.module.StoryViewPresenterModule;
import com.christina.app.story.di.storyViewFragment.StoryViewFragmentComponent;
import com.christina.app.story.di.storyViewFragment.module.StoryViewFragmentPresenterModule;
import com.christina.app.story.view.activity.storiesViewer.StoriesViewerActivity;
import com.christina.app.story.view.activity.storyEditor.StoryEditorActivity;

import dagger.Subcomponent;

@Subcomponent(modules = {
    StoryContentObserverModule.class, StoryViewPresenterModule.class, StoryViewManagerModule.class})
@StoryViewScope
public interface StoryViewComponent {
    StoryViewFragmentComponent addStoryViewFragmentComponent(
        StoryViewFragmentPresenterModule storyViewFragmentPresenterModule);

    void inject(StoriesViewerActivity activity);

    void inject(StoryEditorActivity activity);
}
