package com.christina.app.story.presentation;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import lombok.experimental.Accessors;
import lombok.val;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmObject;

import com.christina.app.story.R;
import com.christina.app.story.core.StoryEventArgs;
import com.christina.app.story.core.manager.ServiceManager;
import com.christina.app.story.data.model.Story;
import com.christina.app.story.view.StoryEditorScreen;
import com.christina.common.contract.Contracts;
import com.christina.common.event.generic.EventHandler;
import com.christina.common.event.notice.NoticeEventHandler;

@Accessors(prefix = "_")
public final class StoryEditorPresenter extends BaseStoryPresenter<StoryEditorScreen> {
    public StoryEditorPresenter(@NonNull final ServiceManager serviceManager) {
        super(Contracts.requireNonNull(serviceManager, "serviceManager == null"));
    }

    @CallSuper
    protected void editStory(@Nullable final Long storyId) {
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

            final val realm = getRealmManager().getRealm();

            final val story = realm.where(Story.class).equalTo(Story.ID, storyId).findFirst();

            RealmObject.addChangeListener(story, new RealmChangeListener<Story>() {
                @Override
                public void onChange(final Story element) {
                    final val screen = getScreen();
                    if (screen != null) {
                        if (RealmObject.isValid(story)) {
                            screen.displayStory(story.getId());
                        } else {
                            screen.displayStory(null);
                        }
                    }
                }
            });

            if (screen != null) {
                if (RealmObject.isValid(story)) {
                    screen.displayStory(story.getId());
                } else {
                    screen.displayStory(null);
                }
            }
        }
    }

    @CallSuper
    protected void insertStory() {
        final val screen = getScreen();
        if (screen != null) {
            screen.displayStoryLoading();
        }

        final val realmManager = getRealmManager();
        final val realm = realmManager.getRealm();

        final long storyId = realmManager.generateNextId(Story.class);

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(final Realm realm) {
                realm.createObject(Story.class, storyId);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                editStory(storyId);
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(final Throwable error) {
                getMessageManager().showInfoMessage(R.string.message_story_insert_fail);
            }
        });
    }

    @CallSuper
    @Override
    protected void onBindScreen(@NonNull final StoryEditorScreen screen) {
        super.onBindScreen(Contracts.requireNonNull(screen, "screen == null"));

        screen.getInsertStoryEvent().addHandler(_insertStoryHandler);
        screen.getEditStoryEvent().addHandler(_editStoryHandler);
    }

    @CallSuper
    @Override
    protected void onUnbindScreen(@NonNull final StoryEditorScreen screen) {
        super.onUnbindScreen(Contracts.requireNonNull(screen, "screen == null"));

        screen.getInsertStoryEvent().removeHandler(_insertStoryHandler);
        screen.getEditStoryEvent().removeHandler(_editStoryHandler);
    }

    @NonNull
    private final EventHandler<StoryEventArgs> _editStoryHandler =
        new EventHandler<StoryEventArgs>() {
            @Override
            public void onEvent(@NonNull final StoryEventArgs eventArgs) {
                Contracts.requireNonNull(eventArgs, "eventArgs == null");

                editStory(eventArgs.getStoryId());
            }
        };

    @NonNull
    private final NoticeEventHandler _insertStoryHandler = new NoticeEventHandler() {
        @Override
        public void onEvent() {
            insertStory();
        }
    };
}
