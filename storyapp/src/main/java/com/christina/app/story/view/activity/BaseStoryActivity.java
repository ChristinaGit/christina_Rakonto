package com.christina.app.story.view.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import lombok.experimental.Accessors;
import lombok.val;

import com.christina.app.story.di.StoryApplicationComponentProvider;
import com.christina.app.story.di.StoryScreenComponentProvider;
import com.christina.app.story.di.storyApplication.StoryApplicationComponent;
import com.christina.app.story.di.storyScreen.StoryScreenComponent;
import com.christina.app.story.di.storyScreen.module.StoryScreenAdviserModule;
import com.christina.app.story.di.storyScreen.module.StoryScreenManagerModule;
import com.christina.app.story.di.storyScreen.module.StoryScreenPresenterModule;
import com.christina.app.story.di.storyScreen.module.StoryScreenRxModule;
import com.christina.common.view.activity.ScreenActivity;

@Accessors(prefix = "_")
public abstract class BaseStoryActivity extends ScreenActivity
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

    @NonNull
    @Override
    public final StoryScreenComponent getStoryScreenComponent() {
        if (_storyScreenComponent == null) {
            throw new IllegalStateException("The activity has not yet been created.");
        }

        return _storyScreenComponent;
    }

    @CallSuper
    @Override
    protected void onInjectMembers() {
        super.onInjectMembers();

        _storyScreenComponent = getStoryApplicationComponent().addStoryScreenComponent(
            new StoryScreenAdviserModule(this),
            new StoryScreenPresenterModule(),
            new StoryScreenManagerModule(/*ObservableActivity*/ this),
            new StoryScreenRxModule(/*LifecycleProvider*/ this));
    }

    @Nullable
    private StoryScreenComponent _storyScreenComponent;
}
