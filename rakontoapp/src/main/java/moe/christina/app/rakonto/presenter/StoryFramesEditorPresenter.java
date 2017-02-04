package moe.christina.app.rakonto.presenter;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import lombok.experimental.Accessors;
import lombok.val;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmObject;

import moe.christina.app.rakonto.core.eventArgs.StoryEventArgs;
import moe.christina.app.rakonto.core.manager.StoryServiceManager;
import moe.christina.app.rakonto.core.manager.search.StoryFrameImage;
import moe.christina.app.rakonto.model.Story;
import moe.christina.app.rakonto.model.StoryFrame;
import moe.christina.app.rakonto.screen.StoryFramesEditorScreen;
import moe.christina.common.ConstantBuilder;
import moe.christina.common.contract.Contracts;
import moe.christina.common.data.realm.RealmChangesObserver;
import moe.christina.common.event.generic.EventHandler;
import moe.christina.common.utility.tuple.Tuple;
import moe.christina.common.utility.tuple.Tuple2;

import java.util.List;

@Accessors(prefix = "_")
public final class StoryFramesEditorPresenter extends BaseStoryPresenter<StoryFramesEditorScreen> {
    private static final String _LOG_TAG = ConstantBuilder.logTag(StoryFramesEditorPresenter.class);

    public StoryFramesEditorPresenter(@NonNull final StoryServiceManager storyServiceManager) {
        super(Contracts.requireNonNull(storyServiceManager, "storyServiceManager == null"));
    }

    protected final void displayStoreFrameImageCandidates(
        final long storyFrameId, @Nullable final List<StoryFrameImage> candidatesUris) {
        final val screen = getScreen();
        if (screen != null) {
            screen.displayStoreFrameImageCandidates(storyFrameId, candidatesUris);
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
                    .filter(new Func1<StoryFrame, Boolean>() {
                        @Override
                        public Boolean call(final StoryFrame storyFrame) {
                            Contracts.requireMainThread();

                            return RealmObject.isValid(storyFrame);
                        }
                    })
                    .map(new Func1<StoryFrame, Tuple2<StoryFrame, String>>() {
                        @Override
                        public Tuple2<StoryFrame, String> call(final StoryFrame storyFrame) {
                            Contracts.requireMainThread();

                            final val storyFrameText =
                                storyText.substring(storyFrame.getTextStartPosition(),
                                                    storyFrame.getTextEndPosition());
                            return Tuple.from(storyFrame, storyFrameText);
                        }
                    })
                    .observeOn(rxManager.getIOScheduler())
                    .map(new Func1<Tuple2<StoryFrame, String>, Tuple2<StoryFrame,
                        List<StoryFrameImage>>>() {
                        @Override
                        public Tuple2<StoryFrame, List<StoryFrameImage>> call(final
                                                                              Tuple2<StoryFrame,
                                                                                  String> arg) {
                            Contracts.requireWorkerThread();

                            final val storyFrame = arg.getFirst();
                            final val storyFrameText = arg.getSecond();

                            Contracts.requireNonNull(storyFrame, "storyFrame == null");

                            List<StoryFrameImage> candidates = null;

                            if (storyFrameText != null) {
                                try {
                                    candidates =
                                        getStorySearchManager().searchFrameImages(storyFrameText);
                                } catch (final Exception e) {
                                    // TODO: 1/26/2017 Add error dialog.
                                    Log.w(_LOG_TAG, "Error during web search.", e);

                                    candidates = null;
                                }
                            }

                            return Tuple.from(storyFrame, candidates);
                        }
                    })
                    .observeOn(rxManager.getUIScheduler())
                    .filter(new Func1<Tuple2<StoryFrame, List<StoryFrameImage>>, Boolean>() {
                        @Override
                        public Boolean call(final Tuple2<StoryFrame, List<StoryFrameImage>> arg) {
                            Contracts.requireMainThread();

                            final val storyFrame = arg.getFirst();

                            return RealmObject.isValid(storyFrame);
                        }
                    })
                    .doOnNext(new Action1<Tuple2<StoryFrame, List<StoryFrameImage>>>() {
                        @Override
                        public void call(final Tuple2<StoryFrame, List<StoryFrameImage>> arg) {
                            Contracts.requireMainThread();

                            final val storyFrame = arg.getFirst();
                            final val candidates = arg.getSecond();

                            Contracts.requireNonNull(storyFrame, "storyFrame == null");

                            final val preferredCandidate =
                                candidates == null || candidates.isEmpty()
                                ? null
                                : candidates.get(0);
                            final long storyFrameId = storyFrame.getId();

                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(final Realm realm) {
                                    final val storyFrame = realm
                                        .where(StoryFrame.class)
                                        .equalTo(StoryFrame.ID, storyFrameId)
                                        .findFirst();

                                    if (RealmObject.isValid(storyFrame)) {
                                        if (preferredCandidate != null) {
                                            storyFrame.setImageUri(preferredCandidate.getUri());
                                        }
                                    }
                                }
                            });
                        }
                    })
                    .observeOn(rxManager.getUIScheduler())
                    .subscribe(new Action1<Tuple2<StoryFrame, List<StoryFrameImage>>>() {
                        @Override
                        public void call(final Tuple2<StoryFrame, List<StoryFrameImage>> arg) {
                            Contracts.requireMainThread();

                            final val storyFrame = arg.getFirst();
                            final val storeFrameCandidates = arg.getSecond();

                            if (storyFrame != null) {
                                displayStoreFrameImageCandidates(storyFrame.getId(),
                                                                 storeFrameCandidates);
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
