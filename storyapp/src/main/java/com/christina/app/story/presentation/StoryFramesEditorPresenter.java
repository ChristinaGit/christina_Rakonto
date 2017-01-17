package com.christina.app.story.presentation;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import lombok.experimental.Accessors;
import lombok.val;

import io.realm.RealmChangeListener;
import io.realm.RealmObject;

import com.christina.app.story.core.RealmChangesObserver;
import com.christina.app.story.core.StoryEventArgs;
import com.christina.app.story.core.manager.StoryServiceManager;
import com.christina.app.story.data.model.Story;
import com.christina.app.story.view.StoryFramesEditorScreen;
import com.christina.common.contract.Contracts;
import com.christina.common.event.generic.EventHandler;

@Accessors(prefix = "_")
public final class StoryFramesEditorPresenter extends BaseStoryPresenter<StoryFramesEditorScreen> {
    public StoryFramesEditorPresenter(@NonNull final StoryServiceManager storyServiceManager) {
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

            final val realmManager = getRealmManager();
            final val realm = realmManager.getRealm();

            final val story = realm.where(Story.class).equalTo(Story.ID, storyId).findFirst();

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
}
