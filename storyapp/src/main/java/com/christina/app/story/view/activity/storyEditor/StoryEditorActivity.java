package com.christina.app.story.view.activity.storyEditor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.val;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnPageChange;

import com.christina.app.story.R;
import com.christina.app.story.core.StoryEventArgs;
import com.christina.app.story.core.adpter.storyEditorPages.StoryEditorPageChangedEventArgs;
import com.christina.app.story.core.adpter.storyEditorPages.StoryEditorPagesAdapter;
import com.christina.app.story.model.contract.StoryContentCode;
import com.christina.app.story.model.contract.StoryContract;
import com.christina.app.story.model.ui.UIStory;
import com.christina.app.story.di.qualifier.PresenterNames;
import com.christina.app.story.view.StoryEditorScreen;
import com.christina.app.story.view.activity.BaseStoryActivity;
import com.christina.common.ConstantBuilder;
import com.christina.common.contract.Contracts;
import com.christina.common.event.Events;
import com.christina.common.event.generic.Event;
import com.christina.common.event.generic.EventHandler;
import com.christina.common.event.generic.ManagedEvent;
import com.christina.common.event.notice.ManagedNoticeEvent;
import com.christina.common.event.notice.NoticeEvent;
import com.christina.common.mvp.presenter.Presenter;
import com.christina.common.utility.AnimationViewUtils;

import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

@Accessors(prefix = "_")
public final class StoryEditorActivity extends BaseStoryActivity implements StoryEditorScreen {
    private static final String _KEY_SAVED_STATE =
        ConstantBuilder.savedStateKey(StoryEditorActivity.class, "saved_state");

    protected static int resultCodeIndexer = 100;

    public static final int RESULT_INSERT_STORY_FAILED = resultCodeIndexer++;

    public static final int RESULT_INSERT_STORY_FRAMES_FAILED = resultCodeIndexer++;

    public static final int RESULT_NOT_FOUND = resultCodeIndexer++;

    public static final int RESULT_NO_DATA = resultCodeIndexer++;

    public static final int RESULT_UNSUPPORTED_ACTION = resultCodeIndexer++;

    @NonNull
    public static Intent getInsertIntent(@NonNull final Context context) {
        Contracts.requireNonNull(context, "context == null");

        final val intent = new Intent(context, StoryEditorActivity.class);
        intent.setAction(Intent.ACTION_INSERT);
        intent.setData(StoryContract.getStoriesUri());
        return intent;
    }

    public static void startInsert(@NonNull final Context context) {
        Contracts.requireNonNull(context, "context == null");

        context.startActivity(getInsertIntent(context));
    }

    public static void startInsertForResult(
        @NonNull final Activity activity, final int requestCode) {
        Contracts.requireNonNull(activity, "activity == null");

        activity.startActivityForResult(getInsertIntent(activity), requestCode);
    }

    @NonNull
    public static Intent getEditIntent(@NonNull final Context context, final long storyId) {
        Contracts.requireNonNull(context, "context == null");

        final val intent = new Intent(context, StoryEditorActivity.class);
        intent.setAction(Intent.ACTION_EDIT);
        intent.setData(StoryContract.getStoryUri(String.valueOf(storyId)));
        return intent;
    }

    public static void startEdit(@NonNull final Context context, final long storyId) {
        Contracts.requireNonNull(context, "context == null");

        context.startActivity(getEditIntent(context, storyId));
    }

    public static void startEditForResult(
        @NonNull final Activity activity, final long storyId, final int requestCode) {
        Contracts.requireNonNull(activity, "activity == null");

        activity.startActivityForResult(getEditIntent(activity, storyId), requestCode);
    }

