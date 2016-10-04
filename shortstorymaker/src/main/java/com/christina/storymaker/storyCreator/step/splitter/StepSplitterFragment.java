package com.christina.storymaker.storyCreator.step.splitter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.christina.common.contract.Contracts;
import com.christina.storymaker.R;

public class StepSplitterFragment extends Fragment {
    public static final String EXTRA_STORY_RAW_TEXT = "story_raw_text";

    public static StepSplitterFragment create() {
        final StepSplitterFragment fragment = new StepSplitterFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    public static void putStoryRawText(@NonNull final Bundle arguments,
        @Nullable final String text) {
        Contracts.requireNonNull(arguments, "arguments == null");

        arguments.putString(EXTRA_STORY_RAW_TEXT, text);
    }

    public static String getStoryRawText(@NonNull final Bundle arguments) {
        Contracts.requireNonNull(arguments, "arguments == null");

        return arguments.getString(EXTRA_STORY_RAW_TEXT, null);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container,
        @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.story_creator_step_splitter, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        _storyTestView = (TextView) view.findViewById(R.id.story_test_view);
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        _rawStoryText = getStoryRawText(getArguments());

        _storyTestView.setText(_rawStoryText);
    }

    private TextView _storyTestView;

    @Nullable
    private String _rawStoryText;
}
