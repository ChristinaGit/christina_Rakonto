package com.christina.app.story.fragment.storiesViewer;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.christina.common.contract.Contracts;
import com.christina.common.event.EventHandler;
import com.christina.common.view.ItemSpacingDecorator;
import com.christina.api.story.dao.StoryDaoManager;
import com.christina.api.story.model.Story;
import com.christina.api.story.observer.StoryContentObserver;
import com.christina.api.story.observer.StoryObserverEventArgs;
import com.christina.app.story.R;
import com.christina.app.story.fragment.BaseStoryFragment;
import com.christina.app.story.fragment.storiesViewer.adapter.StoriesAdapter;
import com.christina.app.story.fragment.storiesViewer.loader.StoriesLoader;
import com.christina.app.story.fragment.storiesViewer.loader.StoriesLoaderResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class StoriesViewerFragment extends BaseStoryFragment {
    private static final String KEY_SAVED_STATE = "saved_state";

    protected static int _loaderIndexer = 0;

    private static final int LOADER_ID_STORIES = _loaderIndexer++;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container,
        @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_stories_viewer, container, false);

        _storiesLoadingProgressView =
            (ContentLoadingProgressBar) view.findViewById(R.id.stories_loading_progress);
        _storiesView = (RecyclerView) view.findViewById(R.id.stories_list);
        onInitializeStoriesView(_storiesView);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            _savedState = savedInstanceState.getParcelable(KEY_SAVED_STATE);
            getSavedState().setScrollPositionRestored(false);
        }

        final StoryContentObserver storyContentObserver = getStoryContentObserver();
        if (storyContentObserver != null) {
            storyContentObserver.onStoryChanged().addHandler(_storyChangedHandler);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getStories() == null) {
            startStoriesLoading();
        }
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);

        if (outState != null) {
            final StoriesViewerSavedState savedState = getSavedState();
            savedState.setScrollPosition(getStoriesLayoutManager().findFirstVisibleItemPosition());
            outState.putParcelable(KEY_SAVED_STATE, savedState);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        final StoryContentObserver storyContentObserver = getStoryContentObserver();
        if (storyContentObserver != null) {
            storyContentObserver.onStoryChanged().removeHandler(_storyChangedHandler);
        }
    }

    @NonNull
    protected final StoriesViewerSavedState getSavedState() {
        return _savedState;
    }

    @Nullable
    protected final List<Story> getStories() {
        return _stories;
    }

    @NonNull
    protected final StoriesAdapter getStoriesAdapter() {
        if (_storiesAdapter == null) {
            _storiesAdapter = new StoriesAdapter();
        }
        return _storiesAdapter;
    }

    @NonNull
    protected final GridLayoutManager getStoriesLayoutManager() {
        if (_storiesLayoutManager == null) {
            final int columnCount =
                getResources().getInteger(R.integer.stories_viewer_column_count);
            _storiesLayoutManager = new GridLayoutManager(getContext(), columnCount);
        }

        return _storiesLayoutManager;
    }

    protected final void startStoriesLoading() {
        getLoaderManager().restartLoader(LOADER_ID_STORIES, null, _storiesLoaderCallbacks);
    }

    protected final void stopStoriesLoading() {
        getLoaderManager().destroyLoader(LOADER_ID_STORIES);
    }

    protected void onDeleteStory(final int position) {
        final StoriesAdapter storiesAdapter = getStoriesAdapter();

        final long itemId = storiesAdapter.getItemId(position);
        storiesAdapter.removeItem(position);

        _deletedStoriesIds.add(itemId);

        StoryDaoManager.getStoryDao().delete(itemId);

        if (_storiesView != null) {
            Snackbar
                .make(_storiesView, R.string.message_story_deleted, Snackbar.LENGTH_LONG)
                .show();
        }
    }

    @CallSuper
    protected void onInitializeStoriesView(@NonNull final RecyclerView storiesView) {
        Contracts.requireNonNull(storiesView, "storiesView == null");

        final Context context = storiesView.getContext();
        final Resources resources = context.getResources();

        final int spacing = resources.getDimensionPixelOffset(R.dimen.card_item_grid_spacing);

        final ItemSpacingDecorator spacingDecorator = new ItemSpacingDecorator.Builder()
            .setSpacing(spacing)
            .collapseBorders()
            .enableEdges()
            .build();
        storiesView.addItemDecoration(spacingDecorator);

        final ItemTouchHelper storyDismissHelper = new ItemTouchHelper(_deleteStoryOnSwipeCallback);
        storyDismissHelper.attachToRecyclerView(_storiesView);

        storiesView.setLayoutManager(getStoriesLayoutManager());
        storiesView.setAdapter(getStoriesAdapter());
    }

    protected void onStoriesLoaded() {
        final List<Story> stories = getStories();
        if (stories != null) {
            getStoriesAdapter().setItems(stories);
        } else {
            getStoriesAdapter().removeItems();
        }

        if (_storiesLoadingProgressView != null) {
            _storiesLoadingProgressView.hide();
        }

        final StoriesViewerSavedState savedState = getSavedState();
        if (!savedState.isScrollPositionRestored()) {
            final int scrollPosition = savedState.getScrollPosition();
            getStoriesLayoutManager().scrollToPositionWithOffset(scrollPosition, 0);
            savedState.setScrollPositionRestored(true);
        }
    }

    protected void onStoriesReset() {
        final StoriesAdapter storiesAdapter = getStoriesAdapter();
        storiesAdapter.removeItems(false);
        storiesAdapter.notifyDataSetChanged();

        if (_storiesLoadingProgressView != null) {
            _storiesLoadingProgressView.show();
        }
    }

    @NonNull
    private final Collection<Long> _deletedStoriesIds = new ArrayList<>();

    @NonNull
    private final EventHandler<StoryObserverEventArgs> _storyChangedHandler =
        new EventHandler<StoryObserverEventArgs>() {
            @Override
            public void onEvent(@NonNull final StoryObserverEventArgs eventArgs) {
                Contracts.requireNonNull(eventArgs, "eventArgs == null");

                if (!_deletedStoriesIds.remove(eventArgs.getId())) {
                    startStoriesLoading();
                }
            }
        };

    private StoriesViewerSavedState _savedState = new StoriesViewerSavedState();

    @Nullable
    private List<Story> _stories;

    @Nullable
    private StoriesAdapter _storiesAdapter;

    @Nullable
    private GridLayoutManager _storiesLayoutManager;

    @Nullable
    private ContentLoadingProgressBar _storiesLoadingProgressView;

    @NonNull
    private final LoaderManager.LoaderCallbacks<StoriesLoaderResult> _storiesLoaderCallbacks =
        new LoaderManager.LoaderCallbacks<StoriesLoaderResult>() {
            @Override
            public Loader<StoriesLoaderResult> onCreateLoader(final int id, final Bundle args) {
                if (id == LOADER_ID_STORIES) {
                    return new StoriesLoader(getActivity());
                } else {
                    throw new IllegalArgumentException("Illegal loader id.");
                }
            }

            @Override
            public void onLoadFinished(final Loader<StoriesLoaderResult> loader,
                final StoriesLoaderResult data) {
                _stories = data.getStories();

                onStoriesLoaded();
            }

            @Override
            public void onLoaderReset(final Loader<StoriesLoaderResult> loader) {
                _stories = null;

                onStoriesReset();
            }
        };

    @Nullable
    private RecyclerView _storiesView;

    @NonNull
    private final ItemTouchHelper.SimpleCallback _deleteStoryOnSwipeCallback =
        new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(final RecyclerView recyclerView,
                final RecyclerView.ViewHolder viewHolder, final RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, final int direction) {
                onDeleteStory(viewHolder.getAdapterPosition());
            }
        };
}
