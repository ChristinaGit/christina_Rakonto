package com.christina.app.story.view.fragment.storiesList;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.christina.api.story.model.Story;
import com.christina.app.story.R;
import com.christina.app.story.adpter.storiesList.StoriesListAdapter;
import com.christina.app.story.core.StoryEventArgs;
import com.christina.app.story.presentation.StoriesListPresenter;
import com.christina.app.story.view.StoriesListPresentableView;
import com.christina.app.story.view.fragment.BaseStoryFragment;
import com.christina.common.ConstantBuilder;
import com.christina.common.data.cursor.dataCursor.DataCursor;
import com.christina.common.event.BaseEvent;
import com.christina.common.event.Event;
import com.christina.common.view.ItemSpacingDecorator;

import javax.inject.Inject;

import butterknife.BindView;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;

@Accessors(prefix = "_")
public final class StoriesListFragment extends BaseStoryFragment
    implements StoriesListPresentableView {
    private static final String _KEY_SAVED_STATE =
        ConstantBuilder.savedStateKey(StoriesListFragment.class, "saved_state");

    @NonNull
    @Override
    public Event<StoryEventArgs> getOnDeleteStoryEvent() {
        return _onDeleteStoryEvent;
    }

    @NonNull
    @Override
    public Event<StoryEventArgs> getOnEditStoryEvent() {
        return getStoriesListAdapter().getOnEditStoryEvent();
    }

    @Override
    public void setLoadingVisible(final boolean visible) {
        if (_loadingVisible != visible) {
            _loadingVisible = visible;

            onLoadingVisibilityChanged();
        }
    }

    @Override
    public void setStoriesVisible(final boolean visible) {
        if (_storiesVisible != visible) {
            _storiesVisible = visible;

            onStoriesVisibilityChanged();
        }
    }

    @Override
    public void displayStories(
        @Nullable final DataCursor<Story> stories) {
        getStoriesListAdapter().setDataCursor(stories);

        if (_savedState != null && !_savedState.isScrollPositionRestored()) {
            final int scrollPosition = _savedState.getScrollPosition();
            getStoriesLayoutManager().scrollToPositionWithOffset(scrollPosition, 0);
            _savedState.setScrollPositionRestored(true);
        }
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            _savedState = savedInstanceState.getParcelable(_KEY_SAVED_STATE);

            if (_savedState != null) {
                _savedState.setScrollPositionRestored(false);
            }
        }
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

        _invalidateLoadingProgressView();
        _invalidateStoriesView();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        unbindViews();
    }

    @Override
    protected void onBindPresenter() {
        super.onBindPresenter();

        final val presenter = getStoriesListPresenter();
        if (presenter != null) {
            presenter.setPresentableView(this);
        }
    }

    @Override
    protected void onUnbindPresenter() {
        super.onUnbindPresenter();

        final val presenter = getStoriesListPresenter();
        if (presenter != null) {
            presenter.setPresentableView(null);
        }
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);

        if (outState != null) {
            if (_savedState == null) {
                _savedState = new StoriesListSavedState();
            }

            _savedState.setScrollPosition(getStoriesLayoutManager().findFirstVisibleItemPosition());
            outState.putParcelable(_KEY_SAVED_STATE, _savedState);
        }
    }

    @Override
    protected void onInject() {
        super.onInject();

        getStoryViewFragmentComponent().inject(this);
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
            _storiesView.setAdapter(getStoriesListAdapter());
        }
    }

    protected void onLoadingVisibilityChanged() {
        _invalidateLoadingProgressView();
    }

    protected void onStoriesVisibilityChanged() {
        _invalidateStoriesView();
    }

    protected void onSwipeStory(final long storyId) {
        _onDeleteStoryEvent.rise(new StoryEventArgs(storyId));
    }

    @Inject
    @Getter(AccessLevel.PROTECTED)
    @Nullable
    /*package-private*/ StoriesListPresenter _storiesListPresenter;

    @BindView(R.id.stories_loading_progress)
    @Nullable
    /*package-private*/ ContentLoadingProgressBar _storiesLoadingProgressView;

    @BindView(R.id.stories_list)
    @Nullable
    /*package-private*/ RecyclerView _storiesView;

    private final BaseEvent<StoryEventArgs> _onDeleteStoryEvent = new BaseEvent<>();

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
                onSwipeStory(StoriesListAdapter.getStoryId(viewHolder));
            }
        };

    @Getter(value = AccessLevel.PROTECTED, lazy = true)
    @NonNull
    private final GridLayoutManager _storiesLayoutManager = _createStoriesLayoutManager();

    @Getter(value = AccessLevel.PROTECTED, lazy = true)
    @NonNull
    private final StoriesListAdapter _storiesListAdapter = new StoriesListAdapter();

    @Getter(onMethod = @__(@Override))
    private boolean _loadingVisible;

    @Nullable
    private StoriesListSavedState _savedState;

    @Getter(onMethod = @__(@Override))
    private boolean _storiesVisible;

    @NonNull
    private GridLayoutManager _createStoriesLayoutManager() {
        final int columnCount = getResources().getInteger(R.integer.stories_viewer_column_count);
        return new GridLayoutManager(getContext(), columnCount);
    }

    private void _invalidateLoadingProgressView() {
        if (_storiesLoadingProgressView != null) {
            if (isLoadingVisible()) {
                _storiesLoadingProgressView.show();
            } else {
                _storiesLoadingProgressView.hide();
            }
        }
    }

    private void _invalidateStoriesView() {
        if (_storiesView != null) {
            if (isStoriesVisible()) {
                _storiesView.setVisibility(View.VISIBLE);
            } else {
                _storiesView.setVisibility(View.INVISIBLE);
            }
        }
    }
}
