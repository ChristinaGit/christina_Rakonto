package com.christina.app.story.presentation;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import lombok.experimental.Accessors;
import lombok.val;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmObject;

import com.christina.app.story.core.RealmChangesObserver;
import com.christina.app.story.core.StoryChangedEventArgs;
import com.christina.app.story.core.StoryEventArgs;
import com.christina.app.story.core.StoryTextUtils;
import com.christina.app.story.core.manager.StoryServiceManager;
import com.christina.app.story.data.model.Story;
import com.christina.app.story.data.model.StoryFrame;
import com.christina.app.story.view.StoryTextEditorScreen;
import com.christina.common.contract.Contracts;
import com.christina.common.event.generic.EventHandler;

import java.util.ArrayList;

@Accessors(prefix = "_")
public final class StoryTextEditorPresenter extends BaseStoryPresenter<StoryTextEditorScreen> {
    public StoryTextEditorPresenter(@NonNull final StoryServiceManager storyServiceManager) {
        super(Contracts.requireNonNull(storyServiceManager, "storyServiceManager == null"));
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
            displayStoryLoading();

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

        getRealmManager().getRealm().executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(final Realm realm) {
                final val realmManager = getRealmManager();
                final val fileManager = getStoryFileManager();

                final val story =
                    realm.where(Story.class).equalTo(Story.ID, eventArgs.getStoryId()).findFirst();

                if (story != null) {
                    story.setText(eventArgs.getStoryText());

                    final val storyFrames = story.getStoryFrames();

                    final val deleteFilesTasks = new ArrayList<Runnable>(storyFrames.size());
                    for (final val storyFrame : storyFrames) {
                        final val task = fileManager.getDeleteAssociatedFilesTask(storyFrame, true);
                        deleteFilesTasks.add(task);
                    }
                    storyFrames.deleteAllFromRealm();
                    for (final val deleteFilesTask : deleteFilesTasks) {
                        deleteFilesTask.run();
                    }

                    final val storyText = story.getText();

                    if (storyText != null) {
                        final val storyDefaultSplit = StoryTextUtils.defaultSplit(storyText);

                        int startPosition = 0;
                        int endPosition = 0;
                        for (final val textFrame : storyDefaultSplit) {
                            startPosition += endPosition;
                            endPosition += textFrame.length();

                            final val storyFrame = new StoryFrame();

                            storyFrame.setId(realmManager.generateNextId(StoryFrame.class));
                            storyFrame.setTextStartPosition(startPosition);
                            storyFrame.setTextEndPosition(endPosition);

                            storyFrames.add(storyFrame);
                        }
                    }
                }
            }
        });
    }
}