package com.christina.storymaker;

import android.app.ActionBar;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.christina.content.story.database.StoryDatabase;
import com.christina.content.story.observer.StoryContentObserver;
import com.christina.storymaker.core.StoryContentObserverProvider;
import com.christina.storymaker.storiesViewer.StoriesViewerFragment;

public class StoriesActivity extends AppCompatActivity
    implements StoryContentObserverProvider, View.OnClickListener {
    protected static int rcIndexer = 0;

    public static final int RC_CREATE_NEW_STORY = rcIndexer++;

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
            }
        }

        return handled;
    }

    @CallSuper
    protected void findViews() {
        _toolbarView = (Toolbar) findViewById(R.id.toolbar);
        _fabView = (FloatingActionButton) findViewById(R.id.fab);
        _contentContainerView = (ViewGroup) findViewById(R.id.content_container);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.stories);

        findViews();

        setActionBar(_toolbarView);

        final ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        if (_fabView != null) {
            _fabView.setOnClickListener(this);
        }

        final FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentById(R.id.content_container) == null) {
            fragmentManager
                .beginTransaction()
                .add(R.id.content_container, StoriesViewerFragment.create())
                .commit();
        }
    }

    protected void onFabClick() {
        NewStoryActivity.startForResult(this, RC_CREATE_NEW_STORY);
    }

    @NonNull
    private final StoryContentObserver _storyContentObserver = new StoryContentObserver();

    @Nullable
    private ViewGroup _contentContainerView;

    @Nullable
    private FloatingActionButton _fabView;

    @Nullable
    private Toolbar _toolbarView;
}
