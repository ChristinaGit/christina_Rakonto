package com.christina.app.story.presentation;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import lombok.experimental.Accessors;
import lombok.val;

import rx.functions.Action1;

import io.realm.RealmChangeListener;
import io.realm.RealmObject;
import io.realm.RealmResults;

import com.christina.app.story.core.StoryEventArgs;
import com.christina.app.story.core.manager.ServiceManager;
import com.christina.app.story.data.model.Story;
import com.christina.app.story.data.model.StoryFrame;
import com.christina.app.story.view.StoryFramesEditorScreen;
import com.christina.common.contract.Contracts;
import com.christina.common.event.generic.EventHandler;

@Accessors(prefix = "_")
public final class StoryFramesEditorPresenter extends BaseStoryPresenter<StoryFramesEditorScreen> {
    public StoryFramesEditorPresenter(@NonNull final ServiceManager serviceManager) {
        super(Contracts.requireNonNull(serviceManager, "serviceManager == null"));
    }

    @CallSuper
    @Override
    protected void onBindScreen(@NonNull final StoryFramesEditorScreen screen) {
        super.onBindScreen(Contracts.requireNonNull(screen, "screen == null"));

        screen.getStartEditStoryEvent().addHandler(_startEditStoryHandler);
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
            final val screen = getScreen();
            if (screen != null) {
                screen.displayStory(null);
            }
        } else {
            final val screen = getScreen();
            if (screen != null) {
                screen.displayStoryLoading();
            }

            final val rxManager = getRxManager();
            final val realmManager = getRealmManager();
            final val realm = realmManager.getRealm();

            final val story = realm.where(Story.class).equalTo(Story.ID, storyId).findFirst();

            RealmObject.addChangeListener(story, new RealmChangeListener<Story>() {
                @Override
                public void onChange(final Story element) {
                    final val screen = getScreen();
                    if (screen != null) {
                        screen.displayStory(element);
                    }
                }
            });

            if (screen != null) {
                screen.displayStory(story);

                screen.displayStoryFramesLoading();
            }

            final val storyFrames = realm
                .where(StoryFrame.class)
                .equalTo(StoryFrame.STORY_ID, storyId)
                .findAllAsync()
                .asObservable();

            rxManager
                .autoManage(storyFrames)
                .observeOn(rxManager.getIOScheduler())
                .subscribeOn(rxManager.getUIScheduler())
                .subscribe(new Action1<RealmResults<StoryFrame>>() {
                    @Override
                    public void call(final RealmResults<StoryFrame> storyFrames) {
                        Contracts.requireMainThread();

                        final val screen = getScreen();
                        if (screen != null) {
                            screen.displayStoryFrames(storyFrames);
                        }
                    }
                });
        }
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
}
