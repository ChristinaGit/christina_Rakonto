package moe.christina.app.rakonto.di.subscreen.module;

import android.support.annotation.NonNull;

import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.android.FragmentEvent;

import moe.christina.app.rakonto.di.qualifier.ScopeNames;
import moe.christina.app.rakonto.di.subscreen.RakontoSubscreenScope;
import moe.christina.common.contract.Contracts;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
@RakontoSubscreenScope
public final class RakontoSubscreenRxModule {
    public RakontoSubscreenRxModule(
        @NonNull final LifecycleProvider<FragmentEvent> lifecycleProvider) {
        Contracts.requireNonNull(lifecycleProvider, "lifecycleProvider == null");

        _lifecycleProvider = lifecycleProvider;
    }

    @Named(ScopeNames.SUBSCREEN)
    @Provides
    @RakontoSubscreenScope
    @NonNull
    public final LifecycleProvider<FragmentEvent> provideLifecycleProvider() {
        return _lifecycleProvider;
    }

    @NonNull
    private final LifecycleProvider<FragmentEvent> _lifecycleProvider;
}
