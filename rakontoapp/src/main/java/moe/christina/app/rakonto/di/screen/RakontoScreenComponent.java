package moe.christina.app.rakonto.di.screen;

import moe.christina.app.rakonto.di.screen.module.RakontoScreenAdviserModule;
import moe.christina.app.rakonto.di.screen.module.RakontoScreenManagerModule;
import moe.christina.app.rakonto.di.screen.module.RakontoScreenPresenterModule;
import moe.christina.app.rakonto.di.screen.module.RakontoScreenRxModule;
import moe.christina.app.rakonto.di.subscreen.RakontoSubscreenComponent;
import moe.christina.app.rakonto.di.subscreen.module.RakontoSubscreenAdviserModule;
import moe.christina.app.rakonto.di.subscreen.module.RakontoSubscreenManagerModule;
import moe.christina.app.rakonto.di.subscreen.module.RakontoSubscreenPresenterModule;
import moe.christina.app.rakonto.di.subscreen.module.RakontoSubscreenRxModule;
import moe.christina.app.rakonto.screen.activity.storiesViewer.StoriesViewerActivity;
import moe.christina.app.rakonto.screen.activity.storyEditor.StoryEditorActivity;

import dagger.Subcomponent;

@Subcomponent(modules = {
    RakontoScreenPresenterModule.class,
    RakontoScreenManagerModule.class,
    RakontoScreenRxModule.class,
    RakontoScreenAdviserModule.class})
@RakontoScreenScope
public interface RakontoScreenComponent {
    RakontoSubscreenComponent addRakontoSubscreenComponent(
        RakontoSubscreenAdviserModule rakontoSubscreenAdviserModule,
        RakontoSubscreenManagerModule rakontoSubscreenManagerModule,
        RakontoSubscreenPresenterModule rakontoSubscreenPresenterModule,
        RakontoSubscreenRxModule rakontoSubscreenRxModule);

    void inject(StoriesViewerActivity activity);

    void inject(StoryEditorActivity activity);
}
