package com.christina.app.story.operation.viewStories;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.christina.api.story.observer.StoryContentObserver;
import com.christina.app.story.R;
import com.christina.app.story.core.StoryContentObserverProvider;
import com.christina.app.story.fragment.storiesViewer.StoriesViewerFragment;
import com.christina.app.story.operation.editStory.EditStoryActivity;
import com.christina.common.contract.Contracts;
import com.christina.content.story.StoryDatabase;

public class ViewStoriesActivity extends AppCompatActivity
    implements StoryContentObserverProvider, View.OnClickListener {
    protected static int requestCodeIndexer = 0;

    protected static final int REQUEST_CODE_INSERT_STORY = requestCodeIndexer++;

    protected static int resultCodeIndexer = 100;

    public static final int RESULT_UNSUPPORTED_ACTION = resultCodeIndexer++;

    @NonNull
    @Override
    public final StoryContentObserver getStoryContentObserver() {
        return _storyContentObserver;
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.fab: {
                onFabClick();
                break;
            }
            default: {
                throw new IllegalArgumentException("Click at unknown view.");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.stories_viewer_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final boolean handled;

        switch (item.getItemId()) {
            case R.id.action_settings: {
                deleteDatabase(StoryDatabase.NAME);
                Process.killProcess(Process.myPid());
                handled = true;
                break;
            }
            case android.R.id.home: {
                finish();
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

    @Nullable
    protected final Mode getMode() {
        return _mode;
    }

    @CallSuper
    protected void findViews() {
        _toolbarView = (Toolbar) findViewById(R.id.toolbar);
        _fabView = (FloatingActionButton) findViewById(R.id.fab);
        _contentContainerView = (ViewGroup) findViewById(R.id.content_container);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode,
        final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_INSERT_STORY) {
            onInsertStoryResult(resultCode, data);
        }
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _mode = onHandleIntent(getIntent());

        if (getMode() != null) {

            setContentView(R.layout.activity_view_stories);

            findViews();

            setActionBar(_toolbarView);

            final ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.setDisplayShowHomeEnabled(false);
                actionBar.setDisplayShowTitleEnabled(true);
                actionBar.setDisplayHomeAsUpEnabled(false);
            }

            if (_fabView != null) {
                _fabView.setOnClickListener(/*Listener*/ this);
            }

            final FragmentManager fragmentManager = getSupportFragmentManager();
            if (fragmentManager.findFragmentById(R.id.content_container) == null) {
                fragmentManager
                    .beginTransaction()
                    .add(R.id.content_container, new StoriesViewerFragment())
                    .commit();
            }
        } else {
            finish();
        }
    }

    protected void onFabClick() {
        EditStoryActivity.startInsertForResult(/*Activity*/ this, REQUEST_CODE_INSERT_STORY);
    }

    @Nullable
    protected Mode onHandleIntent(@NonNull final Intent intent) {
        Contracts.requireNonNull(intent, "intent == null");

        final Mode mode;

        final String action = getIntent().getAction();
        switch (action) {
            case Intent.ACTION_MAIN:
            case Intent.ACTION_VIEW: {
                mode = onHandleViewIntent(intent);
                break;
            }
            default: {
                setResult(RESULT_UNSUPPORTED_ACTION);
                mode = null;
                break;
            }
        }

        return mode;
    }

    protected void onInsertStoryResult(final int resultCode, @Nullable final Intent data) {
        if (resultCode == RESULT_OK) {
            if (_contentContainerView != null) {
                Snackbar
                    .make(_contentContainerView, R.string.message_story_inserted,
                        Snackbar.LENGTH_SHORT)
                    .show();
            }
        }
    }

    @NonNull
    private final StoryContentObserver _storyContentObserver = new StoryContentObserver();

    @Nullable
    private ViewGroup _contentContainerView;

    @Nullable
    private FloatingActionButton _fabView;

    @Nullable
    private Mode _mode;

    @Nullable
    private Toolbar _toolbarView;

    private Mode onHandleViewIntent(@NonNull final Intent intent) {
        Contracts.requireNonNull(intent, "intent == null");

        return Mode.VIEW;
    }

    private enum Mode {
        VIEW
    }
}
