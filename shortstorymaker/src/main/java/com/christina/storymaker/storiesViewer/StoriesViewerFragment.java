package com.christina.storymaker.storiesViewer;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
import com.christina.content.story.dao.StoryDaoManager;
import com.christina.content.story.model.Story;
import com.christina.content.story.observer.StoryContentObserver;
import com.christina.content.story.observer.StoryObserverEventArgs;
import com.christina.storymaker.R;
import com.christina.storymaker.core.StoryContentObserverProvider;
import com.christina.storymaker.storiesViewer.adapter.StoriesAdapter;
import com.christina.storymaker.storiesViewer.loader.StoriesLoader;
import com.christina.storymaker.storiesViewer.loader.StoriesLoaderResult;
import com.christina.storymaker.view.ListItemMarginDecorator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class StoriesViewerFragment extends Fragment {
    private static final String KEY_SAVED_STATE = "saved_state";

    protected static int _loaderIndexer = 0;

    private static final int LOADER_ID_STORIES = _loaderIndexer++;

    @NonNull
    public static StoriesViewerFragment create() {
        return new StoriesViewerFragment();
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container,
        @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.stories_viewer, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        _storiesView = (RecyclerView) view.findViewById(R.id.story_list);
        onInitializeStoriesView(_storiesView);

        _storyLoadingProgressView =
            (ContentLoadingProgressBar) view.findViewById(R.id.story_loading_progress);
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            _savedState = savedInstanceState.getParcelable(KEY_SAVED_STATE);
            getSavedState().setScrollPositionRestored(false);
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
    public void onResume() {
        super.onResume();

        final StoryContentObserver storyContentObserver = getStoryContentObserver();
        if (storyContentObserver != null) {
            storyContentObserver.onStoryChanged().addHandler(_storyChangedHandler);
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
    public void onPause() {
        super.onPause();

        final StoryContentObserver storyContentObserver = getStoryContentObserver();
        if (storyContentObserver != null) {
            storyContentObserver.onStoryChanged().removeHandler(_storyChangedHandler);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        stopStoriesLoading();
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

    @Nullable
    protected final StoryContentObserver getStoryContentObserver() {
        StoryContentObserver result = null;

        final Activity activity = getActivity();
        if (activity instanceof StoryContentObserverProvider) {
            result = ((StoryContentObserverProvider) activity).getStoryContentObserver();
        }

        return result;
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

        final int verticalMargin =
            resources.getDimensionPixelOffset(R.dimen.list_item_vertical_margin);
        final int horizontalMargin =
            resources.getDimensionPixelOffset(R.dimen.list_item_horizontal_margin);

        storiesView.addItemDecoration(
            new ListItemMarginDecorator(verticalMargin, horizontalMargin));

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

        if (_storyLoadingProgressView != null) {
            _storyLoadingProgressView.hide();
        }

        final StoriesViewerSavedState savedState = getSavedState();
        if (!savedState.isScrollPositionRestored()) {
            final int scrollPosition = savedState.getScrollPosition();
            getStoriesLayoutManager().scrollToPosition(scrollPosition);
            savedState.setScrollPositionRestored(true);
        }
    }

    protected void onStoriesReset() {
        final StoriesAdapter storiesAdapter = getStoriesAdapter();
        storiesAdapter.removeItems(false);
        storiesAdapter.notifyDataSetChanged();

        if (_storyLoadingProgressView != null) {
            _storyLoadingProgressView.show();
        }
    }

    @NonNull
    private final Collection<Long> _deletedStoriesIds = new ArrayList<>();

    @NonNull
    private final EventHandler<StoryObserverEventArgs> _storyChangedHandler =
        new EventHandler<StoryObserverEventArgs>() {
            @Override
            public void onEvent(@NonNull final StoryObserverEventArgs eventArgs) {
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

    @Nullable
    private ContentLoadingProgressBar _storyLoadingProgressView;

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
}