    @Override
    public final void displayStory(@Nullable final UIStory story) {
        setStory(story);
        notifyStoryChanged();

        if (story != null) {
            final val mode = getMode();
            if (mode == StoryEditorMode.INSERT) {
                final val resultData = new Intent();
                resultData.setData(StoryContract.getStoryUri(String.valueOf(story.getId())));
                setResult(RESULT_OK, resultData);
            } else if (mode == StoryEditorMode.EDIT) {
                final val resultData = new Intent();
                resultData.setData(getIntent().getData());
                setResult(RESULT_OK, resultData);
            }
        } else {
            final val mode = getMode();
            if (mode == StoryEditorMode.INSERT) {
                setResult(RESULT_INSERT_STORY_FAILED);
                finish();
            } else if (mode == StoryEditorMode.EDIT) {
                setResult(RESULT_NOT_FOUND);
                finish();
            }
        }
    }

    @Override
    public void displayStoryLoading() {
        // No loading state.
    }

    @NonNull
    @Override
    public final Event<StoryEventArgs> getEditStoryEvent() {
        return _editStoryEvent;
    }

    @NonNull
    @Override
    public final NoticeEvent getInsertStoryEvent() {
        return _insertStoryEvent;
    }

    protected final void nextStep() {
        if (_stepPagerView != null) {
            final int currentStep = _stepPagerView.getCurrentItem();
            final int nextStep = currentStep + 1;
            if (nextStep < getStoryEditorPagesAdapter().getCount()) {
                onLeaveStep(currentStep);
                onEnterStep(nextStep);

                _stepPagerView.setCurrentItem(nextStep, true);
            }
        }
    }

    protected final void notifyStoryChanged() {
        onStoryChanged();
    }

    protected final void previousStep() {
        if (_stepPagerView != null) {
            final int currentStep = _stepPagerView.getCurrentItem();
            final int previousStep = currentStep - 1;
            if (previousStep >= 0) {
                onLeaveStep(currentStep);
                onEnterStep(previousStep);

                _stepPagerView.setCurrentItem(previousStep, true);
            }
        }
    }

    protected final void setStoryId(@Nullable final Long storyId) {
        if (!Objects.equals(_storyId, storyId)) {
            _storyId = storyId;

            onSoryIdChanged();
        }
    }

    @CallSuper
    protected boolean isNextStepAvailable() {
        boolean isNextStepAvailable = false;

        if (_stepPagerView != null) {
            final int position = _stepPagerView.getCurrentItem();
            final boolean lastStep = getStoryEditorPagesAdapter().getCount() - 1 == position;
            isNextStepAvailable = !lastStep;

            if (isNextStepAvailable) {
                final val editorFragment = getStoryEditorPagesAdapter().getEditorPage(position);
                if (editorFragment != null) {
                    isNextStepAvailable = editorFragment.hasContent();
                }
            }
        }

        return isNextStepAvailable;
    }

    @CallSuper
    protected boolean isPreviousStepAvailable() {
        boolean isPreviousStepAvailable = false;

        if (_stepPagerView != null) {
            final int position = _stepPagerView.getCurrentItem();
            final boolean firstStep = position == 0;
            isPreviousStepAvailable = !firstStep;
        }

        return isPreviousStepAvailable;
    }

    @CallSuper
    protected void onEnterStep(final int position) {
        final val editorFragment = getStoryEditorPagesAdapter().getEditorPage(position);
        if (editorFragment != null) {
            editorFragment.notifyStartEditing();
        }
    }

    @Nullable
    @CallSuper
    protected StoryEditorState onHandleEditIntent(@NonNull final Intent intent) {
        Contracts.requireNonNull(intent, "intent == null");

        final StoryEditorState state;

        final val data = intent.getData();
        if (data != null) {
            final int code = StoryContentCode.Matcher.get(data);
            if (code == StoryContract.CODE_STORY) {
                Long storyId;
                try {
                    storyId = Long.parseLong(StoryContract.extractStoryId(data));
                } catch (final NumberFormatException e) {
                    storyId = null;
                }
                if (storyId != null) {
                    state = new StoryEditorState();
                    state.setMode(StoryEditorMode.EDIT);
                    state.setStoryId(storyId);
                } else {
                    setResult(RESULT_NO_DATA);
                    state = null;
                }
            } else {
                setResult(RESULT_NO_DATA);
                state = null;
            }
        } else {
            setResult(RESULT_NO_DATA);
            state = null;
        }

        return state;
    }

