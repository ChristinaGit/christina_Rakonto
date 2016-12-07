package com.christina.app.story.presentation;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.christina.api.story.model.Story;
import com.christina.app.story.R;
import com.christina.app.story.core.StoryContentEventArgs;
import com.christina.app.story.core.StoryEventArgs;
import com.christina.app.story.manager.ServiceManager;
import com.christina.app.story.view.StoryTextEditorPresentableView;
import com.christina.common.AsyncCallback;
import com.christina.common.contract.Contracts;
import com.christina.common.event.EventHandler;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;

@Accessors(prefix = "_")
public final class StoryTextEditorPresenter
    extends BaseStoryPresenter<StoryTextEditorPresentableView> {
    public StoryTextEditorPresenter(
        @NonNull final ServiceManager serviceManager) {
        super(serviceManager);
        Contracts.requireNonNull(serviceManager, "serviceManager == null");
    }

    @Override
    protected void onBindPresentableView(
        @NonNull final StoryTextEditorPresentableView presentableView) {
        super.onBindPresentableView(presentableView);
        Contracts.requireNonNull(presentableView, "presentableView == null");

        presentableView.getOnStartEditStoryEvent().addHandler(getStartEditStoryHandler());
        presentableView.getOnStoryChangedEvent().addHandler(getStoryChangedHandler());
    }

    @Override
    protected void onUnbindPresentableView(
        @NonNull final StoryTextEditorPresentableView presentableView) {
        super.onUnbindPresentableView(presentableView);
        Contracts.requireNonNull(presentableView, "presentableView == null");

        presentableView.getOnStartEditStoryEvent().removeHandler(getStartEditStoryHandler());
        presentableView.getOnStoryChangedEvent().removeHandler(getStoryChangedHandler());
    }

    protected void onStartEditStory(final long storyId) {
        getStoryTaskManager().loadStory(storyId, new AsyncCallback<Story, Exception>() {
            @Override
            public void onError(@Nullable final Exception error) {
                getMessageManager().showInfoMessage(R.string.message_story_load_fail);
            }

            @Override
            public void onSuccess(@Nullable final Story result) {
                final val presentableView = getPresentableView();
                if (presentableView != null) {
                    presentableView.displayStory(result);
                }
            }
        });
    }

    @Getter(value = AccessLevel.PRIVATE, lazy = true)
    @NonNull
    private final EventHandler<StoryEventArgs> _startEditStoryHandler =
        new EventHandler<StoryEventArgs>() {
            @Override
            public void onEvent(@NonNull final StoryEventArgs eventArgs) {
                Contracts.requireNonNull(eventArgs, "eventArgs == null");

                onStartEditStory(eventArgs.getStoryId());
            }
        };

    @Getter(value = AccessLevel.PRIVATE, lazy = true)
    @NonNull
    private final EventHandler<StoryContentEventArgs> _storyChangedHandler =
        new EventHandler<StoryContentEventArgs>() {
            @Override
            public void onEvent(@NonNull final StoryContentEventArgs eventArgs) {
                Contracts.requireNonNull(eventArgs, "eventArgs == null");

                final val story = eventArgs.getStory();

                if (story != null) {
                    getStoryTaskManager().updateStory(story);
                }
            }
        };
}
