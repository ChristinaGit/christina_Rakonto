package com.christina.app.story.view.fragment;

import android.support.annotation.Nullable;

import lombok.Getter;
import lombok.experimental.Accessors;

import com.christina.app.story.core.adpter.storyEditorPages.StoryEditorPage;

import java.util.Objects;

@Accessors(prefix = "_")
public abstract class BaseStoryEditorFragment extends BaseStoryFragment implements StoryEditorPage {
    @Override
    public final void setStoryId(@Nullable final Long storyId) {
        if (!Objects.equals(_storyId, storyId)) {
            _storyId = storyId;

            onStoryIdChanged();
        }
    }

    protected abstract void onStoryIdChanged();

    @Getter(onMethod = @__(@Override))
    @Nullable
    private Long _storyId;
}
