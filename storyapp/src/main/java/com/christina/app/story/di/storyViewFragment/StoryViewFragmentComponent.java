package com.christina.app.story.di.storyViewFragment;

import com.christina.app.story.di.storyViewFragment.module.StoryFragmentViewManagerModule;
import com.christina.app.story.di.storyViewFragment.module.StoryViewFragmentPresenterModule;
import com.christina.app.story.view.fragment.storiesList.StoriesListFragment;
import com.christina.app.story.view.fragment.storyFramesEditor.StoryFramesEditorFragment;
import com.christina.app.story.view.fragment.storyTextEditor.StoryTextEditorFragment;

import dagger.Subcomponent;

@Subcomponent(modules = {
    StoryFragmentViewManagerModule.class, StoryViewFragmentPresenterModule.class})
@StoryViewFragmentScope
public interface StoryViewFragmentComponent {
    void inject(StoriesListFragment fragment);

    void inject(StoryTextEditorFragment fragment);

    void inject(StoryFramesEditorFragment fragment);
}
