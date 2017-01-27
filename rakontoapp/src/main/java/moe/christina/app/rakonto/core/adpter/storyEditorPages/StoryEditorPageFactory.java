package moe.christina.app.rakonto.core.adpter.storyEditorPages;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

public interface StoryEditorPageFactory {
    @NonNull
    Fragment createPageFragment(int position);

    int getPageCount();
}
