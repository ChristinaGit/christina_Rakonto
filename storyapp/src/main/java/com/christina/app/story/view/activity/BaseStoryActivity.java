package com.christina.app.story.view.activity;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.christina.app.story.di.StoryApplicationComponentProvider;
import com.christina.app.story.di.StoryScreenComponentProvider;
import com.christina.app.story.di.storyApplication.StoryApplicationComponent;
import com.christina.app.story.di.storyView.StoryViewComponent;
import com.christina.app.story.di.storyView.module.StoryContentObserverModule;
import com.christina.app.story.di.storyView.module.StoryPresenterModule;
import com.christina.app.story.di.storyView.module.StoryViewManagerModule;
import com.christina.app.story.manager.navigation.ActivityStoryNavigator;
import com.christina.app.story.manager.message.ActivityMessageManager;
import com.christina.common.view.activity.PresentableActivity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.val;

@Accessors(prefix = "_")
public abstract class BaseStoryActivity extends PresentableActivity
    implements StoryScreenComponentProvider {

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

    @Override
    protected void onInject() {
        super.onInject();

        registerActivityListener(getActivityStoryNavigator());
    }

    @Override
    protected void onReleaseInject() {
        super.onReleaseInject();

        unregisterActivityListener(getActivityStoryNavigator());
    }

    @NonNull
    @Getter(value = AccessLevel.PRIVATE, lazy = true)
    private final ActivityMessageManager _activityMessageManager =
        new ActivityMessageManager(getSnackbarParentView());

    @NonNull
    @Getter(value = AccessLevel.PRIVATE, lazy = true)
    private final ActivityStoryNavigator _activityStoryNavigator = new ActivityStoryNavigator(this);

    @NonNull
    @Getter(onMethod = @__(@Override), lazy = true)
    private final StoryViewComponent _storyViewComponent =
        getStoryApplicationComponent().addStoryScreenComponent(
            new StoryContentObserverModule(),
            new StoryPresenterModule(),
            new StoryViewManagerModule(getActivityStoryNavigator(), getActivityMessageManager()));

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    @Nullable
    private View _snackbarParentView;
}
