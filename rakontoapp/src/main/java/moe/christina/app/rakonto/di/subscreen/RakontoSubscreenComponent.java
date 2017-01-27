package moe.christina.app.rakonto.di.subscreen;

import moe.christina.app.rakonto.di.subscreen.module.RakontoSubscreenAdviserModule;
import moe.christina.app.rakonto.di.subscreen.module.RakontoSubscreenManagerModule;
import moe.christina.app.rakonto.di.subscreen.module.RakontoSubscreenPresenterModule;
import moe.christina.app.rakonto.di.subscreen.module.RakontoSubscreenRxModule;
import moe.christina.app.rakonto.screen.fragment.storiesList.StoriesListFragment;
import moe.christina.app.rakonto.screen.fragment.storyFramesEditor.StoryFramesEditorFragment;
import moe.christina.app.rakonto.screen.fragment.storyTextEditor.StoryTextEditorFragment;

import dagger.Subcomponent;

@Subcomponent(modules = {
    RakontoSubscreenManagerModule.class,
    RakontoSubscreenPresenterModule.class,
    RakontoSubscreenRxModule.class,
    RakontoSubscreenAdviserModule.class})
@RakontoSubscreenScope
public interface RakontoSubscreenComponent {
    void inject(StoriesListFragment fragment);

    void inject(StoryTextEditorFragment fragment);

    void inject(StoryFramesEditorFragment fragment);
}
