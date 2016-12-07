package com.christina.app.story.view.fragment;

import android.support.annotation.NonNull;

import com.christina.app.story.di.StoryViewComponentProvider;
import com.christina.app.story.di.StoryViewFragmentComponentProvider;
import com.christina.app.story.di.storyView.StoryViewComponent;
import com.christina.app.story.di.storyViewFragment.StoryViewFragmentComponent;
import com.christina.app.story.di.storyViewFragment.module.StoryViewFragmentPresenterModule;
import com.christina.common.view.fragment.PresentableFragment;

import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;

@Accessors(prefix = "_")
public abstract class BaseStoryFragment extends PresentableFragment
    implements StoryViewFragmentComponentProvider {

    @NonNull
    public final StoryViewComponent getStoryViewComponent() {
        final val activity = getActivity();
        if (activity instanceof StoryViewComponentProvider) {
            return ((StoryViewComponentProvider) activity).getStoryViewComponent();
        } else {
            throw new IllegalStateException(
                "The activity must implement " + StoryViewComponentProvider.class.getName());
        }
    }

    @NonNull
    @Getter(onMethod = @__(@Override), lazy = true)
    private final StoryViewFragmentComponent _storyViewFragmentComponent =
        getStoryViewComponent().addStoryViewFragmentComponent(new StoryViewFragmentPresenterModule());
}
