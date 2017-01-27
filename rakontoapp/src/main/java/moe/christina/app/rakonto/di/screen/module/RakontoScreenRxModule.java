package moe.christina.app.rakonto.di.screen.module;

import android.support.annotation.NonNull;

import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.android.ActivityEvent;

import moe.christina.app.rakonto.di.qualifier.ScopeNames;
import moe.christina.app.rakonto.di.screen.RakontoScreenScope;
import moe.christina.common.contract.Contracts;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
@RakontoScreenScope
public final class RakontoScreenRxModule {
    public RakontoScreenRxModule(
        @NonNull final LifecycleProvider<ActivityEvent> lifecycleProvider) {
        Contracts.requireNonNull(lifecycleProvider, "lifecycleProvider == null");

        _lifecycleProvider = lifecycleProvider;
    }

    @Named(ScopeNames.SCREEN)
    @Provides
    @RakontoScreenScope
    @NonNull
    public final LifecycleProvider<ActivityEvent> provideLifecycleProvider() {
        return _lifecycleProvider;
    }

    @NonNull
    private final LifecycleProvider<ActivityEvent> _lifecycleProvider;
}
