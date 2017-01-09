package com.christina.app.story.view.activity.storiesViewer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.christina.api.story.observer.StoryContentObserver;
import com.christina.app.story.R;
import com.christina.app.story.di.qualifier.PresenterNames;
import com.christina.app.story.view.StoriesViewerPresentableView;
import com.christina.app.story.view.activity.BaseStoryActivity;
import com.christina.app.story.view.fragment.storiesList.StoriesListFragment;
import com.christina.common.contract.Contracts;
import com.christina.common.event.BaseNoticeEvent;
import com.christina.common.event.NoticeEvent;
import com.christina.common.view.FabScrollAutoHideBehavior;
import com.christina.common.view.presentation.Presenter;
import com.christina.content.story.StoryDatabase;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.OnClick;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;

@Accessors(prefix = "_")
public final class StoriesViewerActivity extends BaseStoryActivity
    implements StoriesViewerPresentableView {
    protected static int resultCodeIndexer = 100;

    public static final int RESULT_UNSUPPORTED_ACTION = resultCodeIndexer++;

    @NonNull
    @Override
    public final NoticeEvent getOnInsertStoryEvent() {
        return _onInsertStoryEvent;
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
            // TODO: 11/28/2016 remove.
            case R.id.action_settings: {
                deleteDatabase(StoryDatabase.NAME);
                Process.killProcess(Process.myPid());

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

    @CallSuper
    @Override
    protected void onBindPresenter() {
        super.onBindPresenter();

        final val presenter = getPresenter();
        if (presenter != null) {
            presenter.setPresentableView(this);
        }
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

            setActionBar(_toolbarView);

            final val actionBar = getActionBar();
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

    @CallSuper
    @Override
    protected void onUnbindPresenter() {
        super.onUnbindPresenter();

        final val presenter = getPresenter();
        if (presenter != null) {
            presenter.setPresentableView(null);
        }
    }

    @OnClick(R.id.fab)
    protected void onFabClick() {
        _onInsertStoryEvent.rise();
    }

    @CallSuper
    protected boolean onHandleIntent(@NonNull final Intent intent) {
        Contracts.requireNonNull(intent, "intent == null");

        final boolean intentHandled;

        final val action = getIntent().getAction();
        switch (action) {
            case Intent.ACTION_MAIN:
            case Intent.ACTION_VIEW: {
                _mode = Mode.VIEW;
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
    protected void onInject() {
        super.onInject();

        getStoryViewComponent().inject(this);
    }

    @CallSuper
    @Override
    protected void onReleaseInject() {
        super.onReleaseInject();

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
    /*package-private*/ Presenter<StoriesViewerPresentableView> _presenter;

    @Inject
    @Getter(AccessLevel.PROTECTED)
    @Nullable
    /*package-private*/ StoryContentObserver _storyContentObserver;

    @Nullable
    @BindView(R.id.toolbar)
    /*package-private*/ Toolbar _toolbarView;

    @NonNull
    private final BaseNoticeEvent _onInsertStoryEvent = new BaseNoticeEvent();

    @Nullable
    @Getter(AccessLevel.PROTECTED)
    private Mode _mode;

    protected enum Mode {
        VIEW
    }
}
