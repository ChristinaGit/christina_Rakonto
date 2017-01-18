package com.christina.app.story.di.storySubscreen;

import com.christina.app.story.di.storySubscreen.module.StorySubscreenAdviserModule;
import com.christina.app.story.di.storySubscreen.module.StorySubscreenManagerModule;
import com.christina.app.story.di.storySubscreen.module.StorySubscreenPresenterModule;
import com.christina.app.story.di.storySubscreen.module.StorySubscreenRxModule;
import com.christina.app.story.view.fragment.storiesList.StoriesListFragment;
import com.christina.app.story.view.fragment.storyFramesEditor.StoryFramesEditorFragment;
import com.christina.app.story.view.fragment.storyTextEditor.StoryTextEditorFragment;

import dagger.Subcomponent;

@Subcomponent(modules = {
    StorySubscreenManagerModule.class,
    StorySubscreenPresenterModule.class,
    StorySubscreenRxModule.class,
    StorySubscreenAdviserModule.class})
@StorySubscreenScope
public interface StorySubscreenComponent {
    void inject(StoriesListFragment fragment);

    void inject(StoryTextEditorFragment fragment);

    void inject(StoryFramesEditorFragment fragment);
}
