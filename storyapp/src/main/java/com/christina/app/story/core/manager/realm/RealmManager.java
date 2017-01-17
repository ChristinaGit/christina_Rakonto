package com.christina.app.story.core.manager.realm;

import android.support.annotation.NonNull;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmModel;

public interface RealmManager {
    long generateNextId(@NonNull Class<? extends RealmModel> modelClass);

    @NonNull
    Realm getRealm();

    @NonNull
    RealmConfiguration getRealmConfiguration();
}
