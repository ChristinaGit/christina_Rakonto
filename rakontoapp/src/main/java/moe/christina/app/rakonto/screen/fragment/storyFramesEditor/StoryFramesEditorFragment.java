package moe.christina.app.rakonto.screen.fragment.storyFramesEditor;

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

import moe.christina.app.rakonto.R;
import moe.christina.app.rakonto.core.adpter.storyFrames.StoryFramesAdapter;
import moe.christina.app.rakonto.core.eventArgs.StoryEventArgs;
import moe.christina.app.rakonto.core.manager.search.StoryFrameImage;
import moe.christina.app.rakonto.di.qualifier.PresenterNames;
import moe.christina.app.rakonto.model.ui.UIStory;
import moe.christina.app.rakonto.model.ui.UIStoryFrame;
import moe.christina.app.rakonto.screen.StoryFramesEditorScreen;
import moe.christina.app.rakonto.screen.fragment.BaseStoryEditorFragment;
import moe.christina.common.event.Events;
import moe.christina.common.event.generic.Event;
import moe.christina.common.event.generic.ManagedEvent;
import moe.christina.common.event.notice.ManagedNoticeEvent;
import moe.christina.common.event.notice.NoticeEvent;
import moe.christina.common.extension.ItemSpacingDecorator;
import moe.christina.common.mvp.presenter.Presenter;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

@Accessors(prefix = "_")
public final class StoryFramesEditorFragment extends BaseStoryEditorFragment
    implements StoryFramesEditorScreen {

    @Override
    public final void displayStoreFrameImageCandidates(
        final long storyFrameId, @Nullable final List<StoryFrameImage> candidatesUris) {
        // FIXME: 1/25/2017
    }

    @CallSuper
    @Override
    public void displayStory(@Nullable final UIStory story) {
        setStory(story);

        notifyEditedStoryChanged();

        onCompleteStartEditingPreparation();
    }

    @Override
    @NonNull
    public final Event<StoryEventArgs> getStartEditStoryEvent() {
        return _startEditStoryEvent;
    }

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
    public void notifyStartEditing(@Nullable final ReadyCallback callback) {
        super.notifyStartEditing(callback);

        final Long storyId = getStoryId();
        if (storyId != null) {
            _startEditStoryEvent.rise(new StoryEventArgs(storyId));
        }
    }

    @CallSuper
    @Override
    public void notifyStopEditing(@Nullable final ReadyCallback callback) {
        super.notifyStopEditing(callback);

        onSaveStoryChanges();
    }

    @Override
    @CallSuper
    protected void onStoryIdChanged() {
        setStory(null);
        notifyEditedStoryChanged();

        final Long storyId = getStoryId();
        if (storyId != null) {
            _startEditStoryEvent.rise(new StoryEventArgs(storyId));
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

        final val view = inflater.inflate(R.layout.fragment_story_frames_editor, container, false);

        bindViews(view);

        onInitializeStoryFramesView();

        return view;
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
    protected void onSaveStoryChanges() {
        final val editedStory = getStory();
        if (editedStory != null) {
            // FIXME: 12/24/2016
            // _onStoryChangedEvent.rise(new StoryContentEventArgs(editedStory));
        }

        onCompleteStopEditingPreparation();
    }

    @Named(PresenterNames.STORY_FRAMES_EDITOR)
    @Inject
    @Getter(AccessLevel.PROTECTED)
    @Nullable
    /*package-private*/ Presenter<StoryFramesEditorScreen> _presenter;

    @BindView(R.id.story_frames)
    @Nullable
    /*package-private*/ RecyclerView _storyFramesView;

    @NonNull
    private final ManagedNoticeEvent _contentChangedEvent = Events.createNoticeEvent();

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
