package com.christina.app.story.presentation;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.christina.api.story.model.Story;
import com.christina.api.story.observer.StoryObserverEventArgs;
import com.christina.app.story.R;
import com.christina.app.story.core.StoryEventArgs;
import com.christina.app.story.manager.ServiceManager;
import com.christina.app.story.view.StoriesListPresentableView;
import com.christina.common.AsyncCallback;
import com.christina.common.contract.Contracts;
import com.christina.common.data.cursor.dataCursor.DataCursor;
import com.christina.common.event.EventHandler;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;

@Accessors(prefix = "_")
public final class StoriesListPresenter extends BaseStoryPresenter<StoriesListPresentableView> {
    public StoriesListPresenter(
        @NonNull final ServiceManager serviceManager) {
        super(Contracts.requireNonNull(serviceManager, "serviceManager == null"));
    }

    protected void loadStories() {
        getStoryTaskManager().loadStories(getLoadStoriesCallback());
    }

    @Override
    protected void onBindPresentableView(
        @NonNull final StoriesListPresentableView presentableView) {
        super.onBindPresentableView(Contracts.requireNonNull(presentableView,
                                                             "presentableView == null"));

        presentableView.getOnDeleteStoryEvent().addHandler(getDeleteStoryHandler());
        presentableView.getOnEditStoryEvent().addHandler(getEditStoryHandler());
    }

    @Override
    protected void onUnbindPresentableView(
        @NonNull final StoriesListPresentableView presentableView) {
        super.onUnbindPresentableView(Contracts.requireNonNull(presentableView,
                                                               "presentableView == null"));

        presentableView.getOnDeleteStoryEvent().removeHandler(getDeleteStoryHandler());
        presentableView.getOnEditStoryEvent().removeHandler(getEditStoryHandler());
    }

    @Override
    protected void onViewAppear(@NonNull final StoriesListPresentableView presentableView) {
        super.onViewAppear(Contracts.requireNonNull(presentableView, "presentableView == null"));

        getStoryContentObserverManager().registerStoryContentObserver();

        getStoryContentObserver()
            .getOnStoryChangedEvent()
            .addHandler(getStoryExternalChangedHandler());

        presentableView.setStoriesVisible(false);
        presentableView.setLoadingVisible(true);

        loadStories();
    }

    @Override
    protected void onViewDisappear(@NonNull final StoriesListPresentableView presentableView) {
        super.onViewDisappear(Contracts.requireNonNull(presentableView, "presentableView == null"));

        getStoryContentObserverManager().unregisterStoryContentObserver();

        getStoryContentObserver()
            .getOnStoryChangedEvent()
            .removeHandler(getStoryExternalChangedHandler());
    }

    protected void onStoriesExternalChanged() {
        loadStories();
    }

    protected void onStoriesLoadError(@Nullable final Exception error) {
        getMessageManager().showInfoMessage(R.string.message_stories_load_fail);
    }

    protected void onStoriesLoaded(@Nullable final DataCursor<Story> stories) {
        final val presentableView = getPresentableView();
        if (presentableView != null) {
            presentableView.setStoriesVisible(true);
            presentableView.setLoadingVisible(false);
            presentableView.displayStories(stories);
        }
    }

    protected void onStoryDelete(final long id) {
        final int deleted = getStoryDao().delete(id);
        if (deleted > 0) {
            getMessageManager().showInfoMessage(R.string.message_story_deleted);
        }
    }

    protected void onStoryEdit(final long storyId) {
        getStoryNavigator().navigateToEditStory(storyId);
    }

    @Getter(value = AccessLevel.PRIVATE, lazy = true)
    @NonNull
    private final EventHandler<StoryEventArgs> _deleteStoryHandler =
        new EventHandler<StoryEventArgs>() {
            @Override
            public void onEvent(@NonNull final StoryEventArgs eventArgs) {
                Contracts.requireNonNull(eventArgs, "eventArgs == null");

                onStoryDelete(eventArgs.getStoryId());
            }
        };

    @Getter(value = AccessLevel.PRIVATE, lazy = true)
    @NonNull
    private final EventHandler<StoryEventArgs> _editStoryHandler =
        new EventHandler<StoryEventArgs>() {
            @Override
            public void onEvent(@NonNull final StoryEventArgs eventArgs) {
                Contracts.requireNonNull(eventArgs, "eventArgs == null");

                onStoryEdit(eventArgs.getStoryId());
            }
        };

    @Getter(value = AccessLevel.PRIVATE, lazy = true)
    @NonNull
    private final AsyncCallback<DataCursor<Story>, Exception> _loadStoriesCallback =
        new AsyncCallback<DataCursor<Story>, Exception>() {
            @Override
            public void onError(@Nullable final Exception error) {
                onStoriesLoadError(error);
            }

            @Override
            public void onSuccess(
                @Nullable final DataCursor<Story> result) {
                onStoriesLoaded(result);
            }
        };

    @Getter(value = AccessLevel.PRIVATE, lazy = true)
    @NonNull
    private final EventHandler<StoryObserverEventArgs> _storyExternalChangedHandler =
        new EventHandler<StoryObserverEventArgs>() {
            @Override
            public void onEvent(@NonNull final StoryObserverEventArgs eventArgs) {
                Contracts.requireNonNull(eventArgs, "eventArgs == null");

                onStoriesExternalChanged();
            }
        };
}
