package com.christina.app.story.di.storyViewFragment;

import com.christina.app.story.view.fragment.StoriesListFragment;

import dagger.Subcomponent;

@Subcomponent()
@StoryViewFragmentScope
public interface StoryViewFragmentComponent {
    void inject(StoriesListFragment fragment);
}
