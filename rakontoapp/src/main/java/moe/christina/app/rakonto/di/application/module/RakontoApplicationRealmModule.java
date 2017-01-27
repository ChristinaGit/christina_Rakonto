package moe.christina.app.rakonto.di.application.module;

import android.support.annotation.NonNull;

import lombok.val;

import io.realm.Realm;
import io.realm.RealmConfiguration;

import moe.christina.app.rakonto.di.application.RakontoApplicationScope;
import moe.christina.common.contract.Contracts;
import moe.christina.common.data.realm.RealmIdGenerator;

import dagger.Module;
import dagger.Provides;

@Module
@RakontoApplicationScope
public final class RakontoApplicationRealmModule {
    @Provides
    @RakontoApplicationScope
    @NonNull
    public final RealmConfiguration provideRealmConfiguration() {
        return new RealmConfiguration.Builder().name("stories.realm").schemaVersion(1).build();
    }

    @Provides
    @RakontoApplicationScope
    @NonNull
    public final RealmIdGenerator provideRealmIdGenerator(
        @NonNull final RealmConfiguration realmConfiguration) {
        Contracts.requireNonNull(realmConfiguration, "realmConfiguration == null");

        final val realmIdGenerator = new RealmIdGenerator();
        try (final val realm = Realm.getInstance(realmConfiguration)) {
            realmIdGenerator.initialize(realm);
        }
        return realmIdGenerator;
    }
}
