package com.christina.app.story.view.fragment;

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

import com.christina.api.story.dao.story.StoryDao;
import com.christina.api.story.model.Story;
import com.christina.api.story.observer.StoryContentObserver;
import com.christina.api.story.observer.StoryObserverEventArgs;
import com.christina.app.story.R;
import com.christina.app.story.core.StoryContentEventArgs;
import com.christina.app.story.fragment.storiesViewer.StoriesViewerSavedState;
import com.christina.app.story.fragment.storiesViewer.adapter.StoriesAdapter;
import com.christina.app.story.fragment.storiesViewer.loader.StoriesLoader;
import com.christina.app.story.fragment.storiesViewer.loader.StoriesLoaderResult;
import com.christina.app.story.operation.editStory.EditStoryActivity;
import com.christina.app.story.view.StoriesListPresentableView;
import com.christina.common.ConstantBuilder;
import com.christina.common.contract.Contracts;
import com.christina.common.data.cursor.dataCursor.DataCursor;
import com.christina.common.event.EventHandler;
import com.christina.common.view.ItemSpacingDecorator;

import javax.inject.Inject;

import butterknife.BindView;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.val;

@Accessors(prefix = "_")
public final class StoriesListFragment extends BaseStoryFragment
    implements StoriesListPresentableView {
    private static final String _KEY_SAVED_STATE =
        ConstantBuilder.savedStateKey(StoriesListFragment.class, "saved_state");

    protected static int loaderIndexer = 0;

    private static final int LOADER_ID_STORIES = loaderIndexer++;

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            _savedState = savedInstanceState.getParcelable(_KEY_SAVED_STATE);
            setSavedState(_savedState);

            if (_savedState != null) {
                _savedState.setScrollPositionRestored(false);
            }
        }

        getStoryContentObserver().onStoryChanged().addHandler(_storyChangedHandler);
    }

    @Override
    public void onResume() {
        super.onResume();

        final val storiesAdapter = getStoriesAdapter();
        storiesAdapter.onEditStory().addHandler(_editStoryHandler);
    }

    @Override
    public void onPause() {
        super.onPause();

        final val storiesAdapter = getStoriesAdapter();
        storiesAdapter.onEditStory().removeHandler(_editStoryHandler);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        getStoriesAdapter().setDataCursor(null);

        getStoryContentObserver().onStoryChanged().removeHandler(_storyChangedHandler);

        unbindViews();
    }

    @Nullable
    @Override
    public View onCreateView(
        final LayoutInflater inflater,
        @Nullable final ViewGroup container,
        @Nullable final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        final val view = inflater.inflate(R.layout.fragment_stories_list, container, false);

        bindViews(view);

        onInitializeStoriesView();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        startStoriesLoading();
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);

        final val savedState = getSavedState();
        if (outState != null && savedState != null) {
            savedState.setScrollPosition(getStoriesLayoutManager().findFirstVisibleItemPosition());
            outState.putParcelable(_KEY_SAVED_STATE, savedState);
        }
    }

    @Override
    protected void onInject() {
        super.onInject();

        getStoryScreenFragmentComponent().inject(this);
    }

    protected final void startStoriesLoading() {
        getLoaderManager().restartLoader(LOADER_ID_STORIES, null, _storiesLoaderCallbacks);
    }

    protected final void stopStoriesLoading() {
        getLoaderManager().destroyLoader(LOADER_ID_STORIES);
    }

    protected void onDeleteStory(final long storyId) {
        final boolean deleted = getStoryDao().delete(storyId) > 0;

        if (deleted) {
            if (_storiesView != null) {
                Snackbar
                    .make(_storiesView, R.string.message_story_deleted, Snackbar.LENGTH_LONG)
                    .show();
            }
        }
    }

    @CallSuper
    protected void onInitializeStoriesView() {
        if (_storiesView != null) {
            final Context context = _storiesView.getContext();
            final Resources resources = context.getResources();

            final int spacing = resources.getDimensionPixelOffset(R.dimen.card_item_grid_spacing);

            final val spacingDecorator = ItemSpacingDecorator
                .builder()
                .setSpacing(spacing)
                .collapseBorders()
                .enableEdges()
                .build();
            _storiesView.addItemDecoration(spacingDecorator);

            final val storyDismissHelper = new ItemTouchHelper(_deleteStoryOnSwipeCallback);
            storyDismissHelper.attachToRecyclerView(_storiesView);

            _storiesView.setLayoutManager(getStoriesLayoutManager());
            _storiesView.setAdapter(getStoriesAdapter());
        }
    }

    protected void onStoriesLoaded(@Nullable final DataCursor<Story> stories) {
        getStoriesAdapter().setDataCursor(stories);

        if (_storiesLoadingProgressView != null) {
            _storiesLoadingProgressView.hide();
        }

        final val savedState = getSavedState();
        if (savedState != null && !savedState.isScrollPositionRestored()) {
            final int scrollPosition = savedState.getScrollPosition();
            getStoriesLayoutManager().scrollToPositionWithOffset(scrollPosition, 0);
            savedState.setScrollPositionRestored(true);
        }
    }

    protected void onStoriesReset() {
        final val storiesAdapter = getStoriesAdapter();
        storiesAdapter.setDataCursor(null);

        if (_storiesLoadingProgressView != null) {
            _storiesLoadingProgressView.show();
        }
    }

    @BindView(R.id.stories_loading_progress)
    @Nullable
    /*package-private*/ ContentLoadingProgressBar _storiesLoadingProgressView;

    @BindView(R.id.stories_list)
    @Nullable
    /*package-private*/ RecyclerView _storiesView;

    @Inject
    @Nullable
    @Getter(AccessLevel.PROTECTED)
    /*package-private*/ StoryContentObserver _storyContentObserver;

    @Inject
    @Nullable
    @Getter(AccessLevel.PROTECTED)
    /*package-private*/ StoryDao _storyDao;

    @NonNull
    private final ItemTouchHelper.SimpleCallback _deleteStoryOnSwipeCallback =
        new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(
                final RecyclerView recyclerView,
                final RecyclerView.ViewHolder viewHolder,
                final RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, final int direction) {
                onDeleteStory(StoriesAdapter.getStoryId(viewHolder));
            }
        };

    @NonNull
    private final EventHandler<StoryContentEventArgs> _editStoryHandler =
        new EventHandler<StoryContentEventArgs>() {
            @Override
            public void onEvent(@NonNull final StoryContentEventArgs eventArgs) {
                EditStoryActivity.startEdit(getActivity(), eventArgs.getId());
            }
        };

    @Getter(value = AccessLevel.PROTECTED, lazy = true)
    @NonNull
    private final StoriesAdapter _storiesAdapter = new StoriesAdapter();

    @Getter(value = AccessLevel.PROTECTED, lazy = true)
    @NonNull
    private final GridLayoutManager _storiesLayoutManager = new GridLayoutManager(getContext(),
                                                                                  getResources()
                                                                                      .getInteger(
                                                                                      R.integer
                                                                                          .stories_viewer_column_count));

    @NonNull
    private final LoaderManager.LoaderCallbacks<StoriesLoaderResult> _storiesLoaderCallbacks =
        new LoaderManager.LoaderCallbacks<StoriesLoaderResult>() {
            @Override
            public Loader<StoriesLoaderResult> onCreateLoader(final int id, final Bundle args) {
                if (id == LOADER_ID_STORIES) {
                    return new StoriesLoader(getActivity(), _storyDao);
                } else {
                    throw new IllegalArgumentException("Illegal loader id.");
                }
            }

            @Override
            public void onLoadFinished(
                final Loader<StoriesLoaderResult> loader, final StoriesLoaderResult data) {
                onStoriesLoaded(data.getStories());
            }

            @Override
            public void onLoaderReset(final Loader<StoriesLoaderResult> loader) {
                onStoriesReset();
            }
        };

    @NonNull
    private final EventHandler<StoryObserverEventArgs> _storyChangedHandler =
        new EventHandler<StoryObserverEventArgs>() {
            @Override
            public void onEvent(@NonNull final StoryObserverEventArgs eventArgs) {
                Contracts.requireNonNull(eventArgs, "eventArgs == null");

                startStoriesLoading();
            }
        };

    @Getter
    @Setter
    @Nullable
    private StoriesViewerSavedState _savedState;
}