    @Nullable
    @CallSuper
    protected StoryEditorState onHandleInsertIntent(@NonNull final Intent intent) {
        Contracts.requireNonNull(intent, "intent == null");

        final val state = new StoryEditorState();

        state.setMode(StoryEditorMode.INSERT);

        return state;
    }

    @Nullable
    @CallSuper
    protected StoryEditorState onHandleIntent(@NonNull final Intent intent) {
        Contracts.requireNonNull(intent, "intent == null");

        final StoryEditorState state;

        final val action = getIntent().getAction();
        switch (action) {
            case Intent.ACTION_INSERT: {
                state = onHandleInsertIntent(intent);
                break;
            }
            case Intent.ACTION_EDIT: {
                state = onHandleEditIntent(intent);
                break;
            }
            default: {
                setResult(RESULT_UNSUPPORTED_ACTION);
                state = null;
                break;
            }
        }

        return state;
    }

    @Nullable
    @CallSuper
    protected StoryEditorState onHandleSavedState(@NonNull final Bundle savedInstanceState) {
        StoryEditorState state = null;

        if (savedInstanceState.containsKey(_KEY_SAVED_STATE)) {
            state = savedInstanceState.getParcelable(_KEY_SAVED_STATE);
        }

        return state;
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
    protected void onLeaveStep(final int position) {
        final val editorFragment = getStoryEditorPagesAdapter().getEditorPage(position);
        if (editorFragment != null) {
            editorFragment.notifyStopEditing();
        }
    }

    @CallSuper
    @Override
    protected void onPause() {
        super.onPause();

        getStoryEditorPagesAdapter()
            .getContentChangedEvent()
            .removeHandler(_editFragmentContentChangedHandler);
    }

    @CallSuper
    @Override
    protected void onResume() {
        super.onResume();

        getStoryEditorPagesAdapter()
            .getContentChangedEvent()
            .addHandler(_editFragmentContentChangedHandler);

        if (_stepPagerView != null) {
            _stepPagerView.post(new Runnable() {
                @Override
                public void run() {
                    onEnterStep(_stepPagerView.getCurrentItem());
                }
            });
        }

        invalidateNavigationButtons();
    }

    @CallSuper
    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            _state = onHandleSavedState(savedInstanceState);
        }

        if (_state == null) {
            _state = onHandleIntent(getIntent());
        }

        if (_state != null) {
            _mode = _state.getMode();
            _storyId = _state.getStoryId();

            setContentView(R.layout.activity_story_editor);

            bindViews();

            setSupportActionBar(_toolbarView);

            final val actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowTitleEnabled(false);
            }

            if (_stepPagerView != null) {
                final val screensAdapter = getStoryEditorPagesAdapter();
                screensAdapter.setPageFactory(getStoryEditorPages());
                _stepPagerView.setAdapter(screensAdapter);

                _stepPagerView.setCurrentItem(_state.getActivePage(), false);
            }

