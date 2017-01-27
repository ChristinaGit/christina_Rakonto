package moe.christina.app.rakonto.di.application;

import moe.christina.app.rakonto.core.debug.FakeStoryDatabase;
import moe.christina.app.rakonto.di.application.module.RakontoApplicationDebugModule;
import moe.christina.app.rakonto.di.application.module.RakontoApplicationManagerModule;
import moe.christina.app.rakonto.di.application.module.RakontoApplicationRealmModule;
import moe.christina.app.rakonto.di.screen.RakontoScreenComponent;
import moe.christina.app.rakonto.di.screen.module.RakontoScreenAdviserModule;
import moe.christina.app.rakonto.di.screen.module.RakontoScreenManagerModule;
import moe.christina.app.rakonto.di.screen.module.RakontoScreenPresenterModule;
import moe.christina.app.rakonto.di.screen.module.RakontoScreenRxModule;

import dagger.Component;

@Component(modules = {
    RakontoApplicationDebugModule.class,
    RakontoApplicationRealmModule.class,
    RakontoApplicationManagerModule.class})
@RakontoApplicationScope
public interface RakontoApplicationComponent {
    RakontoScreenComponent addRakontoScreenComponent(
        RakontoScreenAdviserModule rakontoScreenAdviserModule,
        RakontoScreenPresenterModule rakontoScreenPresenterModule,
        RakontoScreenManagerModule rakontoScreenManagerModule,
        RakontoScreenRxModule rakontoScreenRxModule);

    FakeStoryDatabase getFakeStoryDatabase();
}
