package com.christina.app.story.presentation;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import lombok.experimental.Accessors;
import lombok.val;

import io.realm.Realm;

import com.christina.app.story.R;
import com.christina.app.story.core.manager.StoryServiceManager;
import com.christina.app.story.core.manager.navigation.NavigationCallback;
import com.christina.app.story.core.manager.navigation.NavigationResult;
import com.christina.app.story.view.StoriesViewerScreen;
import com.christina.common.contract.Contracts;
import com.christina.common.event.notice.NoticeEventHandler;

@Accessors(prefix = "_")
public final class StoriesViewerPresenter extends BaseStoryPresenter<StoriesViewerScreen> {
    public StoriesViewerPresenter(@NonNull final StoryServiceManager storyServiceManager) {
        super(Contracts.requireNonNull(storyServiceManager, "storyServiceManager == null"));
    }

    @CallSuper
    @Override
    protected void onBindScreen(@NonNull final StoriesViewerScreen screen) {
        super.onBindScreen(Contracts.requireNonNull(screen, "screen == null"));

        screen.getRequestInsertStoryEvent().addHandler(_requestInsertStoryHandler);
        screen.getRemoveAllEvent().addHandler(_removeAllHandler);
    }

    @CallSuper
    @Override
    protected void onUnbindScreen(@NonNull final StoriesViewerScreen screen) {
        super.onUnbindScreen(Contracts.requireNonNull(screen, "screen == null"));

        screen.getRequestInsertStoryEvent().removeHandler(_requestInsertStoryHandler);
        screen.getRemoveAllEvent().removeHandler(_removeAllHandler);
    }

    @NonNull
    private final NoticeEventHandler _removeAllHandler = new NoticeEventHandler() {
        @Override
        public void onEvent() {
            getRealmManager().getRealm().executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(final Realm realm) {
                    final val deleteFilesTask =
                        getStoryFileManager().getDeleteAllAssociatedFilesTask();

                    realm.deleteAll();
                    deleteFilesTask.run();
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    getMessageManager().showInfoMessage(R.string.message_database_cleared);
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(final Throwable error) {
                    getMessageManager().showInfoMessage(R.string.message_database_clear_fail);
                }
            });
        }
    };

    @NonNull
    private final NoticeEventHandler _requestInsertStoryHandler = new NoticeEventHandler() {
        @Override
        public void onEvent() {
            getStoryNavigationManager().navigateToInsertStory(new NavigationCallback() {
                @Override
                public void onNavigationResult(@NonNull final NavigationResult result) {
                    Contracts.requireNonNull(result, "result == null");

                    if (result == NavigationResult.SUCCESS) {
                        getMessageManager().showInfoMessage(R.string.message_story_inserted);
                    } else {
                        getMessageManager().showInfoMessage(R.string.message_story_insert_fail);
                    }
                }
            });
        }
    };
}
