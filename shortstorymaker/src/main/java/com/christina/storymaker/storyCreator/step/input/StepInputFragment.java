package com.christina.storymaker.storyCreator.step.input;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.christina.common.BaseTextWatcher;
import com.christina.common.event.BaseEvent;
import com.christina.common.event.Event;
import com.christina.storymaker.R;

public final class StepInputFragment extends Fragment {
    public static StepInputFragment create() {
        final StepInputFragment fragment = new StepInputFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @NonNull
    public final Event<StoryTextChangedEventArgs> onStoryTextChanged() {
        return _storyTextChanged;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container,
        @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.story_creator_step_input, container, false);
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
    }

    @NonNull
    private final BaseEvent<StoryTextChangedEventArgs> _storyTextChanged = new BaseEvent<>();

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
            _storyTextChanged.rise(new StoryTextChangedEventArgs(storyText));
        }
    };

    @Nullable
    private EditText _storyTextView;
}
