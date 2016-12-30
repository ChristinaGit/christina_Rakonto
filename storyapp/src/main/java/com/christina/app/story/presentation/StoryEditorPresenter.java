package com.christina.app.story.presentation;

import android.support.annotation.NonNull;

import com.christina.api.story.model.Story;
import com.christina.app.story.R;
import com.christina.app.story.core.StoryEventArgs;
import com.christina.app.story.core.manager.ServiceManager;
import com.christina.app.story.view.StoryEditorPresentableView;
import com.christina.common.contract.Contracts;
import com.christina.common.event.EventHandler;
import com.christina.common.event.NoticeEventHandler;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

@Accessors(prefix = "_")
public final class StoryEditorPresenter extends BaseStoryPresenter<StoryEditorPresentableView> {
    public StoryEditorPresenter(
        @NonNull final ServiceManager serviceManager) {
        super(Contracts.requireNonNull(serviceManager, "serviceManager == null"));
    }

    @Override
    protected void onBindPresentableView(
        @NonNull final StoryEditorPresentableView presentableView) {
        super.onBindPresentableView(Contracts.requireNonNull(presentableView,
                                                             "presentableView == null"));

        presentableView.getOnInsertStoryEvent().addHandler(getInsertStoryHandler());
        presentableView.getOnEditStoryEvent().addHandler(getEditStoryHandler());
    }

    @Override
    protected void onUnbindPresentableView(
        @NonNull final StoryEditorPresentableView presentableView) {
        super.onUnbindPresentableView(Contracts.requireNonNull(presentableView,
                                                               "presentableView == null"));

        presentableView.getOnInsertStoryEvent().removeHandler(getInsertStoryHandler());
        presentableView.getOnEditStoryEvent().removeHandler(getEditStoryHandler());
    }

    protected void onEditStory(final long storyId) {
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

                    getMessageManager().showInfoMessage(R.string.message_story_load_fail);

                    final val presentableView = getPresentableView();
                    if (presentableView != null) {
                        presentableView.displayStory(Story.NO_ID);
                    }
                }
            })
            .doOnNext(new Action1<Story>() {
                @Override
                public void call(final Story story) {
                    Contracts.requireMainThread();

                    final long id = story != null ? story.getId() : Story.NO_ID;

                    final val presentableView = getPresentableView();
                    if (presentableView != null) {
                        presentableView.displayStory(id);
                    }
                }
            })
            .subscribe();
    }

    protected void onInsertStory() {
        final val rxManager = getRxManager();
        rxManager
            .autoManage(Observable.just(new Story()))
            .observeOn(rxManager.getIOScheduler())
            .doOnNext(new Action1<Story>() {
                @Override
                public void call(final Story story) {
                    Contracts.requireWorkerThread();

                    getStoryDao().insert(story);
                }
            })
            .observeOn(rxManager.getComputationScheduler())
            .map(new Func1<Story, Long>() {
                @Override
                public Long call(final Story story) {
                    Contracts.requireWorkerThread();

                    return story != null ? story.getId() : Story.NO_ID;
                }
            })
            .observeOn(rxManager.getUIScheduler())
            .doOnError(new Action1<Throwable>() {
                @Override
                public void call(final Throwable error) {
                    Contracts.requireMainThread();

                    final val presentableView = getPresentableView();
                    if (presentableView != null) {
                        presentableView.displayStory(Story.NO_ID);
                    }

                    getMessageManager().showInfoMessage(R.string.message_story_insert_fail);
                }
            })
            .doOnNext(new Action1<Long>() {
                @Override
                public void call(final Long storyId) {
                    Contracts.requireMainThread();

                    final val presentableView = getPresentableView();
                    if (presentableView != null) {
                        presentableView.displayStory(storyId);
                    }
                }
            })
            .subscribe();
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
