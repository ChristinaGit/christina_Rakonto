package com.christina.app.story.view.fragment.storiesList;

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
import com.christina.app.story.delegate.LoadingViewDelegate;
import com.christina.app.story.di.qualifier.PresenterNames;
import com.christina.app.story.view.StoriesListPresentableView;
import com.christina.app.story.view.fragment.BaseStoryFragment;
import com.christina.common.ConstantBuilder;
import com.christina.common.data.cursor.dataCursor.DataCursor;
import com.christina.common.event.BaseEvent;
import com.christina.common.event.Event;
import com.christina.common.view.ItemSpacingDecorator;
import com.christina.common.view.presentation.Presenter;

import javax.inject.Inject;
import javax.inject.Named;

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

    @Override
    public void displayStories(@Nullable final DataCursor<Story> stories) {
        getStoriesListAdapter().setDataCursor(stories);

        if (_savedState != null && !_savedState.isScrollPositionRestored()) {
            final int scrollPosition = _savedState.getScrollPosition();
            getStoriesLayoutManager().scrollToPositionWithOffset(scrollPosition, 0);
            _savedState.setScrollPositionRestored(true);
        }
    }

    @NonNull
    @Override
    public final Event<StoryEventArgs> getOnDeleteStoryEvent() {
        return _onDeleteStoryEvent;
    }

    @NonNull
    @Override
    public final Event<StoryEventArgs> getOnEditStoryEvent() {
        return getStoriesListAdapter().getOnEditStoryEvent();
    }

    @Override
    public final boolean isLoadingVisible() {
        return getLoadingViewDelegate().isLoadingVisible();
    }

    @Override
    public final void setLoadingVisible(final boolean visible) {
        getLoadingViewDelegate().setLoadingVisible(visible);
    }

    @Override
    public final boolean isStoriesVisible() {
        return getLoadingViewDelegate().isContentVisible();
    }

    @Override
    public final void setStoriesVisible(final boolean visible) {
        getLoadingViewDelegate().setContentVisible(visible);
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
    @CallSuper
    @Override
    public View onCreateView(
        final LayoutInflater inflater,
        @Nullable final ViewGroup container,
        @Nullable final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        final val view = inflater.inflate(R.layout.fragment_stories_list, container, false);

        bindViews(view);

        onInitializeStoriesView();

        final val loadingViewDelegate = getLoadingViewDelegate();
        loadingViewDelegate.setContentView(_storiesView);
        loadingViewDelegate.setLoadingView(_storiesLoadingView);
        loadingViewDelegate.invalidateViews();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        getStoriesListAdapter().setDataCursor(null);

        unbindViews();
    }

    @Override
    protected void onBindPresenter() {
        super.onBindPresenter();

        final val presenter = getPresenter();
        if (presenter != null) {
            presenter.setPresentableView(this);
        }
    }

    @Override
    protected void onUnbindPresenter() {
        super.onUnbindPresenter();

        final val presenter = getPresenter();
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

    protected void onInitializeStoriesView() {
        if (_storiesView != null) {
            final val context = _storiesView.getContext();
            final val resources = context.getResources();

            final int spacing = resources.getDimensionPixelOffset(R.dimen.card_large_grid_spacing);

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

    protected void onSwipeStory(final long storyId) {
        _onDeleteStoryEvent.rise(new StoryEventArgs(storyId));
    }

    @Named(PresenterNames.STORIES_LIST)
    @Inject
    @Getter(AccessLevel.PROTECTED)
    @Nullable
    /*package-private*/ Presenter<StoriesListPresentableView> _presenter;

    @BindView(R.id.stories_loading)
    @Nullable
    /*package-private*/ ContentLoadingProgressBar _storiesLoadingView;

    @BindView(R.id.stories_list)
    @Nullable
    /*package-private*/ RecyclerView _storiesView;

    @Getter(value = AccessLevel.PROTECTED, lazy = true)
    @NonNull
    private final LoadingViewDelegate _loadingViewDelegate = new LoadingViewDelegate();

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

    @Nullable
    private StoriesListSavedState _savedState;

    @NonNull
    private GridLayoutManager _createStoriesLayoutManager() {
        final int columnCount = getResources().getInteger(R.integer.stories_viewer_column_count);
        return new GridLayoutManager(getContext(), columnCount);
    }
}
