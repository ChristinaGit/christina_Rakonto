package moe.christina.app.rakonto.di;

import android.support.annotation.NonNull;

import moe.christina.app.rakonto.di.application.RakontoApplicationComponent;

public interface RakontoApplicationComponentProvider {
    @NonNull
    RakontoApplicationComponent getRakontoApplicationComponent();
}