            final val mode = getMode();
            if (mode != null) {
                switch (mode) {
                    case INSERT: {
                        _insertStoryEvent.rise();
                        break;
                    }
                    case EDIT: {
                        _editStoryEvent.rise(new StoryEventArgs(getStoryId()));
                        break;
                    }
                    default: {
                        throw new IllegalStateException("Unknown activity mode: " + mode);
                    }
                }
            } else {
                throw new IllegalStateException("Activity mode is null after the handled intent.");
            }
        } else {
            finish();
        }
    }

    @CallSuper
    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);

        if (outState != null) {
            if (_state == null) {
                _state = new StoryEditorState();
            }

            _state.setMode(getMode());
            _state.setStoryId(getStoryId());
            if (_stepPagerView != null) {
                _state.setActivePage(_stepPagerView.getCurrentItem());
            } else {
                _state.setActivePage(0);
            }
            outState.putParcelable(_KEY_SAVED_STATE, _state);
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

    @CallSuper
    protected void onSoryIdChanged() {
        getStoryEditorPagesAdapter().setStoryId(getStoryId());
    }

    @OnPageChange(R.id.creation_step_pager)
    protected void onStepChanged(final int position) {
        invalidateNavigationButtons();
    }

    @CallSuper
    protected void onStoryChanged() {
        final val story = getStory();

        final Long storyId = story != null ? story.getId() : null;

        getStoryEditorPagesAdapter().setStoryId(storyId);
    }

    @BindView(R.id.content_container)
    @Nullable
    /*package-private*/ ViewGroup _contentContainerView;

    @BindView(R.id.navigation_bar)
    @Nullable
    /*package-private*/ View _navigationBarView;

    @BindView(R.id.next_step)
    @Nullable
    /*package-private*/ View _nextStepView;

    @Named(PresenterNames.STORY_EDITOR)
    @Inject
    @Getter(AccessLevel.PROTECTED)
    @Nullable
    /*package-private*/ Presenter<StoryEditorScreen> _presenter;

    @BindView(R.id.previous_step)
    @Nullable
    /*package-private*/ View _previousStepView;

    @BindView(R.id.creation_step_pager)
    @Nullable
    /*package-private*/ ViewPager _stepPagerView;

    @BindView(R.id.toolbar)
    @Nullable
    /*package-private*/ Toolbar _toolbarView;

    @OnClick(R.id.next_step)
    /*package-private*/ void onNextStepClick() {
        nextStep();
    }

    @OnClick(R.id.previous_step)
    /*package-private*/ void onPreviousStepClick() {
        previousStep();
    }

    @NonNull
    private final EventHandler<StoryEditorPageChangedEventArgs> _editFragmentContentChangedHandler =
        new EventHandler<StoryEditorPageChangedEventArgs>() {
            @Override
            public void onEvent(@NonNull final StoryEditorPageChangedEventArgs eventArgs) {
                Contracts.requireNonNull(eventArgs, "eventArgs == null");

                if (_stepPagerView != null &&
                    _stepPagerView.getCurrentItem() == eventArgs.getPage()) {
                    invalidateNavigationButtons();
                }
            }
        };

    @NonNull
    private final ManagedEvent<StoryEventArgs> _editStoryEvent = Events.createEvent();

    @NonNull
    private final ManagedNoticeEvent _insertStoryEvent = Events.createNoticeEvent();

    @Getter(value = AccessLevel.PROTECTED)
    @NonNull
    private final StoryEditorPages _storyEditorPages = new StoryEditorPages();

    @Getter(value = AccessLevel.PROTECTED)
    @NonNull
    private final StoryEditorPagesAdapter _storyEditorPagesAdapter =
        new StoryEditorPagesAdapter(getSupportFragmentManager());

    @Getter(AccessLevel.PROTECTED)
    @Nullable
    private StoryEditorMode _mode;

    @Nullable
    private StoryEditorState _state;

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    @Nullable
    private UIStory _story;

    @Getter
    @Nullable
    private Long _storyId;

    private void invalidateNavigationButtons() {
        if (_stepPagerView != null) {
            final boolean nextStepAvailable = isNextStepAvailable();
            final boolean previousStepAvailable = isPreviousStepAvailable();

            if (_nextStepView != null) {
                final int visibility = nextStepAvailable ? View.VISIBLE : View.GONE;
                AnimationViewUtils.animateSetVisibility(_nextStepView,
                                                        visibility,
                                                        R.anim.fade_in_short,
                                                        R.anim.fade_out_short);
            }
            if (_previousStepView != null) {
                final int visibility = previousStepAvailable ? View.VISIBLE : View.GONE;
                AnimationViewUtils.animateSetVisibility(_previousStepView,
                                                        visibility,
                                                        R.anim.fade_in_short,
                                                        R.anim.fade_out_short);
            }
            if (_navigationBarView != null) {
                final int visibility =
                    nextStepAvailable || previousStepAvailable ? View.VISIBLE : View.GONE;
                AnimationViewUtils.animateSetVisibility(_navigationBarView,
                                                        visibility,
                                                        R.anim.slide_in_bottom_medium,
                                                        R.anim.slide_out_bottom_medium);
            }
        }
    }
}
