package com.christina.app.story.view.activity.storyEditor;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.christina.app.story.adpter.storyEditorPages.StoryEditorPageFactory;
import com.christina.app.story.view.fragment.storyFramesEditor.StoryFramesEditorFragment;
import com.christina.app.story.view.fragment.storyTextEditor.StoryTextEditorFragment;
import com.christina.common.contract.Contracts;

public final class StoryEditorPages implements StoryEditorPageFactory {
    private static int _positionIndexer = 0;

    public static final int POSITION_TEXT_EDITOR = _positionIndexer++;

    public static final int POSITION_FRAMES_EDITOR = _positionIndexer++;

    @NonNull
    @Override
    public final Fragment create(@NonNull final Integer argument) {
        Contracts.requireNonNull(argument, "argument == null");

        final Fragment pageFragment;

        if (POSITION_TEXT_EDITOR == argument) {
            pageFragment = new StoryTextEditorFragment();
        } else if (POSITION_FRAMES_EDITOR == argument) {
            pageFragment = new StoryFramesEditorFragment();
        } else {
            throw new IllegalArgumentException("Illegal position: " + argument);
        }

        return pageFragment;
    }

    @Override
    public final int getPageCount() {
        return _positionIndexer;
    }
}
