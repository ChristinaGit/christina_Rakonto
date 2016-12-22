package com.christina.app.story.presentation;

import android.support.annotation.NonNull;

import com.christina.api.story.model.Story;
import com.christina.api.story.model.StoryFrame;
import com.christina.api.story.observer.StoryContentObserver;
import com.christina.app.story.manager.ServiceManager;
import com.christina.app.story.manager.asyncTask.StoryTaskManager;
import com.christina.app.story.manager.content.StoryContentObserverManager;
import com.christina.app.story.manager.content.StoryDaoManager;
import com.christina.app.story.manager.message.MessageManager;
import com.christina.app.story.manager.navigation.StoryNavigator;
import com.christina.common.contract.Contracts;
import com.christina.common.data.dao.SqlDao;
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
    protected final SqlDao<Story> getStoryDao() {
        return getStoryDaoManager().getStoryDao();
    }

    @NonNull
    protected final StoryDaoManager getStoryDaoManager() {
        return getServiceManager().getStoryDaoManager();
    }

    @NonNull
    protected final SqlDao<StoryFrame> getStoryFrameDao() {
        return getStoryDaoManager().getStoryFrameDao();
    }

    @NonNull
    protected final StoryNavigator getStoryNavigator() {
        return getServiceManager().getStoryNavigator();
    }

    @NonNull
    protected final StoryTaskManager getStoryTaskManager() {
        return getServiceManager().getStoryTaskManager();
    }

    @Getter(AccessLevel.PROTECTED)
    @NonNull
    private final ServiceManager _serviceManager;
}
