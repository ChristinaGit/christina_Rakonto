package moe.christina.app.rakonto.screen.fragment;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import lombok.experimental.Accessors;
import lombok.val;

import moe.christina.app.rakonto.di.RakontoScreenComponentProvider;
import moe.christina.app.rakonto.di.RakontoSubscreenComponentProvider;
import moe.christina.app.rakonto.di.screen.RakontoScreenComponent;
import moe.christina.app.rakonto.di.subscreen.RakontoSubscreenComponent;
import moe.christina.app.rakonto.di.subscreen.module.RakontoSubscreenAdviserModule;
import moe.christina.app.rakonto.di.subscreen.module.RakontoSubscreenManagerModule;
import moe.christina.app.rakonto.di.subscreen.module.RakontoSubscreenPresenterModule;
import moe.christina.app.rakonto.di.subscreen.module.RakontoSubscreenRxModule;
import moe.christina.common.extension.fragment.ScreenFragment;

@Accessors(prefix = "_")
public abstract class BaseStoryFragment extends ScreenFragment
    implements RakontoSubscreenComponentProvider {

    @NonNull
    public final RakontoSubscreenComponent getRakontoSubscreenComponent() {
        if (_rakontoSubscreenComponent == null) {
            throw new IllegalStateException("The fragment has not yet been created.");
        }

        return _rakontoSubscreenComponent;
    }

    @NonNull
    public final RakontoScreenComponent getStoryViewComponent() {
        final val activity = getActivity();
        if (activity instanceof RakontoScreenComponentProvider) {
            return ((RakontoScreenComponentProvider) activity).getRakontoScreenComponent();
        } else {
            throw new IllegalStateException(
                "The activity must implement " + RakontoScreenComponentProvider.class.getName());
        }
    }

    @CallSuper
    @Override
    protected void onInjectMembers() {
        super.onInjectMembers();

        _rakontoSubscreenComponent = getStoryViewComponent().addRakontoSubscreenComponent(
            new RakontoSubscreenAdviserModule(this),
            new RakontoSubscreenManagerModule(),
            new RakontoSubscreenPresenterModule(),
            new RakontoSubscreenRxModule(this));
    }

    @Nullable
    private RakontoSubscreenComponent _rakontoSubscreenComponent;
}
