package com.christina.app.story.presentation;

import android.support.annotation.NonNull;

import com.christina.app.story.R;
import com.christina.app.story.core.manager.ServiceManager;
import com.christina.app.story.core.manager.navigation.NavigationResult;
import com.christina.app.story.core.manager.navigation.editStory.InsertStoryNavigationCallback;
import com.christina.app.story.view.StoriesViewerPresentableView;
import com.christina.common.contract.Contracts;
import com.christina.common.event.NoticeEventHandler;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
public final class StoriesViewerPresenter extends BaseStoryPresenter<StoriesViewerPresentableView> {
    public StoriesViewerPresenter(
        @NonNull final ServiceManager serviceManager) {
        super(Contracts.requireNonNull(serviceManager, "serviceManager == null"));
    }

    @Override
    protected void onBindPresentableView(
        @NonNull final StoriesViewerPresentableView presentableView) {
        super.onBindPresentableView(Contracts.requireNonNull(presentableView,
                                                             "presentableView == null"));

        presentableView.getOnInsertStoryEvent().addHandler(getViewInsertStoryHandler());
    }

    @Override
    protected void onUnbindPresentableView(
        @NonNull final StoriesViewerPresentableView presentableView) {
        super.onUnbindPresentableView(Contracts.requireNonNull(presentableView,
                                                               "presentableView == null"));

        presentableView.getOnInsertStoryEvent().removeHandler(getViewInsertStoryHandler());
    }

    @Getter(value = AccessLevel.PRIVATE, lazy = true)
    @NonNull
    private final NoticeEventHandler _viewInsertStoryHandler = new NoticeEventHandler() {
        @Override
        public void onEvent() {
            getStoryNavigator().navigateToInsertStory(new InsertStoryNavigationCallback() {
                @Override
                public void onInsertStoryNavigationResult(
                    @NonNull final NavigationResult result) {
                    Contracts.requireNonNull(result, "result == null");

                    if (result == NavigationResult.SUCCESS) {
                        getMessageManager().showInfoMessage(R.string.message_story_inserted);
                    } else {
                        getMessageManager().showInfoMessage(R.string.message_story_insert_fail);
                    }
                }
            });
        }
    };
}
