package moe.christina.app.rakonto.core.adpter.storyEditorPages;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.SparseArray;
import android.view.ViewGroup;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.var;
import lombok.val;

import moe.christina.common.contract.Contracts;
import moe.christina.common.event.Events;
import moe.christina.common.event.generic.Event;
import moe.christina.common.event.generic.ManagedEvent;
import moe.christina.common.event.notice.NoticeEventHandler;
import moe.christina.common.extension.pager.WeakFragmentStatePagerAdapter;

import java.util.Objects;

@Accessors(prefix = "_")
public final class StoryEditorPagesAdapter extends WeakFragmentStatePagerAdapter {
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

    @Override
    public Fragment getItem(final int position) {
        final val pageFragment = super.getItem(position);

        if (pageFragment instanceof StoryEditorPage) {
            final val editorFragment = (StoryEditorPage) pageFragment;

            editorFragment.setStoryId(getStoryId());
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

    @Override
    protected void onDestroyFragment(@NonNull final Fragment fragment, final int position) {
        super.onDestroyFragment(Contracts.requireNonNull(fragment, "fragment == null"), position);

        if (fragment instanceof StoryEditorPage) {
            final val editorPage = (StoryEditorPage) fragment;

            onDestroyStoryEditorPage(editorPage, position);
        }
    }

    @Override
    protected void onInstantiateFragment(@NonNull final Fragment fragment, final int position) {
        super.onInstantiateFragment(Contracts.requireNonNull(fragment, "fragment == null"),
                                    position);

        if (fragment instanceof StoryEditorPage) {
            final val editorPage = (StoryEditorPage) fragment;

            onInstantiateStoryEditorPage(editorPage, position);
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

    protected void onInstantiateStoryEditorPage(
        @NonNull final StoryEditorPage editorFragment, final int position) {
        Contracts.requireNonNull(editorFragment, "editorFragment == null");

        var internalHandler = getInternalContentChangedHandlers().get(position);

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
        final int maxPageCount = getCount();
        for (int i = 0; i < maxPageCount; i++) {
            final val editorPage = getEditorPage(i);

            if (editorPage != null) {
                editorPage.setStoryId(getStoryId());
            }
        }
    }

    @NonNull
    private final ManagedEvent<StoryEditorPageChangedEventArgs> _contentChangedEvent =
        Events.createEvent();

    @Getter(value = AccessLevel.PRIVATE)
    @NonNull
    private final SparseArray<NoticeEventHandler> _internalContentChangedHandlers =
        new SparseArray<>();

    @Getter
    @Nullable
    private Long _storyId;
}
