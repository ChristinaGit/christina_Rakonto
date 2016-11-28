package com.christina.app.story.presentation;

import android.support.annotation.NonNull;

import com.christina.app.story.R;
import com.christina.app.story.manager.content.StoryContentObserverManager;
import com.christina.app.story.manager.message.MessageManager;
import com.christina.app.story.manager.navigation.StoryNavigator;
import com.christina.app.story.manager.navigation.editStory.InsertStoryNavigationCallback;
import com.christina.app.story.manager.navigation.editStory.InsertStoryNavigationResult;
import com.christina.app.story.view.StoriesViewerPresentableView;
import com.christina.common.contract.Contracts;
import com.christina.common.event.NoticeEventHandler;
import com.christina.common.view.presentation.AbstractPresenter;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
public class StoriesViewerPresenter extends AbstractPresenter<StoriesViewerPresentableView> {
    public StoriesViewerPresenter(
        @NonNull final StoryNavigator storyNavigator,
        @NonNull final MessageManager messageManager,
        @NonNull final StoryContentObserverManager storyContentObserverManager) {
        Contracts.requireNonNull(storyNavigator, "storyNavigator == null");
        Contracts.requireNonNull(messageManager, "messageManager == null");
        Contracts.requireNonNull(storyContentObserverManager,
                                 "storyContentObserverManager == null");

        _storyNavigator = storyNavigator;
        _messageManager = messageManager;
        _storyContentObserverManager = storyContentObserverManager;
    }

    @Override
    protected void onBindPresentableView(
        @NonNull final StoriesViewerPresentableView presentableView) {
        super.onBindPresentableView(presentableView);
        Contracts.requireNonNull(presentableView, "presentableView == null");

        presentableView.getOnInsertStoryEvent().addHandler(getViewInsertStoryHandler());
    }

    @Override
    protected void onUnbindPresentableView(
        @NonNull final StoriesViewerPresentableView presentableView) {
        super.onUnbindPresentableView(presentableView);
        Contracts.requireNonNull(presentableView, "presentableView == null");

        presentableView.getOnInsertStoryEvent().removeHandler(getViewInsertStoryHandler());
    }

    @Override
    protected void onViewAppear() {
        super.onViewAppear();

        getStoryContentObserverManager().registerStoryContentObserver();
    }

    @Override
    protected void onViewDisappear() {
        super.onViewDisappear();

        getStoryContentObserverManager().unregisterStoryContentObserver();
    }

    protected void onInsertStory() {
        getStoryNavigator().navigateToInsertStory(new InsertStoryNavigationCallback() {
            @Override
            public void onInsertStoryNavigationResult(
                @NonNull final InsertStoryNavigationResult result) {
                Contracts.requireNonNull(result, "result == null");

                if (result == InsertStoryNavigationResult.RESULT_OK) {
                    getMessageManager().showInfoMessage(R.string.message_story_inserted);
                } else {
                    getMessageManager().showInfoMessage(R.string.message_story_insert_fail);
                }
            }
        });
    }

    @Getter(AccessLevel.PROTECTED)
    @NonNull
    private final MessageManager _messageManager;

    @Getter(AccessLevel.PROTECTED)
    @NonNull
    private final StoryContentObserverManager _storyContentObserverManager;

    @Getter(AccessLevel.PROTECTED)
    @NonNull
    private final StoryNavigator _storyNavigator;

    @Getter(value = AccessLevel.PRIVATE, lazy = true)
    @NonNull
    private final NoticeEventHandler _viewInsertStoryHandler = new NoticeEventHandler() {
        @Override
        public void onEvent() {
            onInsertStory();
        }
    };
}
