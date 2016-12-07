package com.christina.app.story.presentation;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.christina.api.story.model.Story;
import com.christina.app.story.R;
import com.christina.app.story.core.StoryEventArgs;
import com.christina.app.story.manager.ServiceManager;
import com.christina.app.story.view.StoryEditorPresentableView;
import com.christina.common.AsyncCallback;
import com.christina.common.contract.Contracts;
import com.christina.common.event.EventHandler;
import com.christina.common.event.NoticeEventHandler;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;

@Accessors(prefix = "_")
public final class StoryEditorPresenter extends BaseStoryPresenter<StoryEditorPresentableView> {
    public StoryEditorPresenter(
        @NonNull final ServiceManager serviceManager) {
        super(serviceManager);
        Contracts.requireNonNull(serviceManager, "serviceManager == null");
    }

    @Override
    protected void onBindPresentableView(
        @NonNull final StoryEditorPresentableView presentableView) {
        super.onBindPresentableView(presentableView);
        Contracts.requireNonNull(presentableView, "presentableView == null");

        presentableView.getOnInsertStoryEvent().addHandler(getInsertStoryHandler());
        presentableView.getOnEditStoryEvent().addHandler(getEditStoryHandler());
    }

    @Override
    protected void onUnbindPresentableView(
        @NonNull final StoryEditorPresentableView presentableView) {
        super.onUnbindPresentableView(presentableView);
        Contracts.requireNonNull(presentableView, "presentableView == null");

        presentableView.getOnInsertStoryEvent().removeHandler(getInsertStoryHandler());
        presentableView.getOnEditStoryEvent().removeHandler(getEditStoryHandler());
    }

    protected void onEditStory(final long storyId) {
        getStoryTaskManager().loadStory(storyId, new AsyncCallback<Story, Exception>() {
            @Override
            public void onError(@Nullable final Exception error) {
                getMessageManager().showInfoMessage(R.string.message_story_load_fail);
            }

            @Override
            public void onSuccess(@Nullable final Story result) {
                final long storyId = result != null ? result.getId() : Story.NO_ID;

                final val presentableView = getPresentableView();
                if (presentableView != null) {
                    presentableView.displayStory(storyId);
                }
            }
        });
    }

    protected void onInsertStory() {
        getStoryTaskManager().insertStory(new AsyncCallback<Story, Exception>() {
            @Override
            public void onError(@Nullable final Exception error) {
                getMessageManager().showInfoMessage(R.string.message_story_insert_fail);
            }

            @Override
            public void onSuccess(@Nullable final Story result) {
                final long storyId = result != null ? result.getId() : Story.NO_ID;

                final val presentableView = getPresentableView();
                if (presentableView != null) {
                    presentableView.displayStory(storyId);
                }
            }
        });
    }

    @Getter(value = AccessLevel.PRIVATE, lazy = true)
    @NonNull
    private final EventHandler<StoryEventArgs> _editStoryHandler =
        new EventHandler<StoryEventArgs>() {
            @Override
            public void onEvent(@NonNull final StoryEventArgs eventArgs) {
                Contracts.requireNonNull(eventArgs, "eventArgs == null");

                onEditStory(eventArgs.getStoryId());
            }
        };

    @Getter(value = AccessLevel.PRIVATE, lazy = true)
    @NonNull
    private final NoticeEventHandler _insertStoryHandler = new NoticeEventHandler() {
        @Override
        public void onEvent() {
            onInsertStory();
        }
    };
}
