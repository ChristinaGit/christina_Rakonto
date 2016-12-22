package com.christina.app.story.view.fragment.storyFramesEditor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.christina.app.story.R;
import com.christina.app.story.adpter.storyFrames.StoryFramesAdapter;
import com.christina.app.story.di.qualifier.PresenterNames;
import com.christina.app.story.view.StoryFramesEditorPresentableView;
import com.christina.app.story.view.fragment.BaseStoryFragment;
import com.christina.common.view.presentation.Presenter;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;

@Accessors(prefix = "_")
public final class StoryFramesEditorFragment extends BaseStoryFragment {
    @Nullable
    @Override
    public View onCreateView(
        final LayoutInflater inflater,
        @Nullable final ViewGroup container,
        @Nullable final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        final val view = inflater.inflate(R.layout.fragment_story_frames_editor, container, false);

        bindViews(view);

        onInitializeStoryFramesView();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        unbindViews();
    }

    @Override
    protected void onUnbindPresenter() {
        super.onUnbindPresenter();

        final val presenter = getPresenter();
        if (presenter != null) {
            presenter.setPresentableView(null);
        }
    }

    protected void onInitializeStoryFramesView() {
        if (_storyFramesView != null) {
            _storyFramesView.setLayoutManager(getStoryFramesLayoutManager());
            _storyFramesView.setAdapter(getStoryFramesAdapter());
        }
    }

    @Override
    protected void onInject() {
        super.onInject();

        getStoryViewFragmentComponent().inject(this);
    }

    @Named(PresenterNames.STORY_FRAMES_EDITOR)
    @Inject
    @Getter(AccessLevel.PROTECTED)
    @Nullable
    /*package-private*/ Presenter<StoryFramesEditorPresentableView> _presenter;

    @BindView(R.id.story_frames)
    @Nullable
    /*package-private*/ RecyclerView _storyFramesView;

    @Getter(value = AccessLevel.PROTECTED, lazy = true)
    @Nullable
    private final StoryFramesAdapter _storyFramesAdapter = new StoryFramesAdapter();

    @Getter(value = AccessLevel.PROTECTED, lazy = true)
    @Nullable
    private final LinearLayoutManager _storyFramesLayoutManager =
        new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
}
