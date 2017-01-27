package moe.christina.app.rakonto.di;

import android.support.annotation.NonNull;

import moe.christina.app.rakonto.di.subscreen.RakontoSubscreenComponent;

public interface RakontoSubscreenComponentProvider {
    @NonNull
    RakontoSubscreenComponent getRakontoSubscreenComponent();
}
