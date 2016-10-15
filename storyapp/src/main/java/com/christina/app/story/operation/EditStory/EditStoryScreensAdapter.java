package com.christina.app.story.operation.editStory;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.christina.api.story.model.Story;
import com.christina.app.story.fragment.storyTextEditor.StoryTextEditorFragment;
import com.christina.app.story.fragment.storyTextPartsEditor.StoryTextPartsEditorFragment;

public class EditStoryScreensAdapter extends FragmentStatePagerAdapter {
    protected static int positionIndexer = 0;

    private static final int POSITION_TEXT_EDITOR = positionIndexer++;

    private static final int POSITION_TEXT_PARTS_EDITOR = positionIndexer++;

    public EditStoryScreensAdapter(final FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    public final long getStoryId() {
        return _storyId;
    }

    public void setStoryId(final long storyId) {
        _storyId = storyId;
    }

    @Override
    public int getCount() {
        return positionIndexer;
    }

    @Override
    public Fragment getItem(final int position) {
        final Fragment fragment;

        if (position == POSITION_TEXT_EDITOR) {
            final StoryTextEditorFragment stepFragment = new StoryTextEditorFragment();
            stepFragment.setStoryId(getStoryId());
            fragment = stepFragment;
        } else if (position == POSITION_TEXT_PARTS_EDITOR) {
            final StoryTextPartsEditorFragment stepFragment = new StoryTextPartsEditorFragment();
            stepFragment.setStoryId(getStoryId());
            fragment = stepFragment;
        } else {
            throw new IllegalArgumentException("Illegal position: " + position);
        }

        return fragment;
    }

    private long _storyId = Story.NO_ID;
}
