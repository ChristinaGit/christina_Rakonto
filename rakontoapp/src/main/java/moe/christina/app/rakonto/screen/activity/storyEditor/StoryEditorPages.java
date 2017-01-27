package moe.christina.app.rakonto.screen.activity.storyEditor;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import moe.christina.app.rakonto.core.adpter.storyEditorPages.StoryEditorPageFactory;
import moe.christina.app.rakonto.screen.fragment.storyFramesEditor.StoryFramesEditorFragment;
import moe.christina.app.rakonto.screen.fragment.storyTextEditor.StoryTextEditorFragment;

/*package-private*/ final class StoryEditorPages implements StoryEditorPageFactory {
    public static final int POSITION_TEXT_EDITOR;

    public static final int POSITION_FRAMES_EDITOR;

    protected static final int POSITION_LAST;

    static {
        int positionIndexer = 0;

        POSITION_TEXT_EDITOR = positionIndexer++;
        POSITION_FRAMES_EDITOR = positionIndexer++;

        POSITION_LAST = positionIndexer;
    }

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
        return POSITION_LAST;
    }
}
