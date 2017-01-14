package com.christina.app.story.core;

import android.support.annotation.NonNull;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;

import io.realm.Realm;
import io.realm.RealmModel;

import com.christina.common.contract.Contracts;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Accessors(prefix = "_")
public final class RealmIdGenerator {
    public final long generateNextId(@NonNull Class<? extends RealmModel> modelClass) {
        Contracts.requireNonNull(modelClass, "modelClass == null");

        synchronized (_lock$maximumKeys) {
            if (!_initialized) {
                throw new IllegalStateException(
                    RealmIdGenerator.class + " isn't already initialized.");
            }

            final val maximumKeys = getMaximumKeys();
            AtomicLong primaryKeyValue = maximumKeys.get(modelClass);
            if (primaryKeyValue == null) {
                primaryKeyValue = new AtomicLong(0);
                maximumKeys.put(modelClass, primaryKeyValue);
            }
            return primaryKeyValue.incrementAndGet();
        }
    }

    public final void initialize(@NonNull final Realm realm) {
        Contracts.requireNonNull(realm, "realm == null");

        synchronized (_lock$maximumKeys) {
            if (_initialized) {
                throw new IllegalStateException(
                    RealmIdGenerator.class + " is already initialized.");
            }

            final val maximumKeys = getMaximumKeys();

            final val configuration = realm.getConfiguration();
            final val realmSchema = realm.getSchema();
            for (final val modelClass : configuration.getRealmObjectClasses()) {
                final val objectSchema = realmSchema.get(modelClass.getSimpleName());
                if (objectSchema.hasPrimaryKey()) {
                    final val primaryKeyField = objectSchema.getPrimaryKey();
                    final val primaryKey = realm.where(modelClass).max(primaryKeyField);

                    final long primaryKeyValue;
                    if (primaryKey != null) {
                        primaryKeyValue = primaryKey.longValue();
                    } else {
                        primaryKeyValue = 0L;
                    }

                    maximumKeys.put(modelClass, new AtomicLong(primaryKeyValue));
                }
            }

            _initialized = true;
        }
    }

    private final Object _lock$maximumKeys = new Object();

    @Getter(AccessLevel.PRIVATE)
    @NonNull
    private final Map<Class<? extends RealmModel>, AtomicLong> _maximumKeys = new HashMap<>();

    private boolean _initialized = false;
}
