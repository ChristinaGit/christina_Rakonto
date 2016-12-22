package com.christina.app.story.presentation;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.christina.api.story.model.Story;
import com.christina.api.story.observer.StoryObserverEventArgs;
import com.christina.app.story.R;
import com.christina.app.story.core.StoryEventArgs;
import com.christina.app.story.manager.ServiceManager;
import com.christina.app.story.view.StoryTextEditorPresentableView;
import com.christina.common.AsyncCallback;
import com.christina.common.contract.Contracts;
import com.christina.common.event.EventHandler;
import com.christina.common.event.NoticeEventHandler;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;

@Accessors(prefix = "_")
public final class StoryTextEditorPresenter
    extends BaseStoryPresenter<StoryTextEditorPresentableView> {
    public StoryTextEditorPresenter(
        @NonNull final ServiceManager serviceManager) {
        super(Contracts.requireNonNull(serviceManager, "serviceManager == null"));
    }

    @Nullable
    protected final Story getDisplayedStory() {
        final Story story;

        final val presentableView = getPresentableView();
        if (presentableView != null) {
            story = presentableView.getDisplayedStory();
        } else {
            story = null;
        }
        return story;
    }

    protected final void loadStory(final long storyId) {
        getStoryTaskManager().loadStory(storyId, new AsyncCallback<Story, Exception>() {
            @Override
            public void onError(@Nullable final Exception error) {
                onStoryLoadError(error);
            }

            @Override
            public void onSuccess(@Nullable final Story result) {
                onStoryLoaded(result);
            }
        });
    }

    @Override
    protected void onBindPresentableView(
        @NonNull final StoryTextEditorPresentableView presentableView) {
        super.onBindPresentableView(Contracts.requireNonNull(presentableView,
                                                             "presentableView == null"));

        presentableView.getOnStartEditStoryEvent().addHandler(getStartEditStoryHandler());
        presentableView.getOnStoryChangedEvent().addHandler(getStoryChangedHandler());
    }

    @Override
    protected void onUnbindPresentableView(
        @NonNull final StoryTextEditorPresentableView presentableView) {
        super.onUnbindPresentableView(Contracts.requireNonNull(presentableView,
                                                               "presentableView == null"));

        presentableView.getOnStartEditStoryEvent().removeHandler(getStartEditStoryHandler());
        presentableView.getOnStoryChangedEvent().removeHandler(getStoryChangedHandler());
    }

    @Override
    protected void onViewAppear(
        @NonNull final StoryTextEditorPresentableView presentableView) {
        super.onViewAppear(Contracts.requireNonNull(presentableView, "presentableView == null"));

        presentableView.setStoryVisible(false);
        presentableView.setLoadingVisible(true);

        getStoryContentObserver()
            .getOnStoryChangedEvent()
            .addHandler(getStoryExternalChangedHandler());
    }

    @Override
    protected void onViewDisappear(
        @NonNull final StoryTextEditorPresentableView presentableView) {
        super.onViewDisappear(Contracts.requireNonNull(presentableView, "presentableView == null"));

        getStoryContentObserver()
            .getOnStoryChangedEvent()
            .removeHandler(getStoryExternalChangedHandler());
    }

    protected void onStartEditStory(final long storyId) {
        loadStory(storyId);
    }

    protected void onStoryChanged(@NonNull final Story story) {
        Contracts.requireNonNull(story, "story == null");

        story.setModifyDate(System.currentTimeMillis());
        getStoryTaskManager().updateStory(story, new AsyncCallback<Integer, Exception>() {
            @Override
            public void onError(@Nullable final Exception error) {
            }

            @Override
            public void onSuccess(@Nullable final Integer result) {
                //                getStoryDao().delete(StoryFrameSelections.byStoryId(story.getId
                // ()));
                //                String storyText = story.getText();
                //
                //                if (storyText != null) {
                //                    storyText = StoryTextUtils.cleanup(storyText);
                //                    final val storyDefaultSplit = StoryTextUtils.defaultSplit
                // (storyText);
                //
                //                }
            }
        });
    }

    protected void onStoryExternalChanged(final long storyId) {
        loadStory(storyId);
    }

    protected void onStoryLoadError(@Nullable final Exception error) {
        getMessageManager().showInfoMessage(R.string.message_story_load_fail);
    }

    protected void onStoryLoaded(@Nullable final Story story) {
        final val presentableView = getPresentableView();
        if (presentableView != null) {
            presentableView.setLoadingVisible(false);
            presentableView.setStoryVisible(true);
            presentableView.displayStory(story);
        }
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
    private final NoticeEventHandler _storyChangedHandler = new NoticeEventHandler() {
        @Override
        public void onEvent() {
            final val story = getDisplayedStory();

            if (story != null) {
                onStoryChanged(new Story(story));
            }
        }
    };

    @Getter(value = AccessLevel.PRIVATE, lazy = true)
    @NonNull
    private final EventHandler<StoryObserverEventArgs> _storyExternalChangedHandler =
        new EventHandler<StoryObserverEventArgs>() {
            @Override
            public void onEvent(@NonNull final StoryObserverEventArgs eventArgs) {
                final val displayedStory = getDisplayedStory();
                final long changedStoryId = eventArgs.getId();

                if (displayedStory != null && displayedStory.getId() == changedStoryId) {
                    onStoryExternalChanged(changedStoryId);
                }
            }
        };
}