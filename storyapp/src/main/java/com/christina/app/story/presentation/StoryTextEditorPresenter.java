package com.christina.app.story.presentation;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import com.christina.api.story.model.Story;
import com.christina.api.story.model.StoryFrame;
import com.christina.api.story.observer.StoryObserverEventArgs;
import com.christina.app.story.R;
import com.christina.app.story.core.StoryContentEventArgs;
import com.christina.app.story.core.StoryEventArgs;
import com.christina.app.story.core.StoryTextUtils;
import com.christina.app.story.core.manager.ServiceManager;
import com.christina.app.story.view.StoryTextEditorPresentableView;
import com.christina.common.contract.Contracts;
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
public final class StoryTextEditorPresenter
    extends BaseStoryPresenter<StoryTextEditorPresentableView> {
    public StoryTextEditorPresenter(
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
            presentableView.setStoryVisible(false);
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
                public void call(final Throwable throwable) {
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
            .doOnCompleted(new Action0() {
                @Override
                public void call() {
                    Contracts.requireMainThread();

                    final val presentableView = getPresentableView();
                    if (presentableView != null) {
                        presentableView.setLoadingVisible(false);
                        presentableView.setStoryVisible(true);
                    }
                }
            })
            .subscribe();
    }

    @CallSuper
    @Override
    protected void onBindPresentableView(
        @NonNull final StoryTextEditorPresentableView presentableView) {
        super.onBindPresentableView(Contracts.requireNonNull(presentableView,
                                                             "presentableView == null"));

        presentableView.getOnStartEditStoryEvent().addHandler(getStartEditStoryHandler());
        presentableView.getOnStoryChangedEvent().addHandler(getStoryChangedHandler());
    }

    @CallSuper
    @Override
    protected void onUnbindPresentableView(
        @NonNull final StoryTextEditorPresentableView presentableView) {
        super.onUnbindPresentableView(Contracts.requireNonNull(presentableView,
                                                               "presentableView == null"));

        presentableView.getOnStartEditStoryEvent().removeHandler(getStartEditStoryHandler());
        presentableView.getOnStoryChangedEvent().removeHandler(getStoryChangedHandler());
    }

    @CallSuper
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

    @CallSuper
    @Override
    protected void onViewDisappear(
        @NonNull final StoryTextEditorPresentableView presentableView) {
        super.onViewDisappear(Contracts.requireNonNull(presentableView, "presentableView == null"));

        getStoryContentObserver()
            .getOnStoryChangedEvent()
            .removeHandler(getStoryExternalChangedHandler());
    }

    @CallSuper
    protected void onStartEditStory(final long storyId) {
        loadStory(storyId);
    }

    @CallSuper
    protected void onStoryChanged(@NonNull final Story story) {
        Contracts.requireNonNull(story, "story == null");

        story.setModifyDate(System.currentTimeMillis());

        final val rxManager = getRxManager();
        rxManager
            .autoManage(Observable.just(story))
            .observeOn(rxManager.getIOScheduler())
            .map(new Func1<Story, Story>() {
                @Override
                public Story call(final Story story) {
                    Contracts.requireWorkerThread();

                    getStoryDaoManager().getStoryDao().update(story);

                    return story;
                }
            })
            .map(new Func1<Story, Story>() {
                @Override
                public Story call(final Story story) {
                    Contracts.requireWorkerThread();

                    // FIXME: 1/1/2017
                    //                    getStoryFrameDao().delete(StoryFrameSelections
                    // .byStoryId(story.getId()));

                    return story;
                }
            })
            .map(new Func1<Story, Story>() {
                @Override
                public Story call(final Story story) {
                    Contracts.requireWorkerThread();

                    final val storyText = story.getText();

                    if (storyText != null) {
                        final val storyDefaultSplit = StoryTextUtils.defaultSplit(storyText);

                        final val storyFrameDao = getStoryDaoManager().getStoryFrameDao();
                        int startPosition = 0;
                        int endPosition = 0;
                        for (final val textFrame : storyDefaultSplit) {
                            startPosition += endPosition;
                            endPosition += textFrame.length();

                            final val storyFrame = new StoryFrame();
                            storyFrame.setStoryId(story.getId());

                            storyFrame.setTextStartPosition(startPosition);
                            storyFrame.setTextEndPosition(endPosition);

                            storyFrameDao.insert(storyFrame);
                        }
                    }

                    return story;
                }
            })
            .subscribe();
    }

    @CallSuper
    protected void onStoryExternalChanged(final long storyId) {
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
    private final EventHandler<StoryContentEventArgs> _storyChangedHandler =
        new EventHandler<StoryContentEventArgs>() {
            @Override
            public void onEvent(@NonNull final StoryContentEventArgs eventArgs) {
                Contracts.requireNonNull(eventArgs, "eventArgs == null");

                final val story = eventArgs.getStory();

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
                Contracts.requireNonNull(eventArgs, "eventArgs == null");

                final long editedStory = getEditedStoryId();
                final long changedStoryId = eventArgs.getId();

                if (editedStory == changedStoryId) {
                    onStoryExternalChanged(changedStoryId);
                }
            }
        };
}