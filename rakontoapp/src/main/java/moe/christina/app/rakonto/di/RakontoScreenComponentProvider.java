package moe.christina.app.rakonto.di;

import android.support.annotation.NonNull;

import moe.christina.app.rakonto.di.screen.RakontoScreenComponent;

public interface RakontoScreenComponentProvider {
    @NonNull
    RakontoScreenComponent getRakontoScreenComponent();
}
