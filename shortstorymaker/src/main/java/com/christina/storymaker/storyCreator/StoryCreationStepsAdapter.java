package com.christina.storymaker.storyCreator;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class StoryCreationStepsAdapter extends FragmentStatePagerAdapter {
    public StoryCreationStepsAdapter(@NonNull final FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Nullable
    public final List<Fragment> getCreationSteps() {
        return _creationSteps;
    }

    public final void setCreationSteps(@Nullable final List<Fragment> creationSteps) {
        _creationSteps = creationSteps;
    }

    @Override
    public int getCount() {
        final int count;

        final List<Fragment> creationSteps = getCreationSteps();
        if (creationSteps != null) {
            count = creationSteps.size();
        } else {
            count = 0;
        }

        return count;
    }

    @Nullable
    @Override
    public Fragment getItem(final int position) {
        final Fragment fragment;

        final List<Fragment> creationSteps = getCreationSteps();
        if (creationSteps != null) {
            fragment = creationSteps.get(position);
        } else {
            fragment = null;
        }

        return fragment;
    }

    @Nullable
    private List<Fragment> _creationSteps;
}