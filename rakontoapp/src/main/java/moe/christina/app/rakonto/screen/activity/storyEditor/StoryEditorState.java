package moe.christina.app.rakonto.screen.activity.storyEditor;

import android.support.annotation.Nullable;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import org.parceler.Parcel;

@Parcel
@Accessors(prefix = "_")
/*package-private*/ final class StoryEditorState {
    @Getter
    @Setter
    /*package-private*/ int _activePage = 0;

    @Getter
    @Setter
    @Nullable
    /*package-private*/ StoryEditorMode _mode;

    @Getter
    @Setter
    @Nullable
    /*package-private*/ Long _storyId;
}
