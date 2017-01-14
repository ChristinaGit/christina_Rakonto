package com.christina.app.story.view.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;

import com.christina.app.story.core.manager.resource.ResourceManager;
import com.christina.app.story.di.StoryApplicationComponentProvider;
import com.christina.app.story.di.StoryScreenComponentProvider;
import com.christina.app.story.di.storyApplication.StoryApplicationComponent;
import com.christina.app.story.di.storyScreen.StoryScreenComponent;
import com.christina.app.story.di.storyScreen.module.StoryScreenManagerModule;
import com.christina.app.story.di.storyScreen.module.StoryScreenPresenterModule;
import com.christina.app.story.di.storyScreen.module.StoryScreenRxModule;
import com.christina.common.event.Events;
import com.christina.common.event.notice.ManagedNoticeEvent;
import com.christina.common.event.notice.NoticeEvent;
import com.christina.common.view.activity.ScreenActivity;

@Accessors(prefix = "_")
public abstract class BaseStoryActivity extends ScreenActivity
    implements StoryScreenComponentProvider, ResourceManager {

    @NonNull
    @Override
    public final NoticeEvent getAcquireResourcesEvent() {
        return _acquireResourcesEvent;
    }

    @NonNull
    @Override
    public final NoticeEvent getReleaseResourcesEvent() {
        return _releaseResourcesEvent;
    }

    @NonNull
    public final StoryApplicationComponent getStoryApplicationComponent() {
        final val application = getApplication();
        if (application instanceof StoryApplicationComponentProvider) {
            return ((StoryApplicationComponentProvider) application).getStoryApplicationComponent();
        } else {
            throw new IllegalStateException("The application must implement " +
                                            StoryApplicationComponentProvider.class.getName());
        }
    }

    @CallSuper
    @Override
    protected void onAcquireResources() {
        super.onAcquireResources();

        _acquireResourcesEvent.rise();
    }

    @CallSuper
    @Override
    protected void onReleaseResources() {
        super.onReleaseResources();

        _releaseResourcesEvent.rise();
    }

    @NonNull
    private final ManagedNoticeEvent _acquireResourcesEvent = Events.createNoticeEvent();

    @NonNull
    private final ManagedNoticeEvent _releaseResourcesEvent = Events.createNoticeEvent();

    //@formatter:off
    @NonNull
    @Getter(onMethod = @__(@Override), lazy = true)
    private final StoryScreenComponent _storyScreenComponent =
        getStoryApplicationComponent().addStoryScreenComponent(
            new StoryScreenPresenterModule(),
            new StoryScreenManagerModule(/*ObservableActivity*/ this, /*ResourceManager*/ this),
            new StoryScreenRxModule(/*LifecycleProvider*/ this));
    //@formatter:on
}
