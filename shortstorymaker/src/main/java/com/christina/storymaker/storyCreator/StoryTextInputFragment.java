package com.christina.storymaker.storyCreator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.christina.storymaker.R;

public final class StoryTextInputFragment extends Fragment {
    public static StoryTextInputFragment create() {
        return new StoryTextInputFragment();
    }

    @Nullable
    public String getStoryText() {
        final String storyText;

        if (_storyTextView != null) {
            storyText = _storyTextView.getText().toString();
        } else {
            storyText = null;
        }

        return storyText;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container,
        @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.new_story_text_input, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        _storyTextView = (EditText) view.findViewById(R.id.story_text);
    }

    @Nullable
    private EditText _storyTextView;
}
