package com.christina.app.story.presentation;

import android.support.annotation.NonNull;

import com.christina.app.story.manager.ServiceManager;
import com.christina.app.story.view.StoryFramesEditorPresentableView;
import com.christina.common.contract.Contracts;

public class StoryFramesEditorPresenter
    extends BaseStoryPresenter<StoryFramesEditorPresentableView> {
    public StoryFramesEditorPresenter(
        @NonNull final ServiceManager serviceManager) {
        super(Contracts.requireNonNull(serviceManager, "serviceManager == null"));
    }
}
