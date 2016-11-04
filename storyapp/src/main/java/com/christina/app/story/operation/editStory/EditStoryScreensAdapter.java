package com.christina.app.story.operation.editStory;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.view.ViewGroup;

import com.christina.api.story.model.Story;
import com.christina.app.story.fragment.StoryEditorFragment;
import com.christina.app.story.fragment.storyFramesEditor.StoryFramesEditorFragment;
import com.christina.app.story.fragment.storyTextEditor.StoryTextEditorFragment;
import com.christina.common.contract.Contracts;
import com.christina.common.event.BaseEvent;
import com.christina.common.event.Event;
import com.christina.common.event.NoticeEventHandler;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

public class EditStoryScreensAdapter extends FragmentStatePagerAdapter {
    protected static int positionIndexer = 0;

    public static final int POSITION_TEXT_EDITOR = positionIndexer++;

    public static final int POSITION_FRAMES_EDITOR = positionIndexer++;

    public EditStoryScreensAdapter(@NonNull final FragmentManager fragmentManager) {
        super(fragmentManager);

        Contracts.requireNonNull(fragmentManager, "fragmentManager == null");
    }

    @NonNull
    public final Event<Integer> onContentChanged() {
        return _contentChanged;
    }

    public final long getStoryId() {
        return _storyId;
    }

    public final void setStoryId(final long storyId) {
        _storyId = storyId;
    }

    @Override
    public int getCount() {
        return positionIndexer;
    }

    @Nullable
    public StoryEditorFragment getEditorFragment(final int position) {
        final StoryEditorFragment editorFragment;

        final Fragment fragment = getFragment(position);

        if (fragment instanceof StoryEditorFragment) {
            editorFragment = (StoryEditorFragment) fragment;
        } else {
            editorFragment = null;
        }

        return editorFragment;
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
        } else if (position == POSITION_FRAMES_EDITOR) {
            final StoryFramesEditorFragment stepFragment = new StoryFramesEditorFragment();
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
            onInstantiateFragment((Fragment) item, position);
        }

        return item;
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        super.destroyItem(container, position, object);

        if (object instanceof Fragment) {
            onDestroyFragment((Fragment) object, position);
        }
    }

    protected void onDestroyFragment(@NonNull final Fragment fragment, final int position) {
        Contracts.requireNonNull(fragment, "fragment == null");

        _fragmentTags.remove(position);

        if (fragment instanceof StoryEditorFragment) {
            onDestroyStoryEditorFragment((StoryEditorFragment) fragment, position);
        }
    }

    protected void onDestroyStoryEditorFragment(@NonNull final StoryEditorFragment editorFragment,
        final int position) {
        Contracts.requireNonNull(editorFragment, "editorFragment == null");

        final NoticeEventHandler internalHandler = _internalContentChangedHandlers.get(position);
        if (internalHandler != null) {
            editorFragment.onContentChanged().removeHandler(internalHandler);
            _internalContentChangedHandlers.remove(position);
        }
    }

    protected void onInstantiateFragment(@NonNull final Fragment fragment, final int position) {
        Contracts.requireNonNull(fragment, "fragment == null");

        _fragmentTags.append(position, new WeakReference<>(fragment));

        if (fragment instanceof StoryEditorFragment) {
            onInstantiateStoryEditorFragment((StoryEditorFragment) fragment, position);
        }
    }

    protected void onInstantiateStoryEditorFragment(
        @NonNull final StoryEditorFragment editorFragment, final int position) {
        Contracts.requireNonNull(editorFragment, "editorFragment == null");

        NoticeEventHandler internalHandler = _internalContentChangedHandlers.get(position);

        if (internalHandler == null) {
            internalHandler = new NoticeEventHandler() {
                @Override
                public void onEvent() {
                    _contentChanged.rise(position);
                }
            };
            _internalContentChangedHandlers.append(position, internalHandler);
        }
        editorFragment.onContentChanged().addHandler(internalHandler);
    }

    @NonNull
    private final BaseEvent<Integer> _contentChanged = new BaseEvent<>();

    @NonNull
    private final SparseArrayCompat<Reference<Fragment>> _fragmentTags =
        new SparseArrayCompat<>(positionIndexer);

    @NonNull
    private final SparseArrayCompat<NoticeEventHandler> _internalContentChangedHandlers =
        new SparseArrayCompat<>(positionIndexer);

    private long _storyId = Story.NO_ID;
}
