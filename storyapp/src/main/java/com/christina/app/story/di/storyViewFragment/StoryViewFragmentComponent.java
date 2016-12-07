package com.christina.app.story.di.storyViewFragment;

import com.christina.app.story.di.storyViewFragment.module.StoryViewFragmentPresenterModule;
import com.christina.app.story.view.fragment.storiesList.StoriesListFragment;
import com.christina.app.story.view.fragment.storyTextEditor.StoryTextEditorFragment;

import dagger.Subcomponent;

@Subcomponent(modules = {StoryViewFragmentPresenterModule.class})
@StoryViewFragmentScope
public interface StoryViewFragmentComponent {
    void inject(StoriesListFragment fragment);

    void inject(StoryTextEditorFragment fragment);
}
