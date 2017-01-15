package com.christina.app.story.core;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.realm.RealmChangeListener;
import io.realm.RealmModel;
import io.realm.RealmObject;

import com.christina.common.contract.Contracts;

public final class RealmChangesObserver<TRealmModel extends RealmModel> {
    public RealmChangesObserver(
        @NonNull final RealmChangeListener<TRealmModel> realmChangeListener) {
        Contracts.requireNonNull(realmChangeListener, "realmChangeListener == null");

        _realmChangeListener = new RealmChangeListener<TRealmModel>() {
            @Override
            public void onChange(final TRealmModel element) {
                enable(element);

                realmChangeListener.onChange(element);
            }
        };
    }

    public final void enable(@Nullable final TRealmModel realmModel) {
        if (_realmModel != realmModel) {
            if (_realmModel != null) {
                RealmObject.removeChangeListener(_realmModel, _realmChangeListener);
            }

            if (realmModel != null) {
                RealmObject.addChangeListener(realmModel, _realmChangeListener);
            }

            _realmModel = realmModel;
        }
    }

    public final void release() {
        if (_realmModel != null) {
            RealmObject.removeChangeListener(_realmModel, _realmChangeListener);
        }
    }

    @NonNull
    private final RealmChangeListener<TRealmModel> _realmChangeListener;

    private TRealmModel _realmModel;
}
