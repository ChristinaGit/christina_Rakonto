package com.christina.app.story.view.fragment;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;

import com.christina.app.story.core.manager.resource.ResourceManager;
import com.christina.app.story.di.StoryScreenComponentProvider;
import com.christina.app.story.di.StorySubscreenComponentProvider;
import com.christina.app.story.di.storyScreen.StoryScreenComponent;
import com.christina.app.story.di.storySubscreen.StorySubscreenComponent;
import com.christina.app.story.di.storySubscreen.module.StorySubscreenManagerModule;
import com.christina.app.story.di.storySubscreen.module.StorySubscreenPresenterModule;
import com.christina.app.story.di.storySubscreen.module.StorySubscreenRxModule;
import com.christina.common.event.Events;
import com.christina.common.event.notice.ManagedNoticeEvent;
import com.christina.common.event.notice.NoticeEvent;
import com.christina.common.view.fragment.ScreenFragment;

@Accessors(prefix = "_")
public abstract class BaseStoryFragment extends ScreenFragment
    implements StorySubscreenComponentProvider, ResourceManager {
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
    public final StoryScreenComponent getStoryViewComponent() {
        final val activity = getActivity();
        if (activity instanceof StoryScreenComponentProvider) {
            return ((StoryScreenComponentProvider) activity).getStoryScreenComponent();
        } else {
            throw new IllegalStateException(
                "The activity must implement " + StoryScreenComponentProvider.class.getName());
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
    private final StorySubscreenComponent _storySubscreenComponent =
        getStoryViewComponent().addStorySubscreenComponent(
            new StorySubscreenManagerModule(this),
            new StorySubscreenPresenterModule(),
            new StorySubscreenRxModule(this));
    //@formatter:on
}
