package com.christina.app.story.presentation;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import com.christina.api.story.dao.storyFrame.StoryFrameSelections;
import com.christina.api.story.model.Story;
import com.christina.api.story.model.StoryFrame;
import com.christina.api.story.observer.StoryObserverEventArgs;
import com.christina.app.story.R;
import com.christina.app.story.core.StoryEventArgs;
import com.christina.app.story.core.manager.ServiceManager;
import com.christina.app.story.view.StoryFramesEditorPresentableView;
import com.christina.common.contract.Contracts;
import com.christina.common.data.cursor.dataCursor.DataCursor;
import com.christina.common.event.EventHandler;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;
import rx.Observable;
import rx.functions.Action0;
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
        final val presentableView = getPresentableView();
        if (presentableView != null) {
            presentableView.setLoadingVisible(true);
            presentableView.setStoryFramesVisible(false);
        }

        final val rxManager = getRxManager();
        rxManager
            .autoManage(Observable.just(storyId))
            .observeOn(rxManager.getIOScheduler())
            .map(new Func1<Long, Story>() {
                @Override
                public Story call(final Long storyId) {
                    Contracts.requireWorkerThread();

                    return getStoryDaoManager().getStoryDao().get(storyId);
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

                    return getStoryDaoManager()
                        .getStoryFrameDao()
                        .getAll(StoryFrameSelections.byStoryId(story.getId()))
                        .asDataCursor();
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
            .doOnCompleted(new Action0() {
                @Override
                public void call() {
                    Contracts.requireMainThread();

                    final val presentableView = getPresentableView();
                    if (presentableView != null) {
                        presentableView.setLoadingVisible(false);
                        presentableView.setStoryFramesVisible(true);
                    }
                }
            })
            .subscribe();
    }

    @CallSuper
    @Override
    protected void onBindPresentableView(
        @NonNull final StoryFramesEditorPresentableView presentableView) {
        super.onBindPresentableView(Contracts.requireNonNull(presentableView,
                                                             "presentableView == null"));

        presentableView.getOnStartEditStoryEvent().addHandler(getStartEditStoryHandler());
    }

    @CallSuper
    @Override
    protected void onUnbindPresentableView(
        @NonNull final StoryFramesEditorPresentableView presentableView) {
        super.onUnbindPresentableView(Contracts.requireNonNull(presentableView,
                                                               "presentableView == null"));

        presentableView.getOnStartEditStoryEvent().removeHandler(getStartEditStoryHandler());
    }

    @CallSuper
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

    @CallSuper
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

    @CallSuper
    protected void onStartEditStory(final long storyId) {
        loadStory(storyId);
    }

    @CallSuper
    protected void onStoryExternalChanged(final long changedStoryId) {
        loadStory(changedStoryId);
    }

    @CallSuper
    protected void onStoryFrameExternalChanged(final long editedStoryId) {
        loadStory(editedStoryId);
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

                if (editedStory == changedStoryId) {
                    onStoryExternalChanged(changedStoryId);
                }
            }
        };

    @Getter(value = AccessLevel.PRIVATE, lazy = true)
    @NonNull
    private final EventHandler<StoryObserverEventArgs> _storyFrameExternalChangedHandler =
        new EventHandler<StoryObserverEventArgs>() {
            @Override
            public void onEvent(@NonNull final StoryObserverEventArgs eventArgs) {
                final long changedStoryFrameId = eventArgs.getId();

                final val rxManager = getRxManager();
                rxManager
                    .autoManage(Observable.just(changedStoryFrameId))
                    .observeOn(rxManager.getIOScheduler())
                    .map(new Func1<Long, StoryFrame>() {
                        @Override
                        public StoryFrame call(final Long storyFrameId) {
                            Contracts.requireWorkerThread();

                            return getStoryDaoManager().getStoryFrameDao().get(storyFrameId);
                        }
                    })
                    .observeOn(rxManager.getUIScheduler())
                    .doOnNext(new Action1<StoryFrame>() {
                        @Override
                        public void call(final StoryFrame storyFrame) {
                            Contracts.requireMainThread();

                            final long editedStoryId = getEditedStoryId();
                            if (editedStoryId == storyFrame.getStoryId()) {
                                onStoryFrameExternalChanged(editedStoryId);
                            }
                        }
                    })
                    .subscribe();
            }
        };
}
