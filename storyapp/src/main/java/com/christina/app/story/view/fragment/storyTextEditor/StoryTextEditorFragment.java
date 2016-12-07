package com.christina.app.story.view.fragment.storyTextEditor;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.christina.api.story.model.Story;
import com.christina.app.story.R;
import com.christina.app.story.adpter.editStoryScreens.StoryEditorPage;
import com.christina.app.story.core.StoryContentEventArgs;
import com.christina.app.story.core.StoryEventArgs;
import com.christina.app.story.presentation.StoryTextEditorPresenter;
import com.christina.app.story.view.StoryTextEditorPresentableView;
import com.christina.app.story.view.fragment.BaseStoryFragment;
import com.christina.common.ImeUtils;
import com.christina.common.event.BaseEvent;
import com.christina.common.event.BaseNoticeEvent;
import com.christina.common.event.Event;
import com.christina.common.event.NoticeEvent;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnTextChanged;
import butterknife.OnTextChanged.Callback;
import lombok.AccessLevel;
import lombok.Getter;
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
        return _onContentChangedChangedEvent;
    }

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

    @Override
    public void onStartEditing() {
        if (getEditedStory() == null) {
            final long editedStoryId = getEditedStoryId();
            if (editedStoryId != Story.NO_ID) {
                _onStartEditStoryEvent.rise(new StoryEventArgs(editedStoryId));
            }
        }
    }

    @Override
    public void onStopEditing() {
        if (_storyTextView != null && _storyTextView.hasFocus()) {
            ImeUtils.hideIme(_storyTextView);
        }

        saveStoryChanges();
    }

    @Override
    public void displayStory(@Nullable final Story story) {
        setEditedStory(story);
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
    public void onPause() {
        super.onPause();

        saveStoryChanges();
    }

    @Nullable
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
    public void onDestroy() {
        super.onDestroy();

        unbindViews();
    }

    @Override
    protected void onBindPresenter() {
        super.onBindPresenter();

        final val presenter = getStoryTextEditorPresenter();
        if (presenter != null) {
            presenter.setPresentableView(this);
        }
    }

    @Override
    protected void onUnbindPresenter() {
        super.onUnbindPresenter();

        final val presenter = getStoryTextEditorPresenter();
        if (presenter != null) {
            presenter.setPresentableView(null);
        }
    }

    protected final void saveStoryChanges() {
        final val editedStory = getEditedStory();
        if (editedStory != null) {
            editedStory.setModifyDate(System.currentTimeMillis());
            _onStoryChangedEvent.rise(new StoryContentEventArgs(editedStory));
        }
    }

    protected final void setEditedStory(@Nullable final Story editedStory) {
        if (_editedStory != editedStory) {
            _editedStory = editedStory;

            onEditedStoryChanged();
        }
    }

    protected void onEditedStoryChanged() {
        final val editedStory = getEditedStory();

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

        _onContentChangedChangedEvent.rise();
    }

    protected void onEditedStoryIdChanged() {
        setEditedStory(null);

        final long editedStoryId = getEditedStoryId();
        if (editedStoryId != Story.NO_ID) {
            _onStartEditStoryEvent.rise(new StoryEventArgs(editedStoryId));
        }
    }

    @Override
    protected void onInject() {
        super.onInject();

        getStoryViewFragmentComponent().inject(this);
    }

    @OnTextChanged(value = R.id.story_text, callback = Callback.AFTER_TEXT_CHANGED)
    protected void onStoryTextChanged(final Editable s) {
        final val storyText = s != null ? s.toString() : null;

        final val editedStory = getEditedStory();
        if (editedStory != null) {
            editedStory.setText(storyText);

            _onContentChangedChangedEvent.rise();
        }
    }

    @Inject
    @Getter(AccessLevel.PROTECTED)
    @Nullable
    /*package-private*/ StoryTextEditorPresenter _storyTextEditorPresenter;

    @BindView(R.id.story_text)
    @Nullable
    /*package-private*/ EditText _storyTextView;

    @NonNull
    private final BaseNoticeEvent _onContentChangedChangedEvent = new BaseNoticeEvent();

    @NonNull
    private final BaseEvent<StoryEventArgs> _onStartEditStoryEvent = new BaseEvent<>();

    @NonNull
    private final BaseEvent<StoryContentEventArgs> _onStoryChangedEvent = new BaseEvent<>();

    @Getter(AccessLevel.PROTECTED)
    @Nullable
    private Story _editedStory;

    @Getter(onMethod = @__(@Override))
    private long _editedStoryId;
}
