package com.christina.app.story.view.fragment;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import lombok.experimental.Accessors;
import lombok.val;

import com.christina.app.story.di.StoryScreenComponentProvider;
import com.christina.app.story.di.StorySubscreenComponentProvider;
import com.christina.app.story.di.storyScreen.StoryScreenComponent;
import com.christina.app.story.di.storySubscreen.StorySubscreenComponent;
import com.christina.app.story.di.storySubscreen.module.StorySubscreenAwareModule;
import com.christina.app.story.di.storySubscreen.module.StorySubscreenManagerModule;
import com.christina.app.story.di.storySubscreen.module.StorySubscreenPresenterModule;
import com.christina.app.story.di.storySubscreen.module.StorySubscreenRxModule;
import com.christina.common.view.fragment.ScreenFragment;

@Accessors(prefix = "_")
public abstract class BaseStoryFragment extends ScreenFragment
    implements StorySubscreenComponentProvider {

    @NonNull
    @Override
    public final StorySubscreenComponent getStorySubscreenComponent() {
        if (_storySubscreenComponent == null) {
            throw new IllegalStateException("The fragment has not yet been created.");
        }

        return _storySubscreenComponent;
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
    protected void onInjectMembers() {
        super.onInjectMembers();

        _storySubscreenComponent = getStoryViewComponent().addStorySubscreenComponent(
            new StorySubscreenAwareModule(this),
            new StorySubscreenManagerModule(),
            new StorySubscreenPresenterModule(),
            new StorySubscreenRxModule(this));
    }

    @Nullable
    private StorySubscreenComponent _storySubscreenComponent;
}
