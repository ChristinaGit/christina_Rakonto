package moe.christina.app.rakonto.di.subscreen.module;

import android.support.annotation.NonNull;

import moe.christina.app.rakonto.di.qualifier.ScopeNames;
import moe.christina.app.rakonto.di.subscreen.RakontoSubscreenScope;
import moe.christina.common.contract.Contracts;
import moe.christina.common.control.adviser.ResourceAdviser;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
@RakontoSubscreenScope
public final class RakontoSubscreenAdviserModule {
    public RakontoSubscreenAdviserModule(@NonNull final ResourceAdviser resourceAdviser) {
        Contracts.requireNonNull(resourceAdviser, "resourceAdviser == null");

        _resourceAdviser = resourceAdviser;
    }

    @Named(ScopeNames.SUBSCREEN)
    @Provides
    @RakontoSubscreenScope
    @NonNull
    public final ResourceAdviser provideResourceAdviser() {
        return _resourceAdviser;
    }

    @NonNull
    private final ResourceAdviser _resourceAdviser;
}
