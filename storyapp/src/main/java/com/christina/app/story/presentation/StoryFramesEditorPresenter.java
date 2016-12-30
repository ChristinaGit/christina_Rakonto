package com.christina.app.story.presentation;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.christina.api.story.model.Story;
import com.christina.api.story.model.StoryFrame;
import com.christina.api.story.observer.StoryObserverEventArgs;
import com.christina.app.story.R;
import com.christina.app.story.core.StoryEventArgs;
import com.christina.app.story.core.manager.ServiceManager;
import com.christina.app.story.view.StoryFramesEditorPresentableView;
import com.christina.common.AsyncCallback;
import com.christina.common.contract.Contracts;
import com.christina.common.data.cursor.dataCursor.DataCursor;
import com.christina.common.event.EventHandler;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

@Accessors(prefix = "_")
public final class StoryFramesEditorPresenter
    extends BaseStoryPresenter<StoryFramesEditorPresentableView> {
    public StoryFramesEditorPresenter(
        @NonNull final ServiceManager serviceManager) {
        super(Contracts.requireNonNull(serviceManager, "serviceManager == null"));
    }

    protected final long getEditedStoryId() {
        final long editedStoryId;

        final val presentableView = getPresentableView();
        if (presentableView != null) {
            editedStoryId = presentableView.getEditedStoryId();
        } else {
            editedStoryId = Story.NO_ID;
        }

        return editedStoryId;
    }

    protected final void loadStory(final long storyId) {
        final val rxManager = getRxManager();
        rxManager
            .autoManage(Observable.just(storyId))
            .observeOn(rxManager.getIOScheduler())
            .map(new Func1<Long, Story>() {
                @Override
                public Story call(final Long storyId) {
                    Contracts.requireWorkerThread();

                    return getStoryDao().get(storyId);
                }
            })
            .observeOn(rxManager.getUIScheduler())
            .doOnError(new Action1<Throwable>() {
                @Override
                public void call(final Throwable error) {
                    Contracts.requireMainThread();

                    final val presentableView = getPresentableView();
                    if (presentableView != null) {
                        presentableView.displayStory(null);
                    }

                    getMessageManager().showInfoMessage(R.string.message_story_load_fail);
                }
            })
            .doOnNext(new Action1<Story>() {
                @Override
                public void call(final Story story) {
                    Contracts.requireMainThread();

                    final val presentableView = getPresentableView();
                    if (presentableView != null) {
                        presentableView.displayStory(story);
                    }
                }
            })
            .observeOn(rxManager.getIOScheduler())
            .map(new Func1<Story, DataCursor<StoryFrame>>() {
                @Override
                public DataCursor<StoryFrame> call(final Story story) {
                    Contracts.requireWorkerThread();

                    return getStoryFrameDao().getAllByStoryId(story.getId()).asDataCursor();
                }
            })
            .observeOn(rxManager.getUIScheduler())
            .doOnError(new Action1<Throwable>() {
                @Override
                public void call(final Throwable error) {
                    Contracts.requireMainThread();

                    final val presentableView = getPresentableView();
                    if (presentableView != null) {
                        presentableView.displayStoryFrames(null);
                    }

                    getMessageManager().showInfoMessage(R.string.message_story_load_fail);
                }
            })
            .doOnNext(new Action1<DataCursor<StoryFrame>>() {
                @Override
                public void call(final DataCursor<StoryFrame> storyFrames) {
                    Contracts.requireMainThread();

                    final val presentableView = getPresentableView();
                    if (presentableView != null) {
                        presentableView.displayStoryFrames(storyFrames);
                    }
                }
            })
            .subscribe();
    }

    @Override
    protected void onBindPresentableView(
        @NonNull final StoryFramesEditorPresentableView presentableView) {
        super.onBindPresentableView(Contracts.requireNonNull(presentableView,
                                                             "presentableView == null"));

        presentableView.getOnStartEditStoryEvent().addHandler(getStartEditStoryHandler());
    }

    @Override
    protected void onUnbindPresentableView(
        @NonNull final StoryFramesEditorPresentableView presentableView) {
        super.onUnbindPresentableView(Contracts.requireNonNull(presentableView,
                                                               "presentableView == null"));

        presentableView.getOnStartEditStoryEvent().removeHandler(getStartEditStoryHandler());
    }

    @Override
    protected void onViewAppear(
        @NonNull final StoryFramesEditorPresentableView presentableView) {
        super.onViewAppear(Contracts.requireNonNull(presentableView, "presentableView == null"));

        presentableView.setStoryFramesVisible(false);
        presentableView.setLoadingVisible(true);

        final val storyContentObserver = getStoryContentObserver();
        storyContentObserver.getOnStoryChangedEvent().addHandler(getStoryExternalChangedHandler());
        storyContentObserver
            .getOnStoryFrameChangedEvent()
            .addHandler(getStoryFrameExternalChangedHandler());
    }

    @Override
    protected void onViewDisappear(
        @NonNull final StoryFramesEditorPresentableView presentableView) {
        super.onViewDisappear(Contracts.requireNonNull(presentableView, "presentableView == null"));

        final val storyContentObserver = getStoryContentObserver();
        storyContentObserver
            .getOnStoryChangedEvent()
            .removeHandler(getStoryExternalChangedHandler());
        storyContentObserver
            .getOnStoryFrameChangedEvent()
            .removeHandler(getStoryFrameExternalChangedHandler());
    }

    protected void onStartEditStory(final long storyId) {
        loadStory(storyId);
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
    private final EventHandler<StoryObserverEventArgs> _storyExternalChangedHandler =
        new EventHandler<StoryObserverEventArgs>() {
            @Override
            public void onEvent(@NonNull final StoryObserverEventArgs eventArgs) {
                final long editedStory = getEditedStoryId();
                final long changedStoryId = eventArgs.getId();

                // FIXME: 12/24/2016
                //                if (editedStory == changedStoryId) {
                //                    onStoryExternalChanged(changedStoryId);
                //                }
            }
        };

    @Getter(value = AccessLevel.PRIVATE, lazy = true)
    @NonNull
    private final EventHandler<StoryObserverEventArgs> _storyFrameExternalChangedHandler =
        new EventHandler<StoryObserverEventArgs>() {
            @Override
            public void onEvent(@NonNull final StoryObserverEventArgs eventArgs) {
                final long changedStoryFrameId = eventArgs.getId();

                // FIXME: 12/24/2016
                //                if (editedStory == changedStoryId) {
                //                    onStoryExternalChanged(changedStoryId);
                //                }
            }
        };

    @NonNull
    private AsyncCallback<DataCursor<StoryFrame>, Exception> getLoadStoryFramesCallback() {
        return new AsyncCallback<DataCursor<StoryFrame>, Exception>() {
            @Override
            public void onError(
                @Nullable final Exception error) {
                // FIXME: 12/24/2016
            }

            @Override
            public void onSuccess(@Nullable final DataCursor<StoryFrame> result) {
                final val presentableView = getPresentableView();
                if (presentableView != null) {
                    presentableView.displayStoryFrames(result);
                }
            }
        };
    }

    private void loadStoryFrames(final long storyId) {
        //        getStoryTaskManager().loadStoryFrames(storyId, getLoadStoryFramesCallback());
    }
}
