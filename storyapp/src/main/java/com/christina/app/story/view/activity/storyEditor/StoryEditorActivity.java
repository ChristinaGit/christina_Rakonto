package com.christina.app.story.view.activity.storyEditor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.christina.api.story.contract.StoryContentCode;
import com.christina.api.story.contract.StoryContract;
import com.christina.api.story.model.Story;
import com.christina.app.story.R;
import com.christina.app.story.adpter.storyEditorPages.StoryEditorPageContentChangedEventArgs;
import com.christina.app.story.adpter.storyEditorPages.StoryEditorPagesAdapter;
import com.christina.app.story.core.StoryEventArgs;
import com.christina.app.story.di.qualifier.PresenterNames;
import com.christina.app.story.view.StoryEditorPresentableView;
import com.christina.app.story.view.activity.BaseStoryActivity;
import com.christina.common.ConstantBuilder;
import com.christina.common.contract.Contracts;
import com.christina.common.event.BaseEvent;
import com.christina.common.event.BaseNoticeEvent;
import com.christina.common.event.Event;
import com.christina.common.event.EventHandler;
import com.christina.common.event.NoticeEvent;
import com.christina.common.view.AnimationViewUtils;
import com.christina.common.view.presentation.Presenter;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnPageChange;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;

@Accessors(prefix = "_")
public final class StoryEditorActivity extends BaseStoryActivity
    implements StoryEditorPresentableView {
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
    public final void displayStory(final long storyId) {
        setDisplayedStoryId(storyId);

        if (storyId != Story.NO_ID) {
            final val mode = getMode();
            if (mode == Mode.INSERT) {
                final val resultData = new Intent();
                resultData.setData(StoryContract.getStoryUri(String.valueOf(storyId)));
                setResult(RESULT_OK, resultData);
            } else if (mode == Mode.EDIT) {
                final val resultData = new Intent();
                resultData.setData(getIntent().getData());
                setResult(RESULT_OK, resultData);
            }
        } else {
            final val mode = getMode();
            if (mode == Mode.INSERT) {
                setResult(RESULT_INSERT_STORY_FAILED);
                finish();
            } else if (mode == Mode.EDIT) {
                setResult(RESULT_NOT_FOUND);
                finish();
            }
        }
    }

    @NonNull
    @Override
    public final Event<StoryEventArgs> getOnEditStoryEvent() {
        return _onEditStoryEvent;
    }

    @NonNull
    @Override
    public final NoticeEvent getOnInsertStoryEvent() {
        return _onInsertStoryEvent;
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

    protected final void setDisplayedStoryId(final long displayedStoryId) {
        if (_displayedStoryId != displayedStoryId) {
            _displayedStoryId = displayedStoryId;

            onDisplayedStoryIdChanged();
        }
    }

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

    protected boolean isPreviousStepAvailable() {
        boolean isPreviousStepAvailable = false;

        if (_stepPagerView != null) {
            final int position = _stepPagerView.getCurrentItem();
            final boolean firstStep = position == 0;
            isPreviousStepAvailable = !firstStep;
        }

        return isPreviousStepAvailable;
    }

    @Override
    protected void onBindPresenter() {
        super.onBindPresenter();

        final val presenter = getPresenter();
        if (presenter != null) {
            presenter.setPresentableView(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        getStoryEditorPagesAdapter()
            .getOnContentChangedEvent()
            .removeHandler(getEditFragmentContentChangedHandler());
    }

    @Override
    protected void onResume() {
        super.onResume();

        getStoryEditorPagesAdapter()
            .getOnContentChangedEvent()
            .addHandler(getEditFragmentContentChangedHandler());

        _invalidateNavigationButtons();
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final boolean initialized;

        if (savedInstanceState != null && savedInstanceState.containsKey(_KEY_SAVED_STATE)) {
            _savedState = savedInstanceState.getParcelable(_KEY_SAVED_STATE);

            if (_savedState != null) {
                _mode = _savedState.getMode();
                _displayedStoryId = _savedState.getDisplayedStoryId();

                initialized = true;
            } else {
                initialized = false;
            }
        } else {
            initialized = onHandleIntent(getIntent());
        }

        if (initialized) {
            setContentView(R.layout.activity_story_editor);

            bindViews();

            if (_stepPagerView != null) {
                final val screensAdapter = getStoryEditorPagesAdapter();
                screensAdapter.setPageFactory(getStoryEditorPages());
                screensAdapter.setDisplayedStoryId(getDisplayedStoryId());
                _stepPagerView.setAdapter(screensAdapter);

                final int activePage;
                if (_savedState != null) {
                    activePage = _savedState.getActivePage();
                } else {
                    activePage = 0;
                }
                _stepPagerView.setCurrentItem(activePage, false);
            }

            final val mode = getMode();
            if (mode != null) {
                switch (mode) {
                    case INSERT: {
                        _onInsertStoryEvent.rise();
                        break;
                    }
                    case EDIT: {
                        _onEditStoryEvent.rise(new StoryEventArgs(getDisplayedStoryId()));
                        break;
                    }
                    default: {
                        throw new IllegalStateException("Unknown activity mode.");
                    }
                }
            } else {
                throw new IllegalStateException("Activity mode is null after the handled intent.");
            }
        } else {
            finish();
        }
    }

    @Override
    protected void onUnbindPresenter() {
        super.onUnbindPresenter();

        final val presenter = getPresenter();
        if (presenter != null) {
            presenter.setPresentableView(null);
        }
    }

    protected void onDisplayedStoryIdChanged() {
        final val screensAdapter = getStoryEditorPagesAdapter();
        screensAdapter.setDisplayedStoryId(getDisplayedStoryId());
    }

    protected void onEnterStep(final int position) {
        final val editorFragment = getStoryEditorPagesAdapter().getEditorPage(position);
        if (editorFragment != null) {
            editorFragment.onStartEditing();
        }
    }

    protected boolean onHandleEditIntent(@NonNull final Intent intent) {
        Contracts.requireNonNull(intent, "intent == null");

        final boolean intentHandled;

        final val data = intent.getData();
        if (data != null) {
            final int code = StoryContentCode.Matcher.get(data);
            if (code == StoryContract.CODE_STORY) {
                long storyId;
                try {
                    storyId = Long.parseLong(StoryContract.extractStoryId(data));
                } catch (final NumberFormatException e) {
                    storyId = Story.NO_ID;
                }
                if (storyId != Story.NO_ID) {
                    _displayedStoryId = storyId;
                    intentHandled = true;
                } else {
                    setResult(RESULT_NO_DATA);
                    intentHandled = false;
                }
            } else {
                setResult(RESULT_NO_DATA);
                intentHandled = false;
            }
        } else {
            setResult(RESULT_NO_DATA);
            intentHandled = false;
        }

        return intentHandled;
    }

    protected boolean onHandleIntent(@NonNull final Intent intent) {
        Contracts.requireNonNull(intent, "intent == null");

        final boolean intentHandled;

        final val action = getIntent().getAction();
        switch (action) {
            case Intent.ACTION_INSERT: {
                _mode = Mode.INSERT;
                intentHandled = true;
                break;
            }
            case Intent.ACTION_EDIT: {
                _mode = Mode.EDIT;
                intentHandled = onHandleEditIntent(intent);
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

    @Override
    protected void onInject() {
        super.onInject();

        getStoryViewComponent().inject(this);
    }

    @Override
    protected void onReleaseInject() {
        super.onReleaseInject();

        unbindViews();
    }

    protected void onLeaveStep(final int position) {
        final val editorFragment = getStoryEditorPagesAdapter().getEditorPage(position);
        if (editorFragment != null) {
            editorFragment.onStopEditing();
        }
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);

        if (outState != null) {
            if (_savedState == null) {
                _savedState = new StoryEditorSavedState();
            }

            _savedState.setMode(getMode());
            _savedState.setDisplayedStoryId(getDisplayedStoryId());
            if (_stepPagerView != null) {
                _savedState.setActivePage(_stepPagerView.getCurrentItem());
            } else {
                _savedState.setActivePage(0);
            }
            outState.putParcelable(_KEY_SAVED_STATE, _savedState);
        }
    }

    @OnPageChange(R.id.creation_step_pager)
    protected void onStepChanged(final int position) {
        _invalidateNavigationButtons();
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
    /*package-private*/ Presenter<StoryEditorPresentableView> _presenter;

    @BindView(R.id.previous_step)
    @Nullable
    /*package-private*/ View _previousStepView;

    @BindView(R.id.creation_step_pager)
    @Nullable
    /*package-private*/ ViewPager _stepPagerView;

    @OnClick(R.id.next_step)
    /*package-private*/ void onNextStepClick() {
        nextStep();
    }

    @OnClick(R.id.previous_step)
    /*package-private*/ void onPreviousStepClick() {
        previousStep();
    }

    @Getter(value = AccessLevel.PRIVATE, lazy = true)
    @NonNull
    private final EventHandler<StoryEditorPageContentChangedEventArgs>
        _editFragmentContentChangedHandler =
        new EventHandler<StoryEditorPageContentChangedEventArgs>() {
            @Override
            public void onEvent(@NonNull final StoryEditorPageContentChangedEventArgs eventArgs) {
                Contracts.requireNonNull(eventArgs, "eventArgs == null");

                if (_stepPagerView != null &&
                    _stepPagerView.getCurrentItem() == eventArgs.getPage()) {
                    _invalidateNavigationButtons();
                }
            }
        };

    @NonNull
    private final BaseEvent<StoryEventArgs> _onEditStoryEvent = new BaseEvent<>();

    @NonNull
    private final BaseNoticeEvent _onInsertStoryEvent = new BaseNoticeEvent();

    @Getter(value = AccessLevel.PROTECTED, lazy = true)
    @NonNull
    private final StoryEditorPages _storyEditorPages = new StoryEditorPages();

    @Getter(value = AccessLevel.PROTECTED, lazy = true)
    @NonNull
    private final StoryEditorPagesAdapter _storyEditorPagesAdapter =
        new StoryEditorPagesAdapter(getSupportFragmentManager());

    @Getter
    private long _displayedStoryId = Story.NO_ID;

    @Getter(AccessLevel.PROTECTED)
    @Nullable
    private Mode _mode;

    @Nullable
    private StoryEditorSavedState _savedState;

    private void _invalidateNavigationButtons() {
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

    public enum Mode {
        INSERT,
        EDIT
    }
}
