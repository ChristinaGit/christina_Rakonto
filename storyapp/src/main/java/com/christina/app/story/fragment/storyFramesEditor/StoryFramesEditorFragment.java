package com.christina.app.story.fragment.storyFramesEditor;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.christina.app.story.R;
import com.christina.app.story.fragment.storyFramesEditor.adapter.StoryFramesAdapter;
import com.christina.app.story.view.fragment.BaseStoryFragment;
import com.christina.common.contract.Contracts;

public class StoryFramesEditorFragment extends BaseStoryFragment {
    @Nullable
    @Override
    public View onCreateView(
        final LayoutInflater inflater,
        @Nullable final ViewGroup container,
        @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_story_frames_editor, container, false);

        _storyFramesView = (RecyclerView) view.findViewById(R.id.story_frames);
        onInitializeStoryFramesView(_storyFramesView);

        return view;
    }

    @NonNull
    protected final StoryFramesAdapter getStoryFramesAdapter() {
        if (_storyFramesAdapter == null) {
            _storyFramesAdapter = new StoryFramesAdapter();
        }
        return _storyFramesAdapter;
    }

    @NonNull
    protected final LinearLayoutManager getStoryFramesLayoutManager() {
        if (_storyFramesLayoutManager == null) {
            _storyFramesLayoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        }

        return _storyFramesLayoutManager;
    }

    protected void onStoryLoaded() {
        //        final List<String> storyTextParts = getStoryTextParts();
        //        if (storyTextParts != null) {
        //            getStoryFramesAdapter().setItems(storyTextParts);
        //        } else {
        //            getStoryFramesAdapter().removeItems();
        //        }
    }

    protected void onStoryReset() {
        final StoryFramesAdapter storyFramesAdapter = getStoryFramesAdapter();
        storyFramesAdapter.removeItems(false);
        storyFramesAdapter.notifyDataSetChanged();
    }

    @Nullable
    private StoryFramesAdapter _storyFramesAdapter;

    @Nullable
    private LinearLayoutManager _storyFramesLayoutManager;

    @Nullable
    private RecyclerView _storyFramesView;

    private void onInitializeStoryFramesView(@NonNull final RecyclerView storyFramesView) {
        Contracts.requireNonNull(storyFramesView, "storyFramesView == null");

        storyFramesView.setLayoutManager(getStoryFramesLayoutManager());
        storyFramesView.setAdapter(getStoryFramesAdapter());
    }
}
