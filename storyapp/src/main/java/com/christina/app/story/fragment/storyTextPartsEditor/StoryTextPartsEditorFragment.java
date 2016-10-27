package com.christina.app.story.fragment.storyTextPartsEditor;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.christina.api.story.model.Story;
import com.christina.app.story.R;
import com.christina.app.story.core.StoryTextUtils;
import com.christina.app.story.fragment.singleStory.BaseSingleStoryFragment;
import com.christina.app.story.fragment.storyTextPartsEditor.adapter.StoryTextPartsAdapter;
import com.christina.common.contract.Contracts;

import java.util.List;

public class StoryTextPartsEditorFragment extends BaseSingleStoryFragment {
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container,
        @Nullable final Bundle savedInstanceState) {
        final View view =
            inflater.inflate(R.layout.fragment_story_text_parts_editor, container, false);

        _storyTextPartsView = (RecyclerView) view.findViewById(R.id.raw_story_frames);
        onInitializeStoryTextPartsView(_storyTextPartsView);

        return view;
    }

    @Nullable
    protected final List<String> getStoryTextParts() {
        final List<String> storyTextParts;

        String storyText = null;

        final Story story = getStory();
        if (story != null) {
            storyText = story.getText();
        }

        if (storyText != null) {
            storyText = StoryTextUtils.cleanup(storyText);

            storyTextParts = StoryTextUtils.defaultSplit(storyText);
        } else {
            storyTextParts = null;
        }

        return storyTextParts;
    }

    @NonNull
    protected final StoryTextPartsAdapter getStoryTextPartsAdapter() {
        if (_storyTextPartsAdapter == null) {
            _storyTextPartsAdapter = new StoryTextPartsAdapter();
        }
        return _storyTextPartsAdapter;
    }

    @NonNull
    protected final LinearLayoutManager getStoryTextPartsLayoutManager() {
        if (_storyTextPartsLayoutManager == null) {
            _storyTextPartsLayoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        }

        return _storyTextPartsLayoutManager;
    }

    @Override
    protected void onStoryLoaded() {
        final List<String> storyTextParts = getStoryTextParts();
        if (storyTextParts != null) {
            getStoryTextPartsAdapter().setItems(storyTextParts);
        } else {
            getStoryTextPartsAdapter().removeItems();
        }
    }

    @Override
    protected void onStoryReset() {
        final StoryTextPartsAdapter storyTextPartsAdapter = getStoryTextPartsAdapter();
        storyTextPartsAdapter.removeItems(false);
        storyTextPartsAdapter.notifyDataSetChanged();
    }

    @Nullable
    private StoryTextPartsAdapter _storyTextPartsAdapter;

    @Nullable
    private LinearLayoutManager _storyTextPartsLayoutManager;

    @Nullable
    private RecyclerView _storyTextPartsView;

    private void onInitializeStoryTextPartsView(@NonNull final RecyclerView rawStoryFramesView) {
        Contracts.requireNonNull(rawStoryFramesView, "rawStoryFramesView == null");

        rawStoryFramesView.setLayoutManager(getStoryTextPartsLayoutManager());
        rawStoryFramesView.setAdapter(getStoryTextPartsAdapter());
    }
}
