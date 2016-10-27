package com.christina.app.story.fragment.singleStory;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.christina.api.story.model.Story;
import com.christina.api.story.model.StoryFrame;
import com.christina.api.story.observer.StoryContentObserver;
import com.christina.api.story.observer.StoryObserverEventArgs;
import com.christina.api.story.util.StoryPredicate;
import com.christina.app.story.fragment.BaseStoryFragment;
import com.christina.app.story.fragment.singleStory.loader.SingleStoryLoader;
import com.christina.app.story.fragment.singleStory.loader.SingleStoryLoaderResult;
import com.christina.common.ConstantBuilder;
import com.christina.common.contract.Contracts;
import com.christina.common.event.EventHandler;

import org.apache.commons.collections4.IterableUtils;

import java.util.List;

public abstract class BaseSingleStoryFragment extends BaseStoryFragment {
    private static final String KEY_SAVED_STATE =
        ConstantBuilder.savedStateKey(BaseSingleStoryFragment.class, "saved_state");

    protected static int loaderIndexer = 0;

    private static final int LOADER_ID_STORY = loaderIndexer++;

    public final long getStoryId() {
        return _storyId;
    }

    public final void setStoryId(final long storyId) {
        if (storyId != _storyId) {
            _storyId = storyId;

            onStoryIdChanged();
        }
    }

    @CallSuper
    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_SAVED_STATE)) {
            final BaseSingleStorySavedState savedState =
                savedInstanceState.getParcelable(KEY_SAVED_STATE);

            if (savedState != null) {
                _savedState = savedState;

                setStoryId(_savedState.getStoryId());
            }
        }

        final StoryContentObserver storyContentObserver = getStoryContentObserver();
        if (storyContentObserver != null) {
            storyContentObserver.onStoryChanged().addHandler(_storyChangedHandler);
            storyContentObserver.onStoryFrameChanged().addHandler(_storyFramesChangedHandler);
        }
    }

    @CallSuper
    @Override
    public void onStart() {
        super.onStart();

        if (getStory() == null) {
            startStoryLoading();
        }
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);

        if (outState != null) {
            final BaseSingleStorySavedState savedState = getSavedState();
            savedState.setStoryId(getStoryId());
            outState.putParcelable(KEY_SAVED_STATE, savedState);
        }
    }

    @CallSuper
    @Override
    public void onDestroy() {
        super.onDestroy();

        final StoryContentObserver storyContentObserver = getStoryContentObserver();
        if (storyContentObserver != null) {
            storyContentObserver.onStoryChanged().removeHandler(_storyChangedHandler);
            storyContentObserver.onStoryFrameChanged().removeHandler(_storyFramesChangedHandler);
        }
    }

    @Nullable
    protected final Story getStory() {
        return _story;
    }

    @Nullable
    protected final List<StoryFrame> getStoryFrames() {
        return _storyFrames;
    }

    protected final void startStoryLoading() {
        getLoaderManager().restartLoader(LOADER_ID_STORY, null, _storyLoaderCallbacks);
    }

    protected final void stopStoryLoading() {
        getLoaderManager().destroyLoader(LOADER_ID_STORY);
    }

    protected void onStoryContentChanged() {
        startStoryLoading();
    }

    protected void onStoryFrameContentChanged() {
        startStoryLoading();
    }

    @CallSuper
    protected void onStoryIdChanged() {

        if (isAdded() && isResumed()) {
            startStoryLoading();
        }
    }

    protected abstract void onStoryLoaded();

    protected abstract void onStoryReset();

    @Nullable
    private BaseSingleStorySavedState _savedState;

    @Nullable
    private Story _story;

    @Nullable
    private List<StoryFrame> _storyFrames;

    private long _storyId = Story.NO_ID;

    @NonNull
    private final LoaderManager.LoaderCallbacks<SingleStoryLoaderResult> _storyLoaderCallbacks =
        new LoaderManager.LoaderCallbacks<SingleStoryLoaderResult>() {
            @Override
            public Loader<SingleStoryLoaderResult> onCreateLoader(final int id, final Bundle args) {
                if (id == LOADER_ID_STORY) {
                    return new SingleStoryLoader(getActivity(), getStoryId());
                } else {
                    throw new IllegalArgumentException("Illegal loader id.");
                }
            }

            @Override
            public void onLoadFinished(final Loader<SingleStoryLoaderResult> loader,
                final SingleStoryLoaderResult data) {
                _story = data.getStory();
                _storyFrames = data.getStoryFrames();

                onStoryLoaded();
            }

            @Override
            public void onLoaderReset(final Loader<SingleStoryLoaderResult> loader) {
                _story = null;
                _storyFrames = null;

                onStoryReset();
            }
        };

    @NonNull
    private final EventHandler<StoryObserverEventArgs> _storyChangedHandler =
        new EventHandler<StoryObserverEventArgs>() {
            @Override
            public void onEvent(@NonNull final StoryObserverEventArgs eventArgs) {
                Contracts.requireNonNull(eventArgs, "eventArgs == null");

                final Story story = getStory();
                if (story != null && story.getId() == eventArgs.getId()) {
                    onStoryContentChanged();
                }
            }
        };

    @NonNull
    private final EventHandler<StoryObserverEventArgs> _storyFramesChangedHandler =
        new EventHandler<StoryObserverEventArgs>() {
            @Override
            public void onEvent(@NonNull final StoryObserverEventArgs eventArgs) {
                Contracts.requireNonNull(eventArgs, "eventArgs == null");

                final long storyId = eventArgs.getId();
                if (IterableUtils.find(getStoryFrames(),
                    StoryPredicate.StoryFrame.storyIdEquals(storyId)) != null) {
                    onStoryFrameContentChanged();
                }
            }
        };

    @NonNull
    private BaseSingleStorySavedState getSavedState() {
        if (_savedState == null) {
            _savedState = new BaseSingleStorySavedState();
        }

        return _savedState;
    }
}
