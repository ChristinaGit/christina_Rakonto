package com.christina.app.story.view.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import com.christina.app.story.core.manager.message.ActivityMessageManager;
import com.christina.app.story.core.manager.navigation.ActivityStoryNavigator;
import com.christina.app.story.di.StoryApplicationComponentProvider;
import com.christina.app.story.di.StoryViewComponentProvider;
import com.christina.app.story.di.storyApplication.StoryApplicationComponent;
import com.christina.app.story.di.storyView.StoryViewComponent;
import com.christina.app.story.di.storyView.module.StoryContentObserverModule;
import com.christina.app.story.di.storyView.module.StoryViewManagerModule;
import com.christina.app.story.di.storyView.module.StoryViewPresenterModule;
import com.christina.common.view.activity.PresentableActivity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;

@Accessors(prefix = "_")
public abstract class BaseStoryActivity extends PresentableActivity
    implements StoryViewComponentProvider {

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
    protected void onInject() {
        super.onInject();

        registerActivityListener(getActivityStoryNavigator());
    }

    @CallSuper
    @Override
    protected void onReleaseInject() {
        super.onReleaseInject();

        unregisterActivityListener(getActivityStoryNavigator());
    }

    @NonNull
    @Getter(value = AccessLevel.PRIVATE, lazy = true)
    private final ActivityMessageManager _activityMessageManager = new ActivityMessageManager(this);

    @NonNull
    @Getter(value = AccessLevel.PRIVATE, lazy = true)
    private final ActivityStoryNavigator _activityStoryNavigator = new ActivityStoryNavigator(this);

    //@formatter:off
    @NonNull
    @Getter(onMethod = @__(@Override), lazy = true)
    private final StoryViewComponent _storyViewComponent =
        getStoryApplicationComponent().addStoryViewComponent(
            new StoryContentObserverModule(),
            new StoryViewPresenterModule(),
            new StoryViewManagerModule(this,
                                       getActivityStoryNavigator(),
                                       getActivityMessageManager()));
    //@formatter:on
}
