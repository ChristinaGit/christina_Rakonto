package moe.christina.app.rakonto.screen.fragment.storiesList;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;

import butterknife.BindView;

import moe.christina.app.rakonto.R;
import moe.christina.app.rakonto.core.adpter.storiesList.StoriesListAdapter;
import moe.christina.app.rakonto.core.eventArgs.StoryEventArgs;
import moe.christina.app.rakonto.di.qualifier.PresenterNames;
import moe.christina.app.rakonto.model.ui.UIStory;
import moe.christina.app.rakonto.screen.StoriesListScreen;
import moe.christina.app.rakonto.screen.fragment.BaseStoryFragment;
import moe.christina.common.ConstantBuilder;
import moe.christina.common.event.Events;
import moe.christina.common.event.generic.Event;
import moe.christina.common.event.generic.ManagedEvent;
import moe.christina.common.event.notice.ManagedNoticeEvent;
import moe.christina.common.event.notice.NoticeEvent;
import moe.christina.common.extension.ItemSpacingDecorator;
import moe.christina.common.extension.delegate.loading.LoadingViewDelegate;
import moe.christina.common.extension.delegate.loading.ProgressVisibilityHandler;
import moe.christina.common.extension.delegate.loading.VisibilityHandler;
import moe.christina.common.extension.view.ContentLoaderProgressBar;
import moe.christina.common.mvp.presenter.Presenter;
import moe.christina.common.utility.AnimationViewUtils;

import org.parceler.Parcels;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

@Accessors(prefix = "_")
public final class StoriesListFragment extends BaseStoryFragment implements StoriesListScreen {
    private static final String _KEY_SAVED_STATE =
        ConstantBuilder.savedStateKey(StoriesListFragment.class, "saved_state");

    @CallSuper
    @Override
    public void displayStories(@Nullable final List<UIStory> stories) {
        final val loadingViewDelegate = getLoadingViewDelegate();
        if (loadingViewDelegate != null) {
            loadingViewDelegate.showContent(stories != null && !stories.isEmpty());
        }

        final val storiesListAdapter = getStoriesListAdapter();
        storiesListAdapter.setItems(stories);
        storiesListAdapter.notifyDataSetChanged();

        if (_state != null && !_state.isScrollPositionRestored()) {
            final int scrollPosition = _state.getScrollPosition();
            getStoriesLayoutManager().scrollToPositionWithOffset(scrollPosition, 0);
            _state.setScrollPositionRestored(true);
        }
    }

    @Override
    public final void displayStoriesLoading() {
        final val loadingViewDelegate = getLoadingViewDelegate();
        if (loadingViewDelegate != null) {
            loadingViewDelegate.showLoading();
        }
    }

    @NonNull
    @Override
    public final Event<StoryEventArgs> getDeleteStoryEvent() {
        return _deleteStoryEvent;
    }

    @NonNull
    @Override
    public final Event<StoryEventArgs> getEditStoryEvent() {
        return getStoriesListAdapter().getEditStoryEvent();
    }

    @NonNull
    @Override
    public final NoticeEvent getViewStoriesEvent() {
        return _viewStoriesEvent;
    }

    @CallSuper
    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            _state = Parcels.unwrap(savedInstanceState.getParcelable(_KEY_SAVED_STATE));

            if (_state != null) {
                _state.setScrollPositionRestored(false);
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

        _loadingViewDelegate = LoadingViewDelegate
            .builder()
            .setContentView(_storiesView)
            .setNoContentView(_noStoriesView)
            .setLoadingView(_storiesLoadingView)
            .setLoadingVisibilityHandler(getStoryListLoadingVisibilityHandler())
            .setContentVisibilityHandler(getStoryListVisibilityHandler())
            .build();

        return view;
    }

    @CallSuper
    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);

        if (outState != null) {
            if (_state == null) {
                _state = new StoriesListState();
            }

            final int scrollPosition =
                getStoriesLayoutManager().findFirstCompletelyVisibleItemPositions(null)[0];
            _state.setScrollPosition(scrollPosition);
            outState.putParcelable(_KEY_SAVED_STATE, Parcels.wrap(_state));
        }
    }

    @Override
    protected void onReleaseInjectedMembers() {
        super.onReleaseInjectedMembers();

        final val presenter = getPresenter();
        if (presenter != null) {
            presenter.unbindScreen();
        }

        unbindViews();
    }

    @Override
    public void onResume() {
        super.onResume();

        _viewStoriesEvent.rise();
    }

    @CallSuper
    @Override
    public void onDestroy() {
        super.onDestroy();

        final val storiesListAdapter = getStoriesListAdapter();
        storiesListAdapter.setItems(null);
        storiesListAdapter.notifyDataSetChanged();
    }

    @CallSuper
    protected void onInitializeStoriesView() {
        if (_storiesView != null) {
            final val context = _storiesView.getContext();
            final val resources = context.getResources();

            final int spacing = resources.getDimensionPixelOffset(R.dimen.grid_large_spacing);

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

    @CallSuper
    @Override
    protected void onInjectMembers() {
        super.onInjectMembers();

        getRakontoSubscreenComponent().inject(this);

        final val presenter = getPresenter();
        if (presenter != null) {
            presenter.bindScreen(this);
        }
    }

    @CallSuper
    protected void onSwipeStory(final long storyId) {
        _deleteStoryEvent.rise(new StoryEventArgs(storyId));
    }

    @BindView(R.id.stories_no_content)
    @Nullable
    /*package-private*/ View _noStoriesView;

    @Named(PresenterNames.STORIES_LIST)
    @Inject
    @Getter(AccessLevel.PROTECTED)
    @Nullable
    /*package-private*/ Presenter<StoriesListScreen> _presenter;

    @BindView(R.id.stories_loading)
    @Nullable
    /*package-private*/ ContentLoaderProgressBar _storiesLoadingView;

    @BindView(R.id.stories_list)
    @Nullable
    /*package-private*/ RecyclerView _storiesView;

    @NonNull
    private final ManagedEvent<StoryEventArgs> _deleteStoryEvent = Events.createEvent();

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
    private final StaggeredGridLayoutManager _storiesLayoutManager = createStoriesLayoutManager();

    @Getter(value = AccessLevel.PROTECTED)
    @NonNull
    private final StoriesListAdapter _storiesListAdapter = new StoriesListAdapter();

    @Getter(AccessLevel.PROTECTED)
    @NonNull
    private final ProgressVisibilityHandler _storyListLoadingVisibilityHandler =
        new ProgressVisibilityHandler();

    @Getter(AccessLevel.PROTECTED)
    @NonNull
    private final VisibilityHandler _storyListVisibilityHandler = new VisibilityHandler() {
        @Override
        public void changeVisibility(@NonNull final View view, final boolean visible) {
            if (visible) {
                AnimationViewUtils.animateSetVisibility(view, View.VISIBLE, R.anim.fade_in_long);
            } else {
                view.setVisibility(View.GONE);
            }
        }
    };

    @NonNull
    private final ManagedNoticeEvent _viewStoriesEvent = Events.createNoticeEvent();

    @Getter(value = AccessLevel.PROTECTED)
    @Nullable
    private LoadingViewDelegate _loadingViewDelegate;

    @Nullable
    private StoriesListState _state;

    @NonNull
    private StaggeredGridLayoutManager createStoriesLayoutManager() {
        final int columnCount = getResources().getInteger(R.integer.stories_viewer_column_count);
        return new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
    }
}
