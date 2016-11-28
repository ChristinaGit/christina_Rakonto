package com.christina.app.story.view.fragment;

import android.support.annotation.NonNull;

import com.christina.app.story.di.StoryScreenComponentProvider;
import com.christina.app.story.di.StoryScreenFragmentComponentProvider;
import com.christina.app.story.di.storyView.StoryViewComponent;
import com.christina.app.story.di.storyViewFragment.StoryViewFragmentComponent;
import com.christina.common.view.fragment.PresentableFragment;

import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;

@Accessors(prefix = "_")
public abstract class BaseStoryFragment extends PresentableFragment
    implements StoryScreenFragmentComponentProvider {

    @NonNull
    public final StoryViewComponent getStoryScreenComponent() {
        final val application = getActivity();
        if (application instanceof StoryScreenComponentProvider) {
            return ((StoryScreenComponentProvider) application).getStoryViewComponent();
        } else {
            throw new IllegalStateException(
                "The activity must implement " + StoryScreenComponentProvider.class.getName());
        }
    }

    @NonNull
    @Getter(onMethod = @__(@Override), lazy = true)
    private final StoryViewFragmentComponent _storyScreenFragmentComponent =
        getStoryScreenComponent().addStoryScreenFragmentComponent();
}
