package com.christina.app.story.presentation;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import lombok.experimental.Accessors;
import lombok.val;

import rx.functions.Action1;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;

import com.christina.app.story.R;
import com.christina.app.story.core.StoryEventArgs;
import com.christina.app.story.core.manager.StoryServiceManager;
import com.christina.app.story.model.Story;
import com.christina.app.story.model.ui.UIStory;
import com.christina.app.story.view.StoriesListScreen;
import com.christina.common.contract.Contracts;
import com.christina.common.event.generic.EventHandler;
import com.christina.common.event.notice.NoticeEventHandler;

import java.util.List;

@Accessors(prefix = "_")
public final class StoriesListPresenter extends BaseStoryPresenter<StoriesListScreen> {
    public StoriesListPresenter(@NonNull final StoryServiceManager storyServiceManager) {
        super(Contracts.requireNonNull(storyServiceManager, "storyServiceManager == null"));
    }

    protected final void displayStories() {
        displayStoriesLoading();

        final val rxManager = getRxManager();
        final val realm = getRealmManager().getRealm();

        final val requestResult =
            realm.where(Story.class).findAllSortedAsync(Story.ID, Sort.ASCENDING).asObservable();
        rxManager
            .autoManage(requestResult)
            .observeOn(rxManager.getUIScheduler())
            .subscribeOn(rxManager.getUIScheduler())
            .subscribe(new Action1<RealmResults<Story>>() {
                @Override
                public void call(final RealmResults<Story> stories) {
                    Contracts.requireMainThread();

                    displayStories(stories);
                }
            });
    }

    protected final void displayStories(@Nullable final OrderedRealmCollection<Story> stories) {
        final val screen = getScreen();
        if (screen != null) {
            if (stories == null || stories.isValid()) {
                screen.displayStories((List<UIStory>) (List<? extends UIStory>) stories);
            } else {
                screen.displayStories(null);
            }
        }
    }

    protected final void displayStoriesLoading() {
        final val screen = getScreen();
        if (screen != null) {
            screen.displayStoriesLoading();
        }
    }

    @CallSuper
    protected void deleteStory(@Nullable final Long storyId) {
        final val realm = getRealmManager().getRealm();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(final Realm realm) {
                final val story = realm.where(Story.class).equalTo(Story.ID, storyId).findFirst();
                final val deleteFilesTask =
                    getStoryFileManager().getDeleteAssociatedFilesTask(story, true);

                RealmObject.deleteFromRealm(story);
                deleteFilesTask.run();
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                getMessageManager().showInfoMessage(R.string.message_story_deleted);
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(final Throwable error) {
                getMessageManager().showInfoMessage(R.string.message_story_delete_fail);
            }
        });
    }

    @CallSuper
    protected void editStory(final long storyId) {
        getStoryNavigationManager().navigateToEditStory(storyId);
    }

    @CallSuper
    @Override
    protected void onBindScreen(@NonNull final StoriesListScreen screen) {
        super.onBindScreen(Contracts.requireNonNull(screen, "screen == null"));

        screen.getViewStoriesEvent().addHandler(_viewStoriesHandler);
        screen.getDeleteStoryEvent().addHandler(_deleteStoryHandler);
        screen.getEditStoryEvent().addHandler(_editStoryHandler);
    }

    @CallSuper
    @Override
    protected void onUnbindScreen(@NonNull final StoriesListScreen screen) {
        super.onUnbindScreen(Contracts.requireNonNull(screen, "screen == null"));

        screen.getViewStoriesEvent().removeHandler(_viewStoriesHandler);
        screen.getDeleteStoryEvent().removeHandler(_deleteStoryHandler);
        screen.getEditStoryEvent().removeHandler(_editStoryHandler);
    }

    @NonNull
    private final EventHandler<StoryEventArgs> _deleteStoryHandler =
        new EventHandler<StoryEventArgs>() {
            @Override
            public void onEvent(@NonNull final StoryEventArgs eventArgs) {
                Contracts.requireNonNull(eventArgs, "eventArgs == null");

                deleteStory(eventArgs.getStoryId());
            }
        };

    @NonNull
    private final EventHandler<StoryEventArgs> _editStoryHandler =
        new EventHandler<StoryEventArgs>() {
            @Override
            public void onEvent(@NonNull final StoryEventArgs eventArgs) {
                Contracts.requireNonNull(eventArgs, "eventArgs == null");

                final Long storyId = eventArgs.getStoryId();
                if (storyId != null) {
                    editStory(storyId);
                }
            }
        };

    @NonNull
    private final NoticeEventHandler _viewStoriesHandler = new NoticeEventHandler() {
        @Override
        public void onEvent() {
            displayStories();
        }
    };
}
