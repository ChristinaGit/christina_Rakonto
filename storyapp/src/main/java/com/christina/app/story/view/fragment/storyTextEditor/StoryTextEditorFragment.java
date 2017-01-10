package com.christina.app.story.view.fragment.storyTextEditor;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.christina.api.story.model.Story;
import com.christina.app.story.R;
import com.christina.app.story.core.StoryContentEventArgs;
import com.christina.app.story.core.StoryEventArgs;
import com.christina.app.story.core.adpter.storyEditorPages.StoryEditorPage;
import com.christina.app.story.core.delegate.LoadingViewDelegate;
import com.christina.app.story.di.qualifier.PresenterNames;
import com.christina.app.story.view.StoryTextEditorPresentableView;
import com.christina.app.story.view.fragment.BaseStoryFragment;
import com.christina.common.event.BaseEvent;
import com.christina.common.event.BaseNoticeEvent;
import com.christina.common.event.Event;
import com.christina.common.event.NoticeEvent;
import com.christina.common.utility.ImeUtils;
import com.christina.common.view.presentation.Presenter;

import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.OnTextChanged;
import butterknife.OnTextChanged.Callback;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.val;

@Accessors(prefix = "_")
public final class StoryTextEditorFragment extends BaseStoryFragment
    implements StoryTextEditorPresentableView, StoryEditorPage {
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
        final boolean hasContent;

        final val story = getEditedStory();
        if (story != null) {
            hasContent = !TextUtils.isEmpty(story.getText());
        } else {
            hasContent = false;
        }

        return hasContent;
    }

    @CallSuper
    @Override
    public void onStartEditing() {
        final long editedStoryId = getEditedStoryId();
        if (editedStoryId != Story.NO_ID) {
            _onStartEditStoryEvent.rise(new StoryEventArgs(editedStoryId));
        }

        if (_storyTextView != null && !_storyTextView.hasFocus()) {
            _storyTextView.requestFocus();
            ImeUtils.showIme(_storyTextView);
        }
    }

    @CallSuper
    @Override
    public void onStopEditing() {
        if (_storyTextView != null && _storyTextView.hasFocus()) {
            ImeUtils.hideIme(_storyTextView);
        }

        onSaveStoryChanges();
    }

    @CallSuper
    @Override
    public void displayStory(@Nullable final Story story) {
        setEditedStory(story);
        notifyEditedStoryChanged();
    }

    @NonNull
    public final Event<StoryEventArgs> getOnStartEditStoryEvent() {
        return _onStartEditStoryEvent;
    }

    @NonNull
    @Override
    public final Event<StoryContentEventArgs> getOnStoryChangedEvent() {
        return _onStoryChangedEvent;
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
    public final boolean isStoryVisible() {
        return getLoadingViewDelegate().isContentVisible();
    }

    @Override
    public final void setStoryVisible(final boolean visible) {
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

        final val view = inflater.inflate(R.layout.fragment_story_text_editor, container, false);

        bindViews(view);

        final val loadingViewDelegate = getLoadingViewDelegate();
        loadingViewDelegate.setLoadingView(_storyLoadingView);
        loadingViewDelegate.setContentView(_storyView);
        loadingViewDelegate.invalidateViews();

        return view;
    }

    @CallSuper
    @Override
    public void onPause() {
        super.onPause();

        onSaveStoryChanges();
    }

    @CallSuper
    @Override
    public void onDestroy() {
        super.onDestroy();

        unbindViews();
    }

    @CallSuper
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

        if (_storyTextView != null) {
            if (editedStory != null) {
                final val storyText = editedStory.getText();

                _originalStoryText = storyText;

                _storyTextView.setEnabled(true);
                _storyTextView.setText(storyText);
            } else {
                _storyTextView.setText(null);
                _storyTextView.setEnabled(true);
            }
        }
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
    @Override
    protected void onInject() {
        super.onInject();

        getStoryViewFragmentComponent().inject(this);
    }

    @CallSuper
    protected void onSaveStoryChanges() {
        final val editedStory = getEditedStory();
        if (editedStory != null && !Objects.equals(editedStory.getText(), _originalStoryText)) {
            _onStoryChangedEvent.rise(new StoryContentEventArgs(editedStory));
        }
    }

    @OnTextChanged(value = R.id.story_text, callback = Callback.AFTER_TEXT_CHANGED)
    protected void onStoryTextChanged(final Editable s) {
        final val storyText = s != null ? s.toString().trim() : null;

        final val editedStory = getEditedStory();
        if (editedStory != null) {
            editedStory.setText(storyText);

            _onContentChangedEvent.rise();
        }
    }

    @Named(PresenterNames.STORY_TEXT_EDITOR)
    @Inject
    @Getter(AccessLevel.PROTECTED)
    @Nullable
    /*package-private*/ Presenter<StoryTextEditorPresentableView> _presenter;

    @BindView(R.id.story_loading)
    @Nullable
    /*package-private*/ ContentLoadingProgressBar _storyLoadingView;

    @BindView(R.id.story_text)
    @Nullable
    /*package-private*/ EditText _storyTextView;

    @BindView(R.id.story)
    @Nullable
    /*package-private*/ View _storyView;

    @Getter(value = AccessLevel.PROTECTED, lazy = true)
    @NonNull
    private final LoadingViewDelegate _loadingViewDelegate = new LoadingViewDelegate();

    @NonNull
    private final BaseNoticeEvent _onContentChangedEvent = new BaseNoticeEvent();

    @NonNull
    private final BaseEvent<StoryEventArgs> _onStartEditStoryEvent = new BaseEvent<>();

    @NonNull
    private final BaseEvent<StoryContentEventArgs> _onStoryChangedEvent = new BaseEvent<>();

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    @Nullable
    private Story _editedStory;

    @Getter(onMethod = @__(@Override))
    private long _editedStoryId;

    @Nullable
    private String _originalStoryText;
}
