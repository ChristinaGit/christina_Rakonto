package com.christina.app.story.presentation;

import android.support.annotation.NonNull;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;

import com.christina.app.story.core.manager.ServiceManager;
import com.christina.app.story.core.manager.data.RealmManager;
import com.christina.app.story.core.manager.message.MessageManager;
import com.christina.app.story.core.manager.navigation.StoryNavigator;
import com.christina.app.story.core.manager.resource.ResourceManager;
import com.christina.app.story.core.manager.rx.RxManager;
import com.christina.common.contract.Contracts;
import com.christina.common.presentation.AbstractPresenter;
import com.christina.common.presentation.Screen;

@Accessors(prefix = "_")
public abstract class BaseStoryPresenter<TScreen extends Screen>
    extends AbstractPresenter<TScreen> {
    public BaseStoryPresenter(
        @NonNull final ServiceManager serviceManager) {
        Contracts.requireNonNull(serviceManager, "serviceManager == null");

        _serviceManager = serviceManager;
    }

    @NonNull
    public final RealmManager getRealmManager() {
        return getServiceManager().getRealmManager();
    }

    @NonNull
    public final ResourceManager getResourceManager() {
        return getServiceManager().getResourceManager();
    }

    @NonNull
    public final RxManager getRxManager() {
        return getServiceManager().getRxManager();
    }

    @NonNull
    protected final MessageManager getMessageManager() {
        return getServiceManager().getMessageManager();
    }

    @NonNull
    protected final StoryNavigator getStoryNavigator() {
        return getServiceManager().getStoryNavigator();
    }

    @Getter(AccessLevel.PROTECTED)
    @NonNull
    private final ServiceManager _serviceManager;
}
