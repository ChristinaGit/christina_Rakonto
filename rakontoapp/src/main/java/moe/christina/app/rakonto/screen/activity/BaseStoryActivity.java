package moe.christina.app.rakonto.screen.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import lombok.experimental.Accessors;
import lombok.val;

import moe.christina.app.rakonto.di.RakontoApplicationComponentProvider;
import moe.christina.app.rakonto.di.RakontoScreenComponentProvider;
import moe.christina.app.rakonto.di.application.RakontoApplicationComponent;
import moe.christina.app.rakonto.di.screen.RakontoScreenComponent;
import moe.christina.app.rakonto.di.screen.module.RakontoScreenAdviserModule;
import moe.christina.app.rakonto.di.screen.module.RakontoScreenManagerModule;
import moe.christina.app.rakonto.di.screen.module.RakontoScreenPresenterModule;
import moe.christina.app.rakonto.di.screen.module.RakontoScreenRxModule;
import moe.christina.common.extension.activity.ScreenActivity;

@Accessors(prefix = "_")
public abstract class BaseStoryActivity extends ScreenActivity
    implements RakontoScreenComponentProvider {

    @NonNull
    public final RakontoScreenComponent getRakontoScreenComponent() {
        if (_rakontoScreenComponent == null) {
            throw new IllegalStateException("The activity has not yet been created.");
        }

        return _rakontoScreenComponent;
    }

    @NonNull
    public final RakontoApplicationComponent getStoryApplicationComponent() {
        final val application = getApplication();
        if (application instanceof RakontoApplicationComponentProvider) {
            return ((RakontoApplicationComponentProvider) application)
                .getRakontoApplicationComponent();
        } else {
            throw new IllegalStateException("The application must implement " +
                                            RakontoApplicationComponentProvider.class.getName());
        }
    }

    @CallSuper
    @Override
    protected void onInjectMembers() {
        super.onInjectMembers();

        _rakontoScreenComponent = getStoryApplicationComponent().addRakontoScreenComponent(
            new RakontoScreenAdviserModule(this),
            new RakontoScreenPresenterModule(),
            new RakontoScreenManagerModule(/*ObservableActivity*/ this),
            new RakontoScreenRxModule(/*LifecycleProvider*/ this));
    }

    @Nullable
    private RakontoScreenComponent _rakontoScreenComponent;
}
