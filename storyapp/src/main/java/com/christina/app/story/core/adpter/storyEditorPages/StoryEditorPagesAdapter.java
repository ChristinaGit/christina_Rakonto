package com.christina.app.story.core.adpter.storyEditorPages;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.christina.common.contract.Contracts;
import com.christina.common.event.BaseEvent;
import com.christina.common.event.Event;
import com.christina.common.event.NoticeEventHandler;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.val;

@Accessors(prefix = "_")
public final class StoryEditorPagesAdapter extends FragmentStatePagerAdapter {
    public StoryEditorPagesAdapter(@NonNull final FragmentManager fragmentManager) {
        super(Contracts.requireNonNull(fragmentManager, "fragmentManager == null"));
    }

    @NonNull
    public final Event<StoryEditorPageContentChangedEventArgs> getOnContentChangedEvent() {
        return _onContentChangedEvent;
    }

    public final void setDisplayedStoryId(final long displayedStoryId) {
        if (_displayedStoryId != displayedStoryId) {
            _displayedStoryId = displayedStoryId;

            onDisplayedStoryChanged();
        }
    }

    @Override
    public int getCount() {
        final int count;

        final val pageFactory = getPageFactory();
        if (pageFactory != null) {
            count = pageFactory.getPageCount();
        } else {
            count = 0;
        }

        return count;
    }

    @Nullable
    public StoryEditorPage getEditorPage(final int position) {
        final StoryEditorPage editorPage;

        final val fragment = getFragment(position);

        if (fragment instanceof StoryEditorPage) {
            editorPage = (StoryEditorPage) fragment;
        } else {
            editorPage = null;
        }

        return editorPage;
    }

    @Nullable
    public Fragment getFragment(final int position) {
        final Fragment result;

        final Reference<Fragment> fragmentRef = getFragments().get(position);

        if (fragmentRef != null) {
            result = fragmentRef.get();
        } else {
            result = null;
        }

        return result;
    }

    @Override
    public Fragment getItem(final int position) {
        if (0 <= position && position < getCount()) {
            final Fragment pageFragment;

            final val pageFactory = getPageFactory();

            if (pageFactory != null) {
                pageFragment = pageFactory.create(position);

                if (pageFragment instanceof StoryEditorPage) {
                    final val editorFragment = (StoryEditorPage) pageFragment;

                    editorFragment.setEditedStoryId(getDisplayedStoryId());
                }
            } else {
                pageFragment = null;
            }

            return pageFragment;
        } else {
            throw new IllegalArgumentException("Illegal position: " + position);
        }
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final Object item = super.instantiateItem(container, position);

        if (item instanceof Fragment) {
            final val fragment = (Fragment) item;

            onInstantiateFragment(fragment, position);
        }

        return item;
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        super.destroyItem(container, position, object);

        if (object instanceof Fragment) {
            final val fragment = (Fragment) object;

            onDestroyFragment(fragment, position);
        }
    }

    protected void onDestroyFragment(@NonNull final Fragment fragment, final int position) {
        Contracts.requireNonNull(fragment, "fragment == null");

        getFragments().remove(position);

        if (fragment instanceof StoryEditorPage) {
            final val editorPage = (StoryEditorPage) fragment;

            onDestroyStoryEditorPage(editorPage, position);
        }
    }

    protected void onDestroyStoryEditorPage(
        @NonNull final StoryEditorPage editorFragment, final int position) {
        Contracts.requireNonNull(editorFragment, "editorFragment == null");

        final val internalHandler = getInternalContentChangedHandlers().get(position);
        if (internalHandler != null) {
            editorFragment.getOnContentChangedEvent().removeHandler(internalHandler);
            getInternalContentChangedHandlers().remove(position);
        }
    }

    protected void onDisplayedStoryChanged() {
        final val fragments = getFragments();

        for (int i = 0; i < fragments.size(); i++) {
            final val fragmentRef = fragments.get(fragments.keyAt(i));

            if (fragmentRef != null) {
                final val fragment = fragmentRef.get();

                if (fragment instanceof StoryEditorPage) {
                    final val editorPage = (StoryEditorPage) fragment;

                    editorPage.setEditedStoryId(getDisplayedStoryId());
                }
            }
        }
    }

    protected void onInstantiateFragment(@NonNull final Fragment fragment, final int position) {
        Contracts.requireNonNull(fragment, "fragment == null");

        getFragments().append(position, new WeakReference<>(fragment));

        if (fragment instanceof StoryEditorPage) {
            final val editorPage = (StoryEditorPage) fragment;

            onInstantiateStoryEditorPage(editorPage, position);
        }
    }

    protected void onInstantiateStoryEditorPage(
        @NonNull final StoryEditorPage editorFragment, final int position) {
        Contracts.requireNonNull(editorFragment, "editorFragment == null");

        NoticeEventHandler internalHandler = getInternalContentChangedHandlers().get(position);

        if (internalHandler == null) {
            internalHandler = new NoticeEventHandler() {
                @Override
                public void onEvent() {
                    final val eventArgs = new StoryEditorPageContentChangedEventArgs(position);
                    _onContentChangedEvent.rise(eventArgs);
                }
            };
            getInternalContentChangedHandlers().append(position, internalHandler);
        }
        editorFragment.getOnContentChangedEvent().addHandler(internalHandler);
    }

    @Getter(value = AccessLevel.PRIVATE, lazy = true)
    @NonNull
    private final SparseArray<Reference<Fragment>> _fragments = new SparseArray<>();

    @Getter(value = AccessLevel.PRIVATE, lazy = true)
    @NonNull
    private final SparseArray<NoticeEventHandler> _internalContentChangedHandlers =
        new SparseArray<>();

    @NonNull
    private final BaseEvent<StoryEditorPageContentChangedEventArgs> _onContentChangedEvent =
        new BaseEvent<>();

    @Getter
    private long _displayedStoryId;

    @Getter
    @Setter
    @Nullable
    private StoryEditorPageFactory _pageFactory;
}
