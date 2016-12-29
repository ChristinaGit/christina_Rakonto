package com.christina.app.story.presentation;

import android.support.annotation.NonNull;

import com.christina.api.story.model.Story;
import com.christina.api.story.observer.StoryObserverEventArgs;
import com.christina.app.story.R;
import com.christina.app.story.core.StoryEventArgs;
import com.christina.app.story.manager.ServiceManager;
import com.christina.app.story.view.StoriesListPresentableView;
import com.christina.common.contract.Contracts;
import com.christina.common.data.cursor.dataCursor.DataCursor;
import com.christina.common.event.EventHandler;

import java.util.concurrent.Callable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;
import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;

@Accessors(prefix = "_")
public final class StoriesListPresenter extends BaseStoryPresenter<StoriesListPresentableView> {
    public StoriesListPresenter(
        @NonNull final ServiceManager serviceManager) {
        super(Contracts.requireNonNull(serviceManager, "serviceManager == null"));
    }

    protected void loadStories() {
        final val presentableView = getPresentableView();
        if (presentableView != null) {
            presentableView.setStoriesVisible(false);
            presentableView.setLoadingVisible(true);
        }

        final val rxManager = getRxManager();
        rxManager
            .autoManage(Observable.fromCallable(new Callable<DataCursor<Story>>() {
                @Override
                public DataCursor<Story> call()
                    throws Exception {
                    Contracts.requireWorkerThread();

                    return getStoryDao().getAll().asDataCursor();
                }
            }))
            .subscribeOn(rxManager.getIOScheduler())
            .observeOn(rxManager.getUIScheduler())
            .doOnError(new Action1<Throwable>() {
                @Override
                public void call(final Throwable error) {
                    Contracts.requireMainThread();

                    final val presentableView = getPresentableView();
                    if (presentableView != null) {
                        presentableView.displayStories(null);
                    }

                    getMessageManager().showInfoMessage(R.string.message_stories_load_fail);
                }
            })
            .doOnNext(new Action1<DataCursor<Story>>() {
                @Override
                public void call(final DataCursor<Story> stories) {
                    Contracts.requireMainThread();

                    final val presentableView = getPresentableView();
                    if (presentableView != null) {
                        presentableView.displayStories(stories);
                    }
                }
            })
            .doOnCompleted(new Action0() {
                @Override
                public void call() {
                    Contracts.requireMainThread();

                    final val presentableView = getPresentableView();
                    if (presentableView != null) {
                        presentableView.setStoriesVisible(true);
                        presentableView.setLoadingVisible(false);
                    }
                }
            })
            .subscribe();
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
    private final EventHandler<StoryObserverEventArgs> _storyExternalChangedHandler =
        new EventHandler<StoryObserverEventArgs>() {
            @Override
            public void onEvent(@NonNull final StoryObserverEventArgs eventArgs) {
                Contracts.requireNonNull(eventArgs, "eventArgs == null");

                onStoriesExternalChanged();
            }
        };
}
