package com.christina.app.story.view.fragment.storyFramesEditor;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.christina.api.story.model.Story;
import com.christina.api.story.model.StoryFrame;
import com.christina.app.story.R;
import com.christina.app.story.core.StoryEventArgs;
import com.christina.app.story.core.adpter.storyEditorPages.StoryEditorPage;
import com.christina.app.story.core.adpter.storyFrames.StoryFramesAdapter;
import com.christina.app.story.core.delegate.LoadingViewDelegate;
import com.christina.app.story.di.qualifier.PresenterNames;
import com.christina.app.story.view.StoryFramesEditorPresentableView;
import com.christina.app.story.view.fragment.BaseStoryFragment;
import com.christina.common.data.cursor.dataCursor.DataCursor;
import com.christina.common.event.BaseEvent;
import com.christina.common.event.BaseNoticeEvent;
import com.christina.common.event.Event;
import com.christina.common.event.NoticeEvent;
import com.christina.common.view.ItemSpacingDecorator;
import com.christina.common.view.presentation.Presenter;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.val;

@Accessors(prefix = "_")
public final class StoryFramesEditorFragment extends BaseStoryFragment
    implements StoryFramesEditorPresentableView, StoryEditorPage {
    @Override
    public final void setEditedStoryId(final long editedStoryId) {
        if (_editedStoryId != editedStoryId) {
            _editedStoryId = editedStoryId;

            onEditedStoryIdChanged();
        }
    }

    @NonNull
    @Override
    public final NoticeEvent getOnContentChangedEvent() {
        return _onContentChangedEvent;
    }

    @CallSuper
    @Override
    public boolean hasContent() {
        final val dataCursor = getStoryFramesAdapter().getDataCursor();
        return dataCursor != null && dataCursor.getCount() > 0;
    }

    @CallSuper
    @Override
    public void onStartEditing() {
        final long editedStoryId = getEditedStoryId();
        if (editedStoryId != Story.NO_ID) {
            _onStartEditStoryEvent.rise(new StoryEventArgs(editedStoryId));
        }
    }

    @CallSuper
    @Override
    public void onStopEditing() {
        onSaveStoryChanges();
    }

    @CallSuper
    @Override
    public void displayStory(@Nullable final Story story) {
        setEditedStory(story);
        notifyEditedStoryChanged();
    }

    @CallSuper
    @Override
    public void displayStoryFrames(@Nullable final DataCursor<StoryFrame> storyFrames) {
        getStoryFramesAdapter().setDataCursor(storyFrames);

        _onContentChangedEvent.rise();
    }

    @NonNull
    @Override
    public final Event<StoryEventArgs> getOnStartEditStoryEvent() {
        return _onStartEditStoryEvent;
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
    public final boolean isStoryFramesVisible() {
        return getLoadingViewDelegate().isContentVisible();
    }

    @Override
    public final void setStoryFramesVisible(final boolean visible) {
        getLoadingViewDelegate().setContentVisible(visible);
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
        loadingViewDelegate.invalidateViews();

        return view;
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

        getStoryFramesAdapter().setDataCursor(null);

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

    @CallSuper
    @Override
    protected void onUnbindPresenter() {
        super.onUnbindPresenter();

        final val presenter = getPresenter();
        if (presenter != null) {
            presenter.setPresentableView(null);
        }
    }

    protected final void notifyEditedStoryChanged() {
        onEditedStoryChanged();

        _onContentChangedEvent.rise();
    }

    @CallSuper
    protected void onEditedStoryChanged() {
        final val editedStory = getEditedStory();

        getStoryFramesAdapter().setDataCursor(null);

        final String storyText;
        if (editedStory != null) {
            storyText = editedStory.getText();
        } else {
            storyText = null;
        }
        getStoryFramesAdapter().setStoryText(storyText);
    }

    @CallSuper
    protected void onEditedStoryIdChanged() {
        setEditedStory(null);
        notifyEditedStoryChanged();

        final long editedStoryId = getEditedStoryId();
        if (editedStoryId != Story.NO_ID) {
            _onStartEditStoryEvent.rise(new StoryEventArgs(editedStoryId));
        }
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
    @Override
    protected void onInject() {
        super.onInject();

        getStoryViewFragmentComponent().inject(this);
    }

    @CallSuper
    protected void onSaveStoryChanges() {
        final val editedStory = getEditedStory();
        if (editedStory != null) {
            // FIXME: 12/24/2016
            // _onStoryChangedEvent.rise(new StoryContentEventArgs(editedStory));
        }
    }

    @Named(PresenterNames.STORY_FRAMES_EDITOR)
    @Inject
    @Getter(AccessLevel.PROTECTED)
    @Nullable
    /*package-private*/ Presenter<StoryFramesEditorPresentableView> _presenter;

    @BindView(R.id.story_frames_loading)
    @Nullable
    /*package-private*/ ContentLoadingProgressBar _storyFramesLoadingView;

    @BindView(R.id.story_frames)
    @Nullable
    /*package-private*/ RecyclerView _storyFramesView;

    @Getter(value = AccessLevel.PROTECTED, lazy = true)
    @NonNull
    private final LoadingViewDelegate _loadingViewDelegate = new LoadingViewDelegate();

    @NonNull
    private final BaseNoticeEvent _onContentChangedEvent = new BaseNoticeEvent();

    @NonNull
    private final BaseEvent<StoryEventArgs> _onStartEditStoryEvent = new BaseEvent<>();

    @Getter(value = AccessLevel.PROTECTED, lazy = true)
    @NonNull
    private final StoryFramesAdapter _storyFramesAdapter = new StoryFramesAdapter();

    @Getter(value = AccessLevel.PROTECTED, lazy = true)
    @Nullable
    private final LinearLayoutManager _storyFramesLayoutManager =
        new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    @Nullable
    private Story _editedStory;

    @Getter(onMethod = @__(@Override))
    private long _editedStoryId;
}
