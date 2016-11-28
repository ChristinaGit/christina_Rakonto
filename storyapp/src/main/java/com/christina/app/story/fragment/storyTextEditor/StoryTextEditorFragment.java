package com.christina.app.story.fragment.storyTextEditor;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.christina.api.story.model.Story;
import com.christina.app.story.R;
import com.christina.app.story.fragment.StoryEditorFragment;
import com.christina.app.story.fragment.fullSingleStory.FullSingleStoryFragment;
import com.christina.common.ImeUtils;
import com.christina.common.SimpleTextWatcher;
import com.christina.common.event.BaseEvent;
import com.christina.common.event.BaseNoticeEvent;
import com.christina.common.event.Event;
import com.christina.common.event.NoticeEvent;

public final class StoryTextEditorFragment extends FullSingleStoryFragment
    implements StoryEditorFragment {
    @NonNull
    public final Event<StoryTextChangedEventArgs> onStoryTextChanged() {
        return _storyTextChanged;
    }

    @Override
    public boolean hasContent() {
        final boolean hasContent;

        final Story story = getStory();
        if (story != null) {
            hasContent = !TextUtils.isEmpty(story.getText());
        } else {
            hasContent = false;
        }

        return hasContent;
    }

    @NonNull
    @Override
    public final NoticeEvent getOnContentChangedEvent() {
        return _onContentChangedChangedEvent;
    }

    @Override
    public void onStartEditing() {
        if (getStory() == null) {
            startStoryLoading();
        }
    }

    @Override
    public void onStopEditing() {
        if (_storyTextView != null && _storyTextView.hasFocus()) {
            ImeUtils.hideIme(_storyTextView);
        }
        saveStoryChanges();
    }

    @Nullable
    @Override
    public View onCreateFragmentView(
        final LayoutInflater inflater,
        @Nullable final ViewGroup container,
        @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_story_text_editor, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (_storyTextView != null) {
            _storyTextView.addTextChangedListener(_storyTextWatcher);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (_storyTextView != null) {
            _storyTextView.removeTextChangedListener(_storyTextWatcher);
        }

        saveStoryChanges();
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        _storyTextView = (EditText) view.findViewById(R.id.story_text);
    }

    protected final void saveStoryChanges() {
        final Story story = getStory();
        if (story != null) {
            story.setModifyDate(System.currentTimeMillis());
//            StoryDaoManager.getStoryDao().update(story);
        }
    }

    @Override
    protected void onStoryLoaded() {
        final Story story = getStory();

        if (_storyTextView != null) {
            final String storyText;

            if (story != null) {
                storyText = story.getText();
                _storyTextView.setEnabled(true);
            } else {
                storyText = null;
                _storyTextView.setEnabled(false);
            }

            _storyTextView.setText(storyText);
        } else {
            _onContentChangedChangedEvent.rise();
        }
    }

    @Override
    protected void onStoryReset() {
        if (_storyTextView != null) {
            _storyTextView.setText(null);
            _storyTextView.setEnabled(true);
        }
    }

    @NonNull
    private final BaseNoticeEvent _onContentChangedChangedEvent = new BaseNoticeEvent();

    @NonNull
    private final BaseEvent<StoryTextChangedEventArgs> _storyTextChanged = new BaseEvent<>();

    @NonNull
    private final TextWatcher _storyTextWatcher = new SimpleTextWatcher() {
        @Override
        public void afterTextChanged(final Editable s) {
            final String storyText;
            if (s != null) {
                storyText = s.toString();
            } else {
                storyText = null;
            }

            final Story story = getStory();
            if (story != null) {
                final String oldStoryText = story.getText();
                story.setText(storyText);

                _storyTextChanged.rise(new StoryTextChangedEventArgs(oldStoryText, storyText));
                _onContentChangedChangedEvent.rise();
            }
        }
    };

    @Nullable
    private EditText _storyTextView;
}
