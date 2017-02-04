package moe.christina.app.rakonto.presenter;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import lombok.experimental.Accessors;
import lombok.val;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmObject;

import moe.christina.app.rakonto.core.eventArgs.StoryChangedEventArgs;
import moe.christina.app.rakonto.core.eventArgs.StoryEventArgs;
import moe.christina.app.rakonto.core.manager.StoryServiceManager;
import moe.christina.app.rakonto.core.utility.StoryTextUtils;
import moe.christina.app.rakonto.model.Story;
import moe.christina.app.rakonto.model.StoryFrame;
import moe.christina.app.rakonto.screen.StoryTextEditorScreen;
import moe.christina.common.contract.Contracts;
import moe.christina.common.data.realm.RealmChangesObserver;
import moe.christina.common.event.generic.EventHandler;

import java.util.ArrayList;
import java.util.Locale;

@Accessors(prefix = "_")
public final class StoryTextEditorPresenter extends BaseStoryPresenter<StoryTextEditorScreen> {
    public StoryTextEditorPresenter(@NonNull final StoryServiceManager storyServiceManager) {
        super(Contracts.requireNonNull(storyServiceManager, "storyServiceManager == null"));
    }

    protected final void displaySaveStoryChangedComplete() {
        final val screen = getScreen();
        if (screen != null) {
            screen.displaySaveStoryChangedComplete();
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
    protected void onBindScreen(@NonNull final StoryTextEditorScreen screen) {
        super.onBindScreen(Contracts.requireNonNull(screen, "screen == null"));

        screen.getStartEditStoryEvent().addHandler(_startEditStoryHandler);
        screen.getStoryChangedEvent().addHandler(_storyChangedHandler);
    }

    @Override
    protected void onScreenDisappear(@NonNull final StoryTextEditorScreen screen) {
        super.onScreenDisappear(screen);

        _displayedStoryObserver.release();
    }

    @CallSuper
    @Override
    protected void onUnbindScreen(@NonNull final StoryTextEditorScreen screen) {
        super.onUnbindScreen(Contracts.requireNonNull(screen, "screen == null"));

        screen.getStartEditStoryEvent().removeHandler(_startEditStoryHandler);
        screen.getStoryChangedEvent().removeHandler(_storyChangedHandler);
    }

    @CallSuper
    protected void startEditStory(@Nullable final Long storyId) {
        if (storyId == null) {
            displayStory(null);
        } else {
            final val story = getRealmManager()
                .getRealm()
                .where(Story.class)
                .equalTo(Story.ID, storyId)
                .findFirst();

            _displayedStoryObserver.enable(story);

            displayStory(story);
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

    @NonNull
    private final EventHandler<StoryChangedEventArgs> _storyChangedHandler =
        new EventHandler<StoryChangedEventArgs>() {
            @Override
            public void onEvent(@NonNull final StoryChangedEventArgs eventArgs) {
                Contracts.requireNonNull(eventArgs, "eventArgs == null");

                saveStoryChanged(eventArgs);
            }
        };

    private void saveStoryChanged(@NonNull final StoryChangedEventArgs eventArgs) {
        Contracts.requireNonNull(eventArgs, "eventArgs == null");

        final Long storyId = eventArgs.getStoryId();
        final String storyText = eventArgs.getStoryText();

        getRealmManager().getRealm().executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(final Realm realm) {
                final val story = realm.where(Story.class).equalTo(Story.ID, storyId).findFirst();

                if (story != null) {
                    deleteStoryFrames(story);

                    story.setText(storyText);

                    generateStoryFrames(story);
                }
            }

            private void deleteStoryFrames(@NonNull final Story story) {
                Contracts.requireNonNull(story, "story == null");

                final val fileManager = getStoryFileManager();

                final val storyFrames = story.getStoryFrames();

                final val deleteFilesTasks = new ArrayList<Runnable>(storyFrames.size());
                for (final val storyFrame : storyFrames) {
                    final val task = fileManager.getDeleteAssociatedFilesTask(storyFrame, true);
                    deleteFilesTasks.add(task);
                }
                storyFrames.deleteAllFromRealm();
                final val taskManager = getTaskManager();
                for (final val deleteFilesTask : deleteFilesTasks) {
                    taskManager.executeAsync(deleteFilesTask);
                }
            }

            private void generateStoryFrames(@NonNull final Story story) {
                Contracts.requireNonNull(story, "story == null");

                final val realmManager = getRealmManager();
                final val storyFrames = story.getStoryFrames();
                final val storyText = story.getText();

                if (storyText != null) {
                    // TODO: 2/4/2017 Locale!
                    final val frameBoundaries =
                        StoryTextUtils.getStoryFramesBoundaries(storyText, Locale.getDefault());

                    for (final val frameBoundary : frameBoundaries) {
                        final val storyFrame = new StoryFrame();

                        storyFrame.setId(realmManager.generateNextId(StoryFrame.class));
                        storyFrame.setTextStartPosition(frameBoundary.getStart());
                        storyFrame.setTextEndPosition(frameBoundary.getEnd());

                        storyFrames.add(storyFrame);
                    }
                }
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                displaySaveStoryChangedComplete();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(final Throwable error) {
                // TODO: 2/4/2017 Handle error
            }
        });
    }
}