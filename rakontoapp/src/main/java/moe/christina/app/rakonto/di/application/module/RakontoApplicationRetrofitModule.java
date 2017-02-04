package moe.christina.app.rakonto.di.application.module;

import android.support.annotation.NonNull;

import moe.christina.app.rakonto.core.api.pixabay.PixabayService;
import moe.christina.app.rakonto.di.application.RakontoApplicationScope;
import moe.christina.common.contract.Contracts;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@RakontoApplicationScope
public final class RakontoApplicationRetrofitModule {
    @Provides
    @RakontoApplicationScope
    @NonNull
    public final Retrofit providePixabayRetrofit() {
        return new Retrofit.Builder()
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://pixabay.com/api/")
            .build();
    }

    @Provides
    @RakontoApplicationScope
    @NonNull
    public final PixabayService providePixabayService(@NonNull final Retrofit retrofit) {
        Contracts.requireNonNull(retrofit, "retrofit == null");

        return retrofit.create(PixabayService.class);
    }
}
