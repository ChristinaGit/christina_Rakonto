package com.christina.app.story.view.fragment.storyTextEditor;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.val;

import butterknife.BindView;
import butterknife.OnTextChanged;
import butterknife.OnTextChanged.Callback;

import com.christina.app.story.R;
import com.christina.app.story.core.StoryContentEventArgs;
import com.christina.app.story.core.StoryEventArgs;
import com.christina.app.story.core.delegate.LoadingViewDelegate;
import com.christina.app.story.data.model.Story;
import com.christina.common.view.ContentLoaderProgressBar;
import com.christina.app.story.di.qualifier.PresenterNames;
import com.christina.app.story.view.StoryTextEditorScreen;
import com.christina.app.story.view.fragment.BaseStoryEditorFragment;
import com.christina.common.event.Events;
import com.christina.common.event.generic.Event;
import com.christina.common.event.generic.ManagedEvent;
import com.christina.common.event.notice.ManagedNoticeEvent;
import com.christina.common.event.notice.NoticeEvent;
import com.christina.common.presentation.Presenter;
import com.christina.common.utility.ImeUtils;

import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

@Accessors(prefix = "_")
public final class StoryTextEditorFragment extends BaseStoryEditorFragment
    implements StoryTextEditorScreen {
    @NonNull
    @Override
    public final NoticeEvent getContentChangedEvent() {
        return _contentChangedEvent;
    }

    @CallSuper
    @Override
    public boolean hasContent() {
        final boolean hasContent;

        final val story = getStory();
        if (story != null) {
            hasContent = !TextUtils.isEmpty(story.getText());
        } else {
            hasContent = false;
        }

        return hasContent;
    }

    @CallSuper
    @Override
    public void notifyStartEditing() {
        final Long storyId = getStoryId();
        if (storyId != null) {
            _startEditStoryEvent.rise(new StoryEventArgs(storyId));
        }

        if (_storyTextView != null && !_storyTextView.hasFocus()) {
            _storyTextView.requestFocus();
            ImeUtils.showIme(_storyTextView);
        }
    }

    @CallSuper
    @Override
    public void notifyStopEditing() {
        if (_storyTextView != null && _storyTextView.hasFocus()) {
            ImeUtils.hideIme(_storyTextView);
        }

        onSaveStoryChanges();
    }

    @CallSuper
    @Override
    public void displayStory(@Nullable final Story story) {
        setStory(story);
        notifyEditedStoryChanged();
    }

    @Override
    public void displayStoryLoading() {
        final val loadingViewDelegate = getLoadingViewDelegate();
        loadingViewDelegate.setContentVisible(false);
        loadingViewDelegate.setLoadingVisible(true);
    }

    @NonNull
    public final Event<StoryEventArgs> getStartEditStoryEvent() {
        return _startEditStoryEvent;
    }

    @NonNull
    public final Event<StoryContentEventArgs> getStoryChangedEvent() {
        return _storyChangedEvent;
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

    @CallSuper
    @Override
    public void onPause() {
        super.onPause();

        onSaveStoryChanges();
    }

    protected final void notifyEditedStoryChanged() {
        onEditedStoryChanged();

        _contentChangedEvent.rise();
    }

    @CallSuper
    protected void onEditedStoryChanged() {
        final val editedStory = getStory();

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
    protected void onSaveStoryChanges() {
        final val story = getStory();
        if (story != null && !Objects.equals(story.getText(), _originalStoryText)) {
            _storyChangedEvent.rise(new StoryContentEventArgs(story));
        }
    }

    @CallSuper
    @Override
    protected void onStoryIdChanged() {
        setStory(null);
        notifyEditedStoryChanged();

        final Long storyId = getStoryId();
        if (storyId != null) {
            _startEditStoryEvent.rise(new StoryEventArgs(storyId));
        }
    }

    @OnTextChanged(value = R.id.story_text, callback = Callback.AFTER_TEXT_CHANGED)
    protected void onStoryTextChanged(final Editable s) {
        final val storyText = s != null ? s.toString().trim() : null;

        final val story = getStory();
        if (story != null) {
            story.setText(storyText);

            _contentChangedEvent.rise();
        }
    }

    @Named(PresenterNames.STORY_TEXT_EDITOR)
    @Inject
    @Getter(AccessLevel.PROTECTED)
    @Nullable
    /*package-private*/ Presenter<StoryTextEditorScreen> _presenter;

    @BindView(R.id.story_loading)
    @Nullable
    /*package-private*/ ContentLoaderProgressBar _storyLoadingView;

    @BindView(R.id.story_text)
    @Nullable
    /*package-private*/ EditText _storyTextView;

    @BindView(R.id.story)
    @Nullable
    /*package-private*/ View _storyView;

    @NonNull
    private final ManagedNoticeEvent _contentChangedEvent = Events.createNoticeEvent();

    @Getter(value = AccessLevel.PROTECTED)
    @NonNull
    private final LoadingViewDelegate _loadingViewDelegate = new LoadingViewDelegate();

    @NonNull
    private final ManagedEvent<StoryEventArgs> _startEditStoryEvent = Events.createEvent();

    @NonNull
    private final ManagedEvent<StoryContentEventArgs> _storyChangedEvent = Events.createEvent();

    @Nullable
    private String _originalStoryText;

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    @Nullable
    private Story _story;
}
