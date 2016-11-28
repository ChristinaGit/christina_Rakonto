package com.christina.app.story.view;

import android.support.annotation.NonNull;

import com.christina.common.event.NoticeEvent;
import com.christina.common.view.presentation.PresentableView;

public interface StoriesViewerPresentableView extends PresentableView {
    @NonNull
    NoticeEvent getOnInsertStoryEvent();
}
