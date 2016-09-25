package com.christina.storymaker.storiesViewer;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.christina.common.event.EventHandler;
import com.christina.content.story.dao.StoryDaoManager;
import com.christina.content.story.model.Story;
import com.christina.content.story.observer.StoryContentObserver;
import com.christina.content.story.observer.StoryObserverEventArgs;
import com.christina.storymaker.R;
import com.christina.storymaker.core.StoryContentObserverProvider;
import com.christina.storymaker.storiesViewer.adapter.StoryListAdapter;
import com.christina.storymaker.storiesViewer.loader.StoriesLoader;
import com.christina.storymaker.storiesViewer.loader.StoriesLoaderResult;
import com.christina.storymaker.view.ListItemMarginDecorator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StoriesViewerFragment extends Fragment {
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
        return inflater.inflate(R.layout.story_list_viewer, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        _storyListView = ((RecyclerView) view.findViewById(R.id.story_list));
        _onInitializeStoryListView(_storyListView);
        _storyListAdapter = new StoryListAdapter();
        _storyListView.setAdapter(_storyListAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        startStoriesLoading();

        final StoryContentObserver storyContentObserver = getStoryContentObserver();
        if (storyContentObserver != null) {
            storyContentObserver.onStoryChanged().addHandler(_storyChangedHandler);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        stopStoriesLoading();

        final StoryContentObserver storyContentObserver = getStoryContentObserver();
        if (storyContentObserver != null) {
            storyContentObserver.onStoryChanged().removeHandler(_storyChangedHandler);
        }
    }

    @Nullable
    protected final List<Story> getStories() {
        return _stories;
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

    protected void onDeleteStory(int position) {
        if (_storyListAdapter != null) {
            final long itemId = _storyListAdapter.getItemId(position);

            _storyListAdapter.removeItem(position);

            _deletedStoriesIds.add(itemId);

            StoryDaoManager.getStoryDao().delete(itemId);

            if (_storyListView != null) {
                Snackbar.make(_storyListView, R.string.message_story_deleted, Snackbar.LENGTH_LONG)
                        .show();
            }
        }
    }

    protected void onStoriesLoaded() {
        if (_storyListAdapter != null) {
            final List<Story> stories = getStories();
            if (stories != null) {
                _storyListAdapter.setItems(stories);
            } else {
                _storyListAdapter.removeItems();
            }
        }
    }

    @NonNull
    private Collection<Long> _deletedStoriesIds = new ArrayList<>();

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

    @Nullable
    private List<Story> _stories;

    @Nullable
    private StoryListAdapter _storyListAdapter;

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
            }
        };

    @Nullable
    private RecyclerView _storyListView;

    @NonNull
    private final ItemTouchHelper.SimpleCallback _deleteStoryOnSwipeCallback =
        new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(final RecyclerView recyclerView,
                                  final RecyclerView.ViewHolder viewHolder,
                                  final RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, final int direction) {
                onDeleteStory(viewHolder.getAdapterPosition());
            }
        };

    private void _onInitializeStoryListView(@NonNull RecyclerView storyListView) {
        final Context context = storyListView.getContext();
        final Resources resources = context.getResources();

        final int verticalMargin =
            resources.getDimensionPixelOffset(R.dimen.list_item_vertical_margin);
        final int horizontalMargin =
            resources.getDimensionPixelOffset(R.dimen.list_item_horizontal_margin);

        storyListView.addItemDecoration(
            new ListItemMarginDecorator(verticalMargin, horizontalMargin));

        final ItemTouchHelper storyDismissHelper = new ItemTouchHelper(_deleteStoryOnSwipeCallback);
        storyDismissHelper.attachToRecyclerView(_storyListView);

        storyListView.setLayoutManager(new LinearLayoutManager(context));
    }
}
