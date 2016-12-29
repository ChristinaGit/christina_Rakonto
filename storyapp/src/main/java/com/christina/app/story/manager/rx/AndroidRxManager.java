package com.christina.app.story.manager.rx;

import android.support.annotation.NonNull;

import com.christina.common.contract.Contracts;
import com.trello.rxlifecycle.LifecycleProvider;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@Accessors(prefix = "_")
public final class AndroidRxManager<TLifecycleEvent> implements RxManager {
    public AndroidRxManager(
        @NonNull final LifecycleProvider<TLifecycleEvent> lifecycleProvider) {
        Contracts.requireNonNull(lifecycleProvider, "lifecycleProvider == null");

        _lifecycleProvider = lifecycleProvider;
    }

    @NonNull
    @Override
    public final <T> Observable<T> autoManage(@NonNull final Observable<T> observable) {
        Contracts.requireNonNull(observable, "observable == null");

        return observable.compose(getLifecycleProvider().<T>bindToLifecycle());
    }

    @NonNull
    @Override
    public final Scheduler getComputationScheduler() {
        return Schedulers.computation();
    }

    @NonNull
    @Override
    public final Scheduler getIOScheduler() {
        return Schedulers.io();
    }

    @NonNull
    @Override
    public final Scheduler getUIScheduler() {
        return AndroidSchedulers.mainThread();
    }

    @Getter(AccessLevel.PROTECTED)
    @NonNull
    private final LifecycleProvider<TLifecycleEvent> _lifecycleProvider;
}
