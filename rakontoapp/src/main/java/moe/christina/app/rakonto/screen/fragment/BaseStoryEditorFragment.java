package moe.christina.app.rakonto.screen.fragment;

import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;

import lombok.Getter;
import lombok.experimental.Accessors;

import moe.christina.app.rakonto.core.adpter.storyEditorPages.StoryEditorPage;

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

    @CallSuper
    @Override
    public void notifyStartEditing(@Nullable final ReadyCallback callback) {
        _startEditingReadyCallback = callback;
    }

    @CallSuper
    @Override
    public void notifyStopEditing(@Nullable final ReadyCallback callback) {
        _stopEditingReadyCallback = callback;
    }

    @CallSuper
    protected void onCompleteStartEditingPreparation() {
        if (_startEditingReadyCallback != null) {
            _startEditingReadyCallback.onPageReady();

            _startEditingReadyCallback = null;
        }
    }

    @CallSuper
    protected void onCompleteStopEditingPreparation() {
        if (_stopEditingReadyCallback != null) {
            _stopEditingReadyCallback.onPageReady();

            _stopEditingReadyCallback = null;
        }
    }

    protected abstract void onStoryIdChanged();

    @Nullable
    private ReadyCallback _startEditingReadyCallback;

    @Nullable
    private ReadyCallback _stopEditingReadyCallback;

    @Getter(onMethod = @__(@Override))
    @Nullable
    private Long _storyId;
}
