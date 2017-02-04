package moe.christina.app.rakonto.screen.fragment.storyTextEditor;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import moe.christina.app.rakonto.R;
import moe.christina.app.rakonto.core.eventArgs.StoryChangedEventArgs;
import moe.christina.app.rakonto.core.eventArgs.StoryEventArgs;
import moe.christina.app.rakonto.di.qualifier.PresenterNames;
import moe.christina.app.rakonto.model.ui.UIStory;
import moe.christina.app.rakonto.screen.StoryTextEditorScreen;
import moe.christina.app.rakonto.screen.fragment.BaseStoryEditorFragment;
import moe.christina.common.event.Events;
import moe.christina.common.event.generic.Event;
import moe.christina.common.event.generic.ManagedEvent;
import moe.christina.common.event.notice.ManagedNoticeEvent;
import moe.christina.common.event.notice.NoticeEvent;
import moe.christina.common.mvp.presenter.Presenter;
import moe.christina.common.utility.ImeUtils;

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

        final String storyText;
        if (_storyTextView != null) {
            storyText = _storyTextView.getText().toString().trim();
        } else {
            storyText = null;
        }

        hasContent = storyText != null;

        return hasContent;
    }

    @CallSuper
    @Override
    public void displaySaveStoryChangedComplete() {
        onCompleteStopEditingPreparation();
    }

    @CallSuper
    @Override
    public void displayStory(@Nullable final UIStory story) {
        setStory(story);
        notifyStoryChanged();

        if (_storyTextView != null && !_storyTextView.hasFocus()) {
            _storyTextView.requestFocus();
            ImeUtils.showIme(_storyTextView);
        }

        onCompleteStartEditingPreparation();
    }

    @Override
    @NonNull
    public final Event<StoryEventArgs> getStartEditStoryEvent() {
        return _startEditStoryEvent;
    }

    @Override
    @NonNull
    public final Event<StoryChangedEventArgs> getStoryChangedEvent() {
        return _storyChangedEvent;
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

        if (_storyTextView != null && _storyTextView.hasFocus()) {
            ImeUtils.hideIme(_storyTextView);
        }

        onSaveStoryChanges();
    }

    @CallSuper
    @Override
    protected void onStoryIdChanged() {
        setStory(null);
        notifyStoryChanged();

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

        final val view = inflater.inflate(R.layout.fragment_story_text_editor, container, false);

        bindViews(view);

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

    @CallSuper
    @Override
    public void onPause() {
        super.onPause();

        onSaveStoryChanges();
    }

    protected final void notifyStoryChanged() {
        onStoryChanged();

        _contentChangedEvent.rise();
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
        final String newStoryText;
        if (_storyTextView != null) {
            newStoryText = _storyTextView.getText().toString().trim();
        } else {
            newStoryText = null;
        }

        final String originalStoryText;
        final val story = getStory();
        if (story != null) {
            originalStoryText = story.getText();
        } else {
            originalStoryText = null;
        }

        if (story != null && !Objects.equals(originalStoryText, newStoryText)) {
            final val eventArgs = new StoryChangedEventArgs(story.getId());
            eventArgs.setStoryText(newStoryText);

            _storyChangedEvent.rise(eventArgs);
        } else {
            onCompleteStopEditingPreparation();
        }
    }

    @CallSuper
    protected void onStoryChanged() {
        final val editedStory = getStory();

        if (_storyTextView != null) {
            if (editedStory != null) {
                final val storyText = editedStory.getText();

                _storyTextView.setEnabled(true);
                _storyTextView.setText(storyText);
            } else {
                _storyTextView.setText(null);
                _storyTextView.setEnabled(true);
            }
        }
    }

    @OnTextChanged(value = R.id.story_text, callback = Callback.AFTER_TEXT_CHANGED)
    protected void onStoryTextChanged() {
        _contentChangedEvent.rise();
    }

    @Named(PresenterNames.STORY_TEXT_EDITOR)
    @Inject
    @Getter(AccessLevel.PROTECTED)
    @Nullable
    /*package-private*/ Presenter<StoryTextEditorScreen> _presenter;

    @BindView(R.id.story_text)
    @Nullable
    /*package-private*/ EditText _storyTextView;

    @BindView(R.id.story)
    @Nullable
    /*package-private*/ View _storyView;

    @NonNull
    private final ManagedNoticeEvent _contentChangedEvent = Events.createNoticeEvent();

    @NonNull
    private final ManagedEvent<StoryEventArgs> _startEditStoryEvent = Events.createEvent();

    @NonNull
    private final ManagedEvent<StoryChangedEventArgs> _storyChangedEvent = Events.createEvent();

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    @Nullable
    private UIStory _story;
}
