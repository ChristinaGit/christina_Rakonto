package com.christina.app.story.presentation;

import android.support.annotation.NonNull;

import com.christina.api.story.observer.StoryContentObserver;
import com.christina.app.story.core.manager.ServiceManager;
import com.christina.app.story.core.manager.content.StoryContentObserverManager;
import com.christina.app.story.core.manager.content.StoryDaoManager;
import com.christina.app.story.core.manager.message.MessageManager;
import com.christina.app.story.core.manager.navigation.StoryNavigator;
import com.christina.app.story.core.manager.rx.RxManager;
import com.christina.common.contract.Contracts;
import com.christina.common.view.presentation.AbstractPresenter;
import com.christina.common.view.presentation.PresentableView;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
public abstract class BaseStoryPresenter<TPresentableView extends PresentableView>
    extends AbstractPresenter<TPresentableView> {
    public BaseStoryPresenter(
        @NonNull final ServiceManager serviceManager) {
        Contracts.requireNonNull(serviceManager, "serviceManager == null");

        _serviceManager = serviceManager;
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
    protected final StoryContentObserver getStoryContentObserver() {
        return getStoryContentObserverManager().getStoryContentObserver();
    }

    @NonNull
    protected final StoryContentObserverManager getStoryContentObserverManager() {
        return getServiceManager().getStoryContentObserverManager();
    }

    @NonNull
    protected final StoryDaoManager getStoryDaoManager() {
        return getServiceManager().getStoryDaoManager();
    }

    @NonNull
    protected final StoryNavigator getStoryNavigator() {
        return getServiceManager().getStoryNavigator();
    }

    @Getter(AccessLevel.PROTECTED)
    @NonNull
    private final ServiceManager _serviceManager;
}
