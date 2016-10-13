package com.christina.app.story.fragment.storyTextPartsEditor;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.christina.api.story.model.Story;
import com.christina.api.story.model.StoryFrame;
import com.christina.api.story.observer.StoryContentObserver;
import com.christina.api.story.observer.StoryObserverEventArgs;
import com.christina.api.story.util.StoryPredicate;
import com.christina.app.story.R;
import com.christina.app.story.core.StoryTextUtils;
import com.christina.app.story.fragment.BaseStoryFragment;
import com.christina.app.story.fragment.storyTextPartsEditor.adapter.StoryTextPartsAdapter;
import com.christina.app.story.fragment.storyTextPartsEditor.loader.StoryLoader;
import com.christina.app.story.fragment.storyTextPartsEditor.loader.StoryLoaderResult;
import com.christina.common.FragmentUtils;
import com.christina.common.contract.Contracts;
import com.christina.common.event.EventHandler;

import org.apache.commons.collections4.IterableUtils;

import java.util.List;

public class StoryTextPartsEditorFragment extends BaseStoryFragment {
    public final static String ARG_STORY_ID = "story_id";

    protected static int _loaderIndexer = 0;

    private static final int LOADER_ID_STORY = _loaderIndexer++;

    public static void putStoryId(@NonNull final StoryTextPartsEditorFragment fragment,
        final long storyId) {
        Contracts.requireNonNull(fragment, "fragment == null");

        FragmentUtils.getArguments(fragment).putLong(ARG_STORY_ID, storyId);
    }

    public static long getStoryId(@NonNull final StoryTextPartsEditorFragment fragment) {
        Contracts.requireNonNull(fragment, "fragment == null");

        final long storyId;

        final Bundle arguments = fragment.getArguments();
        if (arguments != null) {
            storyId = arguments.getLong(ARG_STORY_ID, Story.NO_ID);
        } else {
            storyId = Story.NO_ID;
        }

        return storyId;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container,
        @Nullable final Bundle savedInstanceState) {
        final View view =
            inflater.inflate(R.layout.fragment_story_text_parts_editor, container, false);

        _storyTextPartsView = (RecyclerView) view.findViewById(R.id.raw_story_frames);
        onInitializeStoryTextPartsView(_storyTextPartsView);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final StoryContentObserver storyContentObserver = getStoryContentObserver();
        if (storyContentObserver != null) {
            storyContentObserver.onStoryChanged().addHandler(_storyChangedHandler);
            storyContentObserver.onStoryFrameChanged().addHandler(_storyFramesChangedHandler);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getStory() == null) {
            startStoryLoading();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        final StoryContentObserver storyContentObserver = getStoryContentObserver();
        if (storyContentObserver != null) {
            storyContentObserver.onStoryChanged().removeHandler(_storyChangedHandler);
            storyContentObserver.onStoryFrameChanged().removeHandler(_storyFramesChangedHandler);
        }
    }

    @Nullable
    protected final Story getStory() {
        return _story;
    }

    @Nullable
    protected final List<StoryFrame> getStoryFrames() {
        return _storyFrames;
    }

    @Nullable
    protected final List<String> getStoryTextParts() {
        final List<String> storyTextParts;

        String storyText = null;

        final Story story = getStory();
        if (story != null) {
            storyText = story.getText();
        }

        if (storyText != null) {
            storyText = StoryTextUtils.cleanup(storyText);

            storyTextParts = StoryTextUtils.defaultSplit(storyText);
        } else {
            storyTextParts = null;
        }

        return storyTextParts;
    }

    @NonNull
    protected final StoryTextPartsAdapter getStoryTextPartsAdapter() {
        if (_storyTextPartsAdapter == null) {
            _storyTextPartsAdapter = new StoryTextPartsAdapter();
        }
        return _storyTextPartsAdapter;
    }

    @NonNull
    protected final LinearLayoutManager getStoryTextPartsLayoutManager() {
        if (_storyTextPartsLayoutManager == null) {
            _storyTextPartsLayoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        }

        return _storyTextPartsLayoutManager;
    }

    protected final void startStoryLoading() {
        getLoaderManager().restartLoader(LOADER_ID_STORY, null, _storyLoaderCallbacks);
    }

    protected final void stopStoryLoading() {
        getLoaderManager().destroyLoader(LOADER_ID_STORY);
    }

    protected void onStoryLoaded() {
        final List<String> storyTextParts = getStoryTextParts();
        if (storyTextParts != null) {
            getStoryTextPartsAdapter().setItems(storyTextParts);
        } else {
            getStoryTextPartsAdapter().removeItems();
        }
    }

    protected void onStoryReset() {
        final StoryTextPartsAdapter storyTextPartsAdapter = getStoryTextPartsAdapter();
        storyTextPartsAdapter.removeItems(false);
        storyTextPartsAdapter.notifyDataSetChanged();
    }

    @Nullable
    private Story _story;

    @Nullable
    private List<StoryFrame> _storyFrames;

    @Nullable
    private StoryTextPartsAdapter _storyTextPartsAdapter;

    @NonNull
    private final LoaderManager.LoaderCallbacks<StoryLoaderResult> _storyLoaderCallbacks =
        new LoaderManager.LoaderCallbacks<StoryLoaderResult>() {
            @Override
            public Loader<StoryLoaderResult> onCreateLoader(final int id, final Bundle args) {
                if (id == LOADER_ID_STORY) {
                    return new StoryLoader(getActivity(),
                        getStoryId(StoryTextPartsEditorFragment.this));
                } else {
                    throw new IllegalArgumentException("Illegal loader id.");
                }
            }

            @Override
            public void onLoadFinished(final Loader<StoryLoaderResult> loader,
                final StoryLoaderResult data) {
                _story = data.getStory();
                _storyFrames = data.getStoryFrames();

                onStoryLoaded();
            }

            @Override
            public void onLoaderReset(final Loader<StoryLoaderResult> loader) {
                _story = null;
                _storyFrames = null;

                onStoryReset();
            }
        };

    @NonNull
    private final EventHandler<StoryObserverEventArgs> _storyChangedHandler =
        new EventHandler<StoryObserverEventArgs>() {
            @Override
            public void onEvent(@NonNull final StoryObserverEventArgs eventArgs) {
                Contracts.requireNonNull(eventArgs, "eventArgs == null");

                final Story story = getStory();
                if (story != null && story.getId() == eventArgs.getId()) {
                    startStoryLoading();
                }
            }
        };

    @NonNull
    private final EventHandler<StoryObserverEventArgs> _storyFramesChangedHandler =
        new EventHandler<StoryObserverEventArgs>() {
            @Override
            public void onEvent(@NonNull final StoryObserverEventArgs eventArgs) {
                Contracts.requireNonNull(eventArgs, "eventArgs == null");

                final long storyId = eventArgs.getId();
                if (IterableUtils.find(getStoryFrames(),
                    StoryPredicate.StoryFrame.storyIdEquals(storyId)) != null) {
                    startStoryLoading();
                }
            }
        };

    @Nullable
    private LinearLayoutManager _storyTextPartsLayoutManager;

    @Nullable
    private RecyclerView _storyTextPartsView;

    private void onInitializeStoryTextPartsView(@NonNull final RecyclerView rawStoryFramesView) {
        Contracts.requireNonNull(rawStoryFramesView, "rawStoryFramesView == null");

        rawStoryFramesView.setLayoutManager(getStoryTextPartsLayoutManager());
        rawStoryFramesView.setAdapter(getStoryTextPartsAdapter());
    }
}
