package com.christina.app.story.view.fragment.storyFramesEditor;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.val;

import butterknife.BindView;

import com.christina.app.story.R;
import com.christina.app.story.core.StoryEventArgs;
import com.christina.app.story.core.adpter.storyFrames.StoryFramesAdapter;
import com.christina.app.story.core.delegate.LoadingViewDelegate;
import com.christina.app.story.data.model.ui.UIStory;
import com.christina.app.story.data.model.ui.UIStoryFrame;
import com.christina.app.story.di.qualifier.PresenterNames;
import com.christina.app.story.view.StoryFramesEditorScreen;
import com.christina.app.story.view.fragment.BaseStoryEditorFragment;
import com.christina.common.event.Events;
import com.christina.common.event.generic.Event;
import com.christina.common.event.generic.ManagedEvent;
import com.christina.common.event.notice.ManagedNoticeEvent;
import com.christina.common.event.notice.NoticeEvent;
import com.christina.common.presentation.Presenter;
import com.christina.common.view.ContentLoaderProgressBar;
import com.christina.common.view.ItemSpacingDecorator;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

@Accessors(prefix = "_")
public final class StoryFramesEditorFragment extends BaseStoryEditorFragment
    implements StoryFramesEditorScreen {

    @NonNull
    @Override
    public final NoticeEvent getContentChangedEvent() {
        return _contentChangedEvent;
    }

    @CallSuper
    @Override
    public boolean hasContent() {
        final val items = getStoryFramesAdapter().getItems();
        return items != null && !items.isEmpty();
    }

    @CallSuper
    @Override
    public void notifyStartEditing() {
        final Long storyId = getStoryId();
        if (storyId != null) {
            _startEditStoryEvent.rise(new StoryEventArgs(storyId));
        }
    }

    @CallSuper
    @Override
    public void notifyStopEditing() {
        onSaveStoryChanges();
    }

    @CallSuper
    @Override
    public void displayStory(@Nullable final UIStory story) {
        getLoadingViewDelegate().showContent();

        setStory(story);

        notifyEditedStoryChanged();
    }

    @Override
    public final void displayStoryLoading() {
        getLoadingViewDelegate().showLoading();
    }

    @NonNull
    public final Event<StoryEventArgs> getStartEditStoryEvent() {
        return _startEditStoryEvent;
    }

    @Nullable
    @CallSuper
    @Override
    public View onCreateView(
        final LayoutInflater inflater,
        @Nullable final ViewGroup container,
        @Nullable final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        final val view = inflater.inflate(R.layout.fragment_story_frames_editor, container, false);

        bindViews(view);

        onInitializeStoryFramesView();

        final val loadingViewDelegate = getLoadingViewDelegate();
        loadingViewDelegate.setLoadingView(_storyFramesLoadingView);
        loadingViewDelegate.setContentView(_storyFramesView);
        loadingViewDelegate.hideAll();

        return view;
    }

    @CallSuper
    @Override
    protected void onInjectMembers() {
        super.onInjectMembers();

        getStorySubscreenComponent().inject(this);

        final val presenter = getPresenter();
        if (presenter != null) {
            presenter.bindScreen(this);
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
    public void onPause() {
        super.onPause();

        onSaveStoryChanges();
    }

    @CallSuper
    @Override
    public void onDestroy() {
        super.onDestroy();

        final val storyFramesAdapter = getStoryFramesAdapter();
        storyFramesAdapter.setItems(null);
        storyFramesAdapter.notifyDataSetChanged();
    }

    protected final void notifyEditedStoryChanged() {
        onEditedStoryChanged();

        _contentChangedEvent.rise();
    }

    @CallSuper
    protected void onEditedStoryChanged() {
        final val story = getStory();

        final val storyFramesAdapter = getStoryFramesAdapter();

        storyFramesAdapter.setItems(null);

        if (story != null) {
            storyFramesAdapter.setStoryText(story.getText());
            storyFramesAdapter.setItems((List<UIStoryFrame>) story.getStoryFrames());
        } else {
            storyFramesAdapter.setStoryText(null);
            storyFramesAdapter.setItems(null);
        }

        storyFramesAdapter.notifyDataSetChanged();
    }

    @CallSuper
    protected void onInitializeStoryFramesView() {
        if (_storyFramesView != null) {
            final int spacing = getResources().getDimensionPixelOffset(R.dimen.grid_large_spacing);

            final val spacingDecorator = ItemSpacingDecorator
                .builder()
                .setSpacing(spacing)
                .collapseBorders()
                .enableEdges()
                .build();
            _storyFramesView.addItemDecoration(spacingDecorator);

            _storyFramesView.setLayoutManager(getStoryFramesLayoutManager());
            _storyFramesView.setAdapter(getStoryFramesAdapter());
        }
    }

    @CallSuper
    protected void onSaveStoryChanges() {
        final val editedStory = getStory();
        if (editedStory != null) {
            // FIXME: 12/24/2016
            // _onStoryChangedEvent.rise(new StoryContentEventArgs(editedStory));
        }
    }

    @CallSuper
    protected void onStoryIdChanged() {
        setStory(null);
        notifyEditedStoryChanged();

        final Long storyId = getStoryId();
        if (storyId != null) {
            _startEditStoryEvent.rise(new StoryEventArgs(storyId));
        }
    }

    @Named(PresenterNames.STORY_FRAMES_EDITOR)
    @Inject
    @Getter(AccessLevel.PROTECTED)
    @Nullable
    /*package-private*/ Presenter<StoryFramesEditorScreen> _presenter;

    @BindView(R.id.story_frames_loading)
    @Nullable
    /*package-private*/ ContentLoaderProgressBar _storyFramesLoadingView;

    @BindView(R.id.story_frames)
    @Nullable
    /*package-private*/ RecyclerView _storyFramesView;

    @NonNull
    private final ManagedNoticeEvent _contentChangedEvent = Events.createNoticeEvent();

    @Getter(value = AccessLevel.PROTECTED)
    @NonNull
    private final LoadingViewDelegate _loadingViewDelegate = new LoadingViewDelegate();

    @NonNull
    private final ManagedEvent<StoryEventArgs> _startEditStoryEvent = Events.createEvent();

    @Getter(value = AccessLevel.PROTECTED)
    @NonNull
    private final StoryFramesAdapter _storyFramesAdapter = new StoryFramesAdapter();

    @Getter(value = AccessLevel.PROTECTED, lazy = true)
    @Nullable
    private final LinearLayoutManager _storyFramesLayoutManager =
        new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    @Nullable
    private UIStory _story;
}
