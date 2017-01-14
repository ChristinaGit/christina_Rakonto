package com.christina.app.story.core.manager.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmModel;

import com.christina.app.story.core.RealmIdGenerator;
import com.christina.app.story.core.manager.ReleasableManager;
import com.christina.app.story.core.manager.resource.ResourceManager;
import com.christina.common.contract.Contracts;
import com.christina.common.utility.ResourceUtils;

@Accessors(prefix = "_")
public final class AndroidRealmManger extends ReleasableManager implements RealmManager {
    public AndroidRealmManger(
        @NonNull final ResourceManager resourceManager,
        @NonNull final RealmIdGenerator realmIdGenerator) {
        super(Contracts.requireNonNull(resourceManager, "resourceManager == null"));
        Contracts.requireNonNull(realmIdGenerator, "realmIdGenerator == null");

        _realmIdGenerator = realmIdGenerator;
    }

    @Override
    public long generateNextId(@NonNull final Class<? extends RealmModel> modelClass) {
        Contracts.requireNonNull(modelClass, "modelClass == null");

        return getRealmIdGenerator().generateNextId(modelClass);
    }

    @NonNull
    @Override
    public final Realm getRealm() {
        if (_realm == null) {
            throw new IllegalStateException("Realm is not created.");
        }

        return _realm;
    }

    protected final void createRealm() {
        _realm = Realm.getInstance(getRealmConfiguration());
    }

    protected final void destroyRealm() {
        if (_realm != null) {
            ResourceUtils.quietClose(_realm);
            _realm = null;
        }
    }

    @Override
    protected void onAcquireResources() {
        createRealm();
    }

    @Override
    protected void onReleaseResources() {
        destroyRealm();
    }

    @Getter(onMethod = @__(@Override))
    @NonNull
    private final RealmConfiguration _realmConfiguration =
        new RealmConfiguration.Builder().name("stories.realm").schemaVersion(1).build();

    @Getter(AccessLevel.PROTECTED)
    private final RealmIdGenerator _realmIdGenerator;

    @Nullable
    private Realm _realm;
}
