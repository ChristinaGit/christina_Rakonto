package moe.christina.app.rakonto.di.screen.module;

import android.support.annotation.NonNull;

import moe.christina.app.rakonto.di.qualifier.ScopeNames;
import moe.christina.app.rakonto.di.screen.RakontoScreenScope;
import moe.christina.common.contract.Contracts;
import moe.christina.common.control.adviser.ResourceAdviser;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
@RakontoScreenScope
public final class RakontoScreenAdviserModule {
    public RakontoScreenAdviserModule(@NonNull final ResourceAdviser resourceAdviser) {
        Contracts.requireNonNull(resourceAdviser, "resourceAdviser == null");

        _resourceAdviser = resourceAdviser;
    }

    @Named(ScopeNames.SCREEN)
    @Provides
    @RakontoScreenScope
    @NonNull
    public final ResourceAdviser provideResourceAdviser() {
        return _resourceAdviser;
    }

    @NonNull
    private final ResourceAdviser _resourceAdviser;
}
