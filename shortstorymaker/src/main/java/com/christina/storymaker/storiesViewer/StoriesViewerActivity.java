package com.christina.storymaker.storiesViewer;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.christina.content.story.observer.StoryContentObserver;
import com.christina.storymaker.R;
import com.christina.storymaker.core.StoryContentObserverProvider;

public class StoriesViewerActivity extends AppCompatActivity
    implements StoryContentObserverProvider {
    @NonNull
    @Override
    public final StoryContentObserver getStoryContentObserver() {
        return _storyContentObserver;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @CallSuper
    protected void findViews() {
        _toolbarView = (Toolbar) findViewById(R.id.toolbar);
        _fabView = (FloatingActionButton) findViewById(R.id.fab);
        _contentContainerView = (ViewGroup) findViewById(R.id.content_container);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity);

        findViews();

        setSupportActionBar(_toolbarView);
        _fabView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show();
            }
        });

        getSupportFragmentManager().beginTransaction()
                                   .add(R.id.content_container, StoriesViewerFragment.create())
                                   .commit();
    }

    @NonNull
    private final StoryContentObserver _storyContentObserver = new StoryContentObserver();

    private ViewGroup _contentContainerView;

    private FloatingActionButton _fabView;

    private Toolbar _toolbarView;
}
