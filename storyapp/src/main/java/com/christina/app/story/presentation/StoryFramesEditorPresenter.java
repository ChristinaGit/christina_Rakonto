package com.christina.app.story.presentation;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import lombok.experimental.Accessors;
import lombok.val;

import rx.Observable;
import rx.exceptions.Exceptions;
import rx.functions.Action1;
import rx.functions.Func1;

import io.realm.RealmChangeListener;
import io.realm.RealmObject;

import com.christina.app.story.core.StoryEventArgs;
import com.christina.app.story.core.manager.StoryServiceManager;
import com.christina.app.story.model.Story;
import com.christina.app.story.model.StoryFrame;
import com.christina.app.story.view.StoryFramesEditorScreen;
import com.christina.common.contract.Contracts;
import com.christina.common.data.realm.RealmChangesObserver;
import com.christina.common.event.generic.EventHandler;
import com.christina.common.utility.tuple.Tuple;
import com.christina.common.utility.tuple.Tuple2;

import java.util.List;

@Accessors(prefix = "_")
public final class StoryFramesEditorPresenter extends BaseStoryPresenter<StoryFramesEditorScreen> {
    public StoryFramesEditorPresenter(@NonNull final StoryServiceManager storyServiceManager) {
        super(Contracts.requireNonNull(storyServiceManager, "storyServiceManager == null"));
    }

    protected final void displayStoreFrameCandidates(
        final long storyFrameId, @Nullable final List<String> candidatesUris) {
        Contracts.requireNonNull(candidatesUris, "candidatesUris == null");

        final val screen = getScreen();
        if (screen != null) {
            screen.displayStoreFrameCandidates(storyFrameId, candidatesUris);
        }
    }

    protected final void displayStory(@Nullable final Story story) {
        final val screen = getScreen();
        if (screen != null) {
            if (RealmObject.isValid(story)) {
                screen.displayStory(story);
            } else {
                screen.displayStory(null);
            }
        }
    }

    protected final void displayStoryLoading() {
        final val screen = getScreen();
        if (screen != null) {
            screen.displayStoryLoading();
        }
    }

    @CallSuper
    @Override
    protected void onBindScreen(@NonNull final StoryFramesEditorScreen screen) {
        super.onBindScreen(Contracts.requireNonNull(screen, "screen == null"));

        screen.getStartEditStoryEvent().addHandler(_startEditStoryHandler);
    }

    @Override
    protected void onScreenDisappear(@NonNull final StoryFramesEditorScreen screen) {
        super.onScreenDisappear(Contracts.requireNonNull(screen, "screen == null"));

        _displayedStoryObserver.release();
    }

    @CallSuper
    @Override
    protected void onUnbindScreen(@NonNull final StoryFramesEditorScreen screen) {
        super.onUnbindScreen(Contracts.requireNonNull(screen, "screen == null"));

        screen.getStartEditStoryEvent().removeHandler(_startEditStoryHandler);
    }

    @CallSuper
    protected void startEditStory(@Nullable final Long storyId) {
        if (storyId == null) {
            displayStory(null);
        } else {
            displayStoryLoading();

            final val rxManager = getRxManager();
            final val realmManager = getRealmManager();
            final val realm = realmManager.getRealm();

            final val story = realm.where(Story.class).equalTo(Story.ID, storyId).findFirst();

            _displayedStoryObserver.enable(story);

            displayStory(story);

            final val storyText = story.getText();
            if (storyText != null) {
                rxManager
                    .autoManage(Observable.from(story.getStoryFrames()))
                    .subscribeOn(rxManager.getUIScheduler())
                    .observeOn(rxManager.getUIScheduler())
                    .map(new Func1<StoryFrame, Tuple2<Long, String>>() {
                        @Override
                        public Tuple2<Long, String> call(final StoryFrame storyFrame) {
                            Contracts.requireMainThread();

                            final val storyFrameText =
                                storyText.substring(storyFrame.getTextStartPosition(),
                                                    storyFrame.getTextEndPosition());
                            return Tuple.from(storyFrame.getId(), storyFrameText);
                        }
                    })
                    .observeOn(rxManager.getIOScheduler())
                    .map(new Func1<Tuple2<Long, String>, Tuple2<Long, List<String>>>() {
                        @Override
                        public Tuple2<Long, List<String>> call(final Tuple2<Long, String> arg) {
                            Contracts.requireWorkerThread();

                            List<String> storyFrameCandidates = null;

                            if (arg.second != null) {
                                try {
                                    storyFrameCandidates =
                                        getStorySearchManager().search(arg.second);
                                } catch (Exception e) {
                                    throw Exceptions.propagate(e);
                                }
                            }

                            return Tuple.from(arg.first, storyFrameCandidates);
                        }
                    })
                    .observeOn(rxManager.getUIScheduler())
                    .subscribe(new Action1<Tuple2<Long, List<String>>>() {
                        @Override
                        public void call(final Tuple2<Long, List<String>> arg) {
                            Contracts.requireMainThread();

                            if (arg.first != null) {
                                displayStoreFrameCandidates(arg.first, arg.second);
                            }
                        }
                    });
            }
        }
    }

    @NonNull
    private final RealmChangesObserver<Story> _displayedStoryObserver =
        new RealmChangesObserver<>(new RealmChangeListener<Story>() {
            @Override
            public void onChange(final Story element) {
                displayStory(element);
            }
        });

    @NonNull
    private final EventHandler<StoryEventArgs> _startEditStoryHandler =
        new EventHandler<StoryEventArgs>() {
            @Override
            public void onEvent(@NonNull final StoryEventArgs eventArgs) {
                Contracts.requireNonNull(eventArgs, "eventArgs == null");

                startEditStory(eventArgs.getStoryId());
            }
        };
}
