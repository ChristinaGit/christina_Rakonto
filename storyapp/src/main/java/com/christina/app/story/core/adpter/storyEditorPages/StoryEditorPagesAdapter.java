package com.christina.app.story.core.adpter.storyEditorPages;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.val;

import com.christina.common.contract.Contracts;
import com.christina.common.event.Events;
import com.christina.common.event.generic.Event;
import com.christina.common.event.generic.ManagedEvent;
import com.christina.common.event.notice.NoticeEventHandler;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Objects;

@Accessors(prefix = "_")
public final class StoryEditorPagesAdapter extends FragmentStatePagerAdapter {
    public StoryEditorPagesAdapter(@NonNull final FragmentManager fragmentManager) {
        super(Contracts.requireNonNull(fragmentManager, "fragmentManager == null"));
    }

    @NonNull
    public final Event<StoryEditorPageChangedEventArgs> getContentChangedEvent() {
        return _contentChangedEvent;
    }

    public final void setStoryId(@Nullable final Long storyId) {
        if (!Objects.equals(_storyId, storyId)) {
            _storyId = storyId;

            onStoryIdChanged();
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
        Contracts.requireInRange(position, 0, getCount() - 1, new IndexOutOfBoundsException());

        final Fragment pageFragment;

        final val pageFactory = getPageFactory();

        if (pageFactory != null) {
            pageFragment = pageFactory.createPageFragment(position);

            if (pageFragment instanceof StoryEditorPage) {
                final val editorFragment = (StoryEditorPage) pageFragment;

                editorFragment.setStoryId(getStoryId());
            }
        } else {
            pageFragment = null;
        }

        return pageFragment;
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
            editorFragment.getContentChangedEvent().removeHandler(internalHandler);
            getInternalContentChangedHandlers().remove(position);
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
                    final val eventArgs = new StoryEditorPageChangedEventArgs(position);
                    _contentChangedEvent.rise(eventArgs);
                }
            };
            getInternalContentChangedHandlers().append(position, internalHandler);
        }
        editorFragment.getContentChangedEvent().addHandler(internalHandler);
    }

    protected void onStoryIdChanged() {
        final val fragments = getFragments();

        for (int i = 0; i < fragments.size(); i++) {
            final val fragmentRef = fragments.get(fragments.keyAt(i));

            if (fragmentRef != null) {
                final val fragment = fragmentRef.get();

                if (fragment instanceof StoryEditorPage) {
                    final val editorPage = (StoryEditorPage) fragment;

                    editorPage.setStoryId(getStoryId());
                }
            }
        }
    }

    @NonNull
    private final ManagedEvent<StoryEditorPageChangedEventArgs> _contentChangedEvent =
        Events.createEvent();

    @Getter(value = AccessLevel.PRIVATE)
    @NonNull
    private final SparseArray<Reference<Fragment>> _fragments = new SparseArray<>();

    @Getter(value = AccessLevel.PRIVATE)
    @NonNull
    private final SparseArray<NoticeEventHandler> _internalContentChangedHandlers =
        new SparseArray<>();

    @Getter
    @Setter
    @Nullable
    private StoryEditorPageFactory _pageFactory;

    @Getter
    @Nullable
    private Long _storyId;
}
