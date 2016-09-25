package com.christina.storymaker.storiesViewer;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.christina.content.story.observer.StoryContentObserver;
import com.christina.storymaker.R;
import com.christina.storymaker.core.StoryContentObserverProvider;

public class StoriesViewerActivity extends Activity implements StoryContentObserverProvider {
    @NonNull
    @Override
    public final StoryContentObserver getStoryContentObserver() {
        return _storyContentObserver;
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

        setContentView(R.layout.activity);

        findViews();

        setActionBar(_toolbarView);
        if (_fabView != null) {
            _fabView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                            .show();
                }
            });
        }

        getFragmentManager().beginTransaction()
                            .add(R.id.content_container, StoriesViewerFragment.create())
                            .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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
