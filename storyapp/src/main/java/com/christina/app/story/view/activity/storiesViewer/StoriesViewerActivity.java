package com.christina.app.story.view.activity.storiesViewer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;

import butterknife.BindView;
import butterknife.OnClick;

import com.christina.app.story.R;
import com.christina.app.story.di.qualifier.PresenterNames;
import com.christina.app.story.view.StoriesViewerScreen;
import com.christina.app.story.view.activity.BaseStoryActivity;
import com.christina.app.story.view.fragment.storiesList.StoriesListFragment;
import com.christina.common.contract.Contracts;
import com.christina.common.event.Events;
import com.christina.common.event.notice.ManagedNoticeEvent;
import com.christina.common.event.notice.NoticeEvent;
import com.christina.common.presentation.Presenter;
import com.christina.common.view.FabScrollAutoHideBehavior;

import javax.inject.Inject;
import javax.inject.Named;

@Accessors(prefix = "_")
public final class StoriesViewerActivity extends BaseStoryActivity implements StoriesViewerScreen {
    protected static int resultCodeIndexer = 100;

    public static final int RESULT_UNSUPPORTED_ACTION = resultCodeIndexer++;

    @NonNull
    @Override
    public final NoticeEvent getRequestInsertStoryEvent() {
        return _requestInsertStoryEvent;
    }

    @NonNull
    @Override
    public final NoticeEvent getRemoveAllEvent() {
        return _removeAllEvent;
    }

    @CallSuper
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.stories_viewer_menu, menu);
        return true;
    }

    @CallSuper
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final boolean handled;

        switch (item.getItemId()) {
            case R.id.action_remove_all: {
                _removeAllEvent.rise();

                handled = true;
                break;
            }
            case android.R.id.home: {
                NavUtils.navigateUpFromSameTask(this);

                handled = true;
                break;
            }
            default: {
                handled = super.onOptionsItemSelected(item);
                break;
            }
        }

        return handled;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final boolean initialized = onHandleIntent(getIntent());

        if (initialized) {
            setContentView(R.layout.activity_stories_viewer);

            bindViews();

            if (_fabView != null) {
                final val fabLayoutParams =
                    (CoordinatorLayout.LayoutParams) _fabView.getLayoutParams();
                fabLayoutParams.setBehavior(new FabScrollAutoHideBehavior());
                _fabView.setLayoutParams(fabLayoutParams);
            }

            setSupportActionBar(_toolbarView);

            final val actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayShowHomeEnabled(false);
                actionBar.setDisplayShowTitleEnabled(true);
                actionBar.setDisplayHomeAsUpEnabled(false);
            }

            final val fragmentManager = getSupportFragmentManager();
            if (fragmentManager.findFragmentById(R.id.content_container) == null) {
                fragmentManager
                    .beginTransaction()
                    .add(R.id.content_container, new StoriesListFragment())
                    .commit();
            }
        } else {
            finish();
        }
    }

    @OnClick(R.id.fab)
    protected void onFabClick() {
        _requestInsertStoryEvent.rise();
    }

    @CallSuper
    protected boolean onHandleIntent(@NonNull final Intent intent) {
        Contracts.requireNonNull(intent, "intent == null");

        final boolean intentHandled;

        final val action = getIntent().getAction();
        switch (action) {
            case Intent.ACTION_MAIN:
            case Intent.ACTION_VIEW: {
                _mode = StoriesViewerMode.VIEW;
                intentHandled = true;
                break;
            }
            default: {
                setResult(RESULT_UNSUPPORTED_ACTION);
                intentHandled = false;
                break;
            }
        }

        return intentHandled;
    }

    @CallSuper
    @Override
    protected void onInjectMembers() {
        super.onInjectMembers();

        getStoryScreenComponent().inject(this);

        final val presenter = getPresenter();
        if (presenter != null) {
            presenter.bindScreen(this);
        }
    }

    @CallSuper
    @Override
    protected void onReleaseInjectedMembers() {
        super.onReleaseInjectedMembers();

        final val presenter = getPresenter();
        if (presenter != null) {
            presenter.unbindScreen();
        }

        unbindViews();
    }

    @Nullable
    @BindView(R.id.content_container)
    /*package-private*/ ViewGroup _contentContainerView;

    @Nullable
    @BindView(R.id.fab)
    /*package-private*/ FloatingActionButton _fabView;

    @Named(PresenterNames.STORIES_VIEWER)
    @Inject
    @Getter(AccessLevel.PROTECTED)
    @Nullable
    /*package-private*/ Presenter<StoriesViewerScreen> _presenter;

    @Nullable
    @BindView(R.id.toolbar)
    /*package-private*/ Toolbar _toolbarView;

    @NonNull
    private final ManagedNoticeEvent _removeAllEvent = Events.createNoticeEvent();

    @NonNull
    private final ManagedNoticeEvent _requestInsertStoryEvent = Events.createNoticeEvent();

    @Nullable
    @Getter(AccessLevel.PROTECTED)
    private StoriesViewerMode _mode;
}
