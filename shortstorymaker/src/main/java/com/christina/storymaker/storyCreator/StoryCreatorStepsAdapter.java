package com.christina.storymaker.storyCreator;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.christina.storymaker.storyCreator.step.input.StepInputFragment;
import com.christina.storymaker.storyCreator.step.splitter.StepSplitterFragment;

public class StoryCreatorStepsAdapter extends FragmentStatePagerAdapter {
    protected static int _positionIndexer = 0;

    private static final int POSITION_INPUT = _positionIndexer++;

    private static final int POSITION_SPLITTER = _positionIndexer++;

    public StoryCreatorStepsAdapter(final FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public int getCount() {
        return _positionIndexer;
    }

    @Override
    public Fragment getItem(final int position) {
        final Fragment fragment;

        if (position == POSITION_INPUT) {
            fragment = StepInputFragment.create();
        } else if (position == POSITION_SPLITTER) {
            fragment = StepSplitterFragment.create();
        } else {
            throw new IllegalArgumentException("Illegal position: " + position);
        }

        return fragment;
    }
}
