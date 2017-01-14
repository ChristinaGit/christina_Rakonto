package com.christina.app.story.view.activity.storyEditor;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.christina.app.story.core.adpter.storyEditorPages.StoryEditorPageFactory;
import com.christina.app.story.view.fragment.storyFramesEditor.StoryFramesEditorFragment;
import com.christina.app.story.view.fragment.storyTextEditor.StoryTextEditorFragment;

public final class StoryEditorPages implements StoryEditorPageFactory {
    private static int _positionIndexer = 0;

    public static final int POSITION_TEXT_EDITOR = _positionIndexer++;

    public static final int POSITION_FRAMES_EDITOR = _positionIndexer++;

    @NonNull
    @Override
    public final Fragment createPageFragment(final int position) {
        final Fragment pageFragment;

        if (POSITION_TEXT_EDITOR == position) {
            pageFragment = new StoryTextEditorFragment();
        } else if (POSITION_FRAMES_EDITOR == position) {
            pageFragment = new StoryFramesEditorFragment();
        } else {
            throw new IllegalArgumentException("Illegal position: " + position);
        }

        return pageFragment;
    }

    @Override
    public final int getPageCount() {
        return _positionIndexer;
    }
}
