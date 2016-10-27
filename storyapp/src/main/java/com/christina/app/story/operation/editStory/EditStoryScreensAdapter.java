package com.christina.app.story.operation.editStory;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.view.ViewGroup;

import com.christina.api.story.model.Story;
import com.christina.app.story.fragment.storyTextEditor.StoryTextEditorFragment;
import com.christina.app.story.fragment.storyTextPartsEditor.StoryTextPartsEditorFragment;
import com.christina.common.contract.Contracts;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

public class EditStoryScreensAdapter extends FragmentStatePagerAdapter {
    protected static int positionIndexer = 0;

    private static final int POSITION_TEXT_EDITOR = positionIndexer++;

    private static final int POSITION_TEXT_PARTS_EDITOR = positionIndexer++;

    public EditStoryScreensAdapter(@NonNull final FragmentManager fragmentManager) {
        super(fragmentManager);

        Contracts.requireNonNull(fragmentManager, "fragmentManager == null");
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

    @Nullable
    public Fragment getFragment(final int position) {
        final Fragment result;

        final Reference<Fragment> fragmentRef = _fragmentTags.get(position);

        if (fragmentRef != null) {
            result = fragmentRef.get();
        } else {
            result = null;
        }

        return result;
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

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final Object item = super.instantiateItem(container, position);

        if (item instanceof Fragment) {
            _fragmentTags.append(position, new WeakReference<>((Fragment) item));
        }

        return item;
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        super.destroyItem(container, position, object);

        _fragmentTags.remove(position);
    }

    @NonNull
    private final SparseArrayCompat<Reference<Fragment>> _fragmentTags =
        new SparseArrayCompat<>(positionIndexer);

    private long _storyId = Story.NO_ID;
}
