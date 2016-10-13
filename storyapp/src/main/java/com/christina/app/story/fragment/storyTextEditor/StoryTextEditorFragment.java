package com.christina.app.story.fragment.storyTextEditor;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.christina.api.story.dao.StoryDaoManager;
import com.christina.api.story.model.Story;
import com.christina.api.story.observer.StoryContentObserver;
import com.christina.api.story.observer.StoryObserverEventArgs;
import com.christina.app.story.R;
import com.christina.app.story.fragment.BaseStoryFragment;
import com.christina.app.story.fragment.storyTextEditor.loader.StoryLoader;
import com.christina.app.story.fragment.storyTextEditor.loader.StoryLoaderResult;
import com.christina.common.BaseTextWatcher;
import com.christina.common.FragmentUtils;
import com.christina.common.contract.Contracts;
import com.christina.common.event.EventHandler;

public final class StoryTextEditorFragment extends BaseStoryFragment {
    public final static String ARG_STORY_ID = "story_id";

    protected static int _loaderIndexer = 0;

    private static final int LOADER_ID_STORY = _loaderIndexer++;

    public static void putStoryId(@NonNull final StoryTextEditorFragment fragment,
        final long storyId) {
        Contracts.requireNonNull(fragment, "fragment == null");

        FragmentUtils.getArguments(fragment).putLong(ARG_STORY_ID, storyId);
    }

    public static long getStoryId(@NonNull final StoryTextEditorFragment fragment) {
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
        return inflater.inflate(R.layout.fragment_story_text_editor, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        _storyTextView = (EditText) view.findViewById(R.id.story_text);
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final StoryContentObserver storyContentObserver = getStoryContentObserver();
        if (storyContentObserver != null) {
            storyContentObserver.onStoryChanged().addHandler(_storyChangedHandler);
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
    public void onResume() {
        super.onResume();

        if (_storyTextView != null) {
            _storyTextView.addTextChangedListener(_storyTextWatcher);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (_storyTextView != null) {
            _storyTextView.removeTextChangedListener(_storyTextWatcher);
        }

        final Story story = getStory();
        if (story != null) {
            story.setModifyDate();
            StoryDaoManager.getStoryDao().update(story);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        final StoryContentObserver storyContentObserver = getStoryContentObserver();
        if (storyContentObserver != null) {
            storyContentObserver.onStoryChanged().removeHandler(_storyChangedHandler);
        }
    }

    @Nullable
    protected final Story getStory() {
        return _story;
    }

    protected final void startStoryLoading() {
        getLoaderManager().restartLoader(LOADER_ID_STORY, null, _storyLoaderCallbacks);
    }

    protected final void stopStoryLoading() {
        getLoaderManager().destroyLoader(LOADER_ID_STORY);
    }

    protected void onStoryLoaded() {
        final Story story = getStory();

        if (_storyTextView != null) {
            final String storyText;

            if (story != null) {
                storyText = story.getText();
                _storyTextView.setEnabled(true);
            } else {
                storyText = null;
                _storyTextView.setEnabled(false);
            }

            _storyTextView.setText(storyText);
        }
    }

    protected void onStoryReset() {
        if (_storyTextView != null) {
            _storyTextView.setText(null);
            _storyTextView.setEnabled(true);
        }
    }

    @Nullable
    private Story _story;

    @NonNull
    private final TextWatcher _storyTextWatcher = new BaseTextWatcher() {
        @Override
        public void afterTextChanged(final Editable s) {
            final String storyText;
            if (s != null) {
                storyText = s.toString();
            } else {
                storyText = null;
            }
            final Story story = getStory();
            if (story != null) {
                story.setText(storyText);
            }
        }
    };

    @Nullable
    private EditText _storyTextView;

    @NonNull
    private final LoaderManager.LoaderCallbacks<StoryLoaderResult> _storyLoaderCallbacks =
        new LoaderManager.LoaderCallbacks<StoryLoaderResult>() {
            @Override
            public Loader<StoryLoaderResult> onCreateLoader(final int id, final Bundle args) {
                if (id == LOADER_ID_STORY) {
                    return new StoryLoader(getActivity(), getStoryId(StoryTextEditorFragment.this));
                } else {
                    throw new IllegalArgumentException("Illegal loader id.");
                }
            }

            @Override
            public void onLoadFinished(final Loader<StoryLoaderResult> loader,
                final StoryLoaderResult data) {
                _story = data.getStory();

                onStoryLoaded();
            }

            @Override
            public void onLoaderReset(final Loader<StoryLoaderResult> loader) {
                _story = null;

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
}
