package com.christina.app.story.fragment.storyTextEditor;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.christina.api.story.dao.StoryDaoManager;
import com.christina.api.story.model.Story;
import com.christina.app.story.R;
import com.christina.app.story.fragment.StoryEditorFragment;
import com.christina.app.story.fragment.singleStory.BaseSingleStoryFragment;
import com.christina.common.BaseTextWatcher;

public final class StoryTextEditorFragment extends BaseSingleStoryFragment
    implements StoryEditorFragment {
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container,
        @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_story_text_editor, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        _storyTextView = (EditText) view.findViewById(R.id.story_text);
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
    public void saveStoryChanges() {
        final Story story = getStory();
        if (story != null) {
            story.setModifyDate();
            StoryDaoManager.getStoryDao().update(story);
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
    private final TextWatcher _storyTextWatcher = new BaseTextWatcher() {
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
                story.setText(storyText);
            }
        }
    };

    @Nullable
    private EditText _storyTextView;
}
