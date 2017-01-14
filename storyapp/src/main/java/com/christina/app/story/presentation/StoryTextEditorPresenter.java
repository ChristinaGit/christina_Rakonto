package com.christina.app.story.presentation;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import lombok.experimental.Accessors;
import lombok.val;

import com.christina.app.story.core.StoryContentEventArgs;
import com.christina.app.story.core.StoryEventArgs;
import com.christina.app.story.core.manager.ServiceManager;
import com.christina.app.story.data.model.Story;
import com.christina.app.story.view.StoryTextEditorScreen;
import com.christina.common.contract.Contracts;
import com.christina.common.event.generic.EventHandler;

@Accessors(prefix = "_")
public final class StoryTextEditorPresenter extends BaseStoryPresenter<StoryTextEditorScreen> {
    public StoryTextEditorPresenter(@NonNull final ServiceManager serviceManager) {
        super(Contracts.requireNonNull(serviceManager, "serviceManager == null"));
    }

    protected final void loadStory(final long storyId) {
        //        final val presentableView = getScreen();
        //        if (presentableView != null) {
        //            presentableView.setLoadingVisible(true);
        //            presentableView.setStoryVisible(false);
        //        }
        //
        //        final val rxManager = getRxManager();
        //        rxManager
        //            .autoManage(Observable.just(storyId))
        //            .observeOn(rxManager.getIOScheduler())
        //            .map(new Func1<Long, Story>() {
        //                @Override
        //                public Story call(final Long storyId) {
        //                    Contracts.requireWorkerThread();
        //
        //                    return getStoryDaoManager().getStoryDao().get(storyId);
        //                }
        //            })
        //            .observeOn(rxManager.getUIScheduler())
        //            .doOnError(new Action1<Throwable>() {
        //                @Override
        //                public void call(final Throwable throwable) {
        //                    Contracts.requireMainThread();
        //
        //                    final val presentableView = getScreen();
        //                    if (presentableView != null) {
        //                        presentableView.displayStory(null);
        //                    }
        //
        //                    getMessageManager().showInfoMessage(R.string.message_story_load_fail);
        //                }
        //            })
        //            .doOnNext(new Action1<Story>() {
        //                @Override
        //                public void call(final Story story) {
        //                    Contracts.requireMainThread();
        //
        //                    final val presentableView = getScreen();
        //                    if (presentableView != null) {
        //                        presentableView.displayStory(story);
        //                    }
        //                }
        //            })
        //            .doOnCompleted(new Action0() {
        //                @Override
        //                public void call() {
        //                    Contracts.requireMainThread();
        //
        //                    final val presentableView = getScreen();
        //                    if (presentableView != null) {
        //                        presentableView.setLoadingVisible(false);
        //                        presentableView.setStoryVisible(true);
        //                    }
        //                }
        //            })
        //            .subscribe();
    }

    @CallSuper
    @Override
    protected void onBindScreen(@NonNull final StoryTextEditorScreen screen) {
        super.onBindScreen(Contracts.requireNonNull(screen, "screen == null"));

        screen.getStartEditStoryEvent().addHandler(_startEditStoryHandler);
        screen.getStoryChangedEvent().addHandler(_storyChangedHandler);
    }

    @CallSuper
    @Override
    protected void onUnbindScreen(@NonNull final StoryTextEditorScreen screen) {
        super.onUnbindScreen(Contracts.requireNonNull(screen, "screen == null"));

        screen.getStartEditStoryEvent().removeHandler(_startEditStoryHandler);
        screen.getStoryChangedEvent().removeHandler(_storyChangedHandler);
    }

    @CallSuper
    protected void startEditStory(final long storyId) {
        loadStory(storyId);
    }

    @CallSuper
    protected void onStoryChanged(@NonNull final Story story) {
        Contracts.requireNonNull(story, "story == null");

        //        story.setModifyDate(System.currentTimeMillis());
        //
        //        final val rxManager = getRxManager();
        //        rxManager
        //            .autoManage(Observable.just(story))
        //            .observeOn(rxManager.getIOScheduler())
        //            .map(new Func1<Story, Story>() {
        //                @Override
        //                public Story call(final Story story) {
        //                    Contracts.requireWorkerThread();
        //
        //                    getStoryDaoManager().getStoryDao().update(story);
        //
        //                    return story;
        //                }
        //            })
        //            .map(new Func1<Story, Story>() {
        //                @Override
        //                public Story call(final Story story) {
        //                    Contracts.requireWorkerThread();
        //
        //                    final int deleted = getStoryDaoManager()
        //                        .getStoryFrameDao()
        //                        .delete(StoryFrameSelections.byStoryId(story.getId()));
        //
        //                    return story;
        //                }
        //            })
        //            .map(new Func1<Story, Story>() {
        //                @Override
        //                public Story call(final Story story) {
        //                    Contracts.requireWorkerThread();
        //
        //                    final val storyText = story.getText();
        //
        //                    if (storyText != null) {
        //                        final val storyDefaultSplit = StoryTextUtils.defaultSplit
        // (storyText);
        //
        //                        final val storyFrameDao = getStoryDaoManager().getStoryFrameDao();
        //                        int startPosition = 0;
        //                        int endPosition = 0;
        //                        for (final val textFrame : storyDefaultSplit) {
        //                            startPosition += endPosition;
        //                            endPosition += textFrame.length();
        //
        //                            final val storyFrame = new StoryFrame();
        //                            storyFrame.setStoryId(story.getId());
        //
        //                            storyFrame.setTextStartPosition(startPosition);
        //                            storyFrame.setTextEndPosition(endPosition);
        //
        //                            storyFrameDao.insert(storyFrame);
        //                        }
        //                    }
        //
        //                    return story;
        //                }
        //            })
        //            .subscribe();
    }

    @NonNull
    private final EventHandler<StoryEventArgs> _startEditStoryHandler =
        new EventHandler<StoryEventArgs>() {
            @Override
            public void onEvent(@NonNull final StoryEventArgs eventArgs) {
                Contracts.requireNonNull(eventArgs, "eventArgs == null");

                startEditStory(eventArgs.getStoryId());
            }
        };

    @NonNull
    private final EventHandler<StoryContentEventArgs> _storyChangedHandler =
        new EventHandler<StoryContentEventArgs>() {
            @Override
            public void onEvent(@NonNull final StoryContentEventArgs eventArgs) {
                Contracts.requireNonNull(eventArgs, "eventArgs == null");

                final val story = eventArgs.getStory();

                // FIXME: 1/11/2017
                if (story != null) {
                    //                    onStoryChanged(new Story(story));
                }
            }
        };
}