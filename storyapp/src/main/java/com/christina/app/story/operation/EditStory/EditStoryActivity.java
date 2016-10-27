package com.christina.app.story.operation.editStory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.christina.api.story.contract.StoryContentCode;
import com.christina.api.story.contract.StoryContract;
import com.christina.api.story.dao.StoryDaoManager;
import com.christina.api.story.model.Story;
import com.christina.app.story.R;
import com.christina.app.story.fragment.StoryEditorFragment;
import com.christina.app.story.operation.BaseStoryActivity;
import com.christina.common.ConstantBuilder;
import com.christina.common.contract.Contracts;
import com.christina.common.view.AnimationViewUtils;

public class EditStoryActivity extends BaseStoryActivity implements View.OnClickListener {
    private static final String KEY_SAVED_STATE =
        ConstantBuilder.savedStateKey(EditStoryActivity.class, "saved_state");

    protected static int resultCodeIndexer = 100;

    public static final int RESULT_INSERT_FAILED = resultCodeIndexer++;

    public static final int RESULT_NOT_FOUND = resultCodeIndexer++;

    public static final int RESULT_NO_DATA = resultCodeIndexer++;

    public static final int RESULT_UNSUPPORTED_ACTION = resultCodeIndexer++;

    @NonNull
    public static Intent getInsertIntent(@NonNull final Context context) {
        Contracts.requireNonNull(context, "context == null");

        final Intent intent = new Intent(context, EditStoryActivity.class);
        intent.setAction(Intent.ACTION_INSERT);
        intent.setData(StoryContract.getStoriesUri());
        return intent;
    }

    public static void startInsert(@NonNull final Context context) {
        Contracts.requireNonNull(context, "context == null");

        context.startActivity(getInsertIntent(context));
    }

    public static void startInsertForResult(@NonNull final Activity activity,
        final int requestCode) {
        Contracts.requireNonNull(activity, "activity == null");

        activity.startActivityForResult(getInsertIntent(activity), requestCode);
    }

    @NonNull
    public static Intent getEditIntent(@NonNull final Context context, final long storyId) {
        Contracts.requireNonNull(context, "context == null");

        final Intent intent = new Intent(context, EditStoryActivity.class);
        intent.setAction(Intent.ACTION_EDIT);
        intent.setData(StoryContract.getStoryUri(String.valueOf(storyId)));
        return intent;
    }

    public static void startEdit(@NonNull final Context context, final long storyId) {
        Contracts.requireNonNull(context, "context == null");

        context.startActivity(getEditIntent(context, storyId));
    }

    public static void startEditForResult(@NonNull final Activity activity, final long storyId,
        final int requestCode) {
        Contracts.requireNonNull(activity, "activity == null");

        activity.startActivityForResult(getEditIntent(activity, storyId), requestCode);
    }

    @CallSuper
    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.next_step: {
                nextStep();
                break;
            }
            case R.id.previous_step: {
                previousStep();
                break;
            }
            default: {
                throw new IllegalArgumentException("Click at unknown view.");
            }
        }
    }

    @NonNull
    protected final EditStoryScreensAdapter getEditStoryScreensAdapter() {
        if (_editStoryScreensAdapter == null) {
            _editStoryScreensAdapter = new EditStoryScreensAdapter(getSupportFragmentManager());
        }

        return _editStoryScreensAdapter;
    }

    @Nullable
    protected final Mode getMode() {
        return _mode;
    }

    protected final long getStoryId() {
        return _storyId;
    }

    protected final void nextStep() {
        if (_stepPagerView != null) {
            final int nextStep = _stepPagerView.getCurrentItem() + 1;
            if (nextStep < getEditStoryScreensAdapter().getCount()) {
                onStepChanging();

                _stepPagerView.setCurrentItem(nextStep, true);
            }
        }
    }

    protected final void previousStep() {
        if (_stepPagerView != null) {
            final int previousStep = _stepPagerView.getCurrentItem() - 1;
            if (previousStep >= 0) {
                onStepChanging();

                _stepPagerView.setCurrentItem(previousStep, true);
            }
        }
    }

    @CallSuper
    protected void findViews() {
        _contentContainerView = (ViewGroup) findViewById(R.id.content_container);
        _nextStepView = findViewById(R.id.next_step);
        _previousStepView = findViewById(R.id.previous_step);
        _stepPagerView = (ViewPager) findViewById(R.id.creation_step_pager);
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final boolean initialized;

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_SAVED_STATE)) {
            _savedState = savedInstanceState.getParcelable(KEY_SAVED_STATE);

            if (_savedState != null) {
                _mode = _savedState.getMode();
                _storyId = _savedState.getStoryId();

                initialized = true;
            } else {
                initialized = false;
            }
        } else {
            initialized = onHandleIntent(getIntent());
        }

        if (initialized) {
            setContentView(R.layout.activity_create_story);

            findViews();

            if (_stepPagerView != null) {
                final EditStoryScreensAdapter screensAdapter = getEditStoryScreensAdapter();
                screensAdapter.setStoryId(getStoryId());
                _stepPagerView.setAdapter(screensAdapter);
                _stepPagerView.addOnPageChangeListener(getOnStepChangedListener());
                _stepPagerView.setCurrentItem(getSavedState().getActivePage(), false);
            }
            if (_previousStepView != null) {
                _previousStepView.setOnClickListener(/*Listener*/ this);
            }
            if (_nextStepView != null) {
                _nextStepView.setOnClickListener(/*Listener*/ this);
            }

            _updateNavigationButtons();
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (_stepPagerView != null) {
            _stepPagerView.removeOnPageChangeListener(getOnStepChangedListener());
        }
        if (_previousStepView != null) {
            _previousStepView.setOnClickListener(null);
        }
        if (_nextStepView != null) {
            _nextStepView.setOnClickListener(null);
        }
    }

    protected boolean onHandleEditIntent(@NonNull final Intent intent) {
        Contracts.requireNonNull(intent, "intent == null");

        final boolean intentHandled;

        final Uri data = intent.getData();
        if (data != null) {
            final int code = StoryContentCode.Matcher.get(data);
            if (code == StoryContract.CODE_STORY) {
                long storyId = Story.NO_ID;
                try {
                    storyId = Long.parseLong(StoryContract.extractStoryId(data));
                } catch (final NumberFormatException e) {
                    setResult(RESULT_NO_DATA);
                }
                if (storyId != Story.NO_ID) {
                    final Story story = StoryDaoManager.getStoryDao().get(storyId);
                    if (story != null) {
                        _storyId = storyId;

                        final Intent resultData = new Intent();
                        resultData.setData(data);
                        setResult(RESULT_OK, resultData);

                        _mode = Mode.EDIT;
                        intentHandled = true;
                    } else {
                        setResult(RESULT_NOT_FOUND);
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
        } else {
            setResult(RESULT_NO_DATA);
            intentHandled = false;
        }

        return intentHandled;
    }

    protected boolean onHandleInsertIntent(@NonNull final Intent intent) {
        Contracts.requireNonNull(intent, "intent == null");

        final boolean intentHandled;

        final Story story = StoryDaoManager.getStoryDao().create();
        if (story != null) {
            _storyId = story.getId();

            story.setCreateDate();
            story.setModifyDate();

            StoryDaoManager.getStoryDao().update(story);

            final Intent resultData = new Intent();
            resultData.setData(StoryContract.getStoryUri(String.valueOf(story.getId())));
            setResult(RESULT_OK, resultData);

            _mode = Mode.INSERT;
            intentHandled = true;
        } else {
            setResult(RESULT_INSERT_FAILED);
            intentHandled = false;
        }

        return intentHandled;
    }

    protected boolean onHandleIntent(@NonNull final Intent intent) {
        Contracts.requireNonNull(intent, "intent == null");

        final boolean intentHandled;

        final String action = getIntent().getAction();
        switch (action) {
            case Intent.ACTION_INSERT: {
                intentHandled = onHandleInsertIntent(intent);
                break;
            }
            case Intent.ACTION_EDIT: {
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
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);

        if (outState != null) {
            final EditStorySavedState savedState = getSavedState();
            savedState.setStoryId(getStoryId());
            savedState.setMode(getMode());
            if (_stepPagerView != null) {
                savedState.setActivePage(_stepPagerView.getCurrentItem());
            } else {
                savedState.setActivePage(0);
            }
            outState.putParcelable(KEY_SAVED_STATE, savedState);
        }
    }

    protected void onStepChanging() {
        if (_stepPagerView != null) {
            final int currentItem = _stepPagerView.getCurrentItem();
            final Fragment item = getEditStoryScreensAdapter().getFragment(currentItem);
            if (item instanceof StoryEditorFragment) {
                ((StoryEditorFragment) item).saveStoryChanges();
            }
        }
    }

    @Nullable
    private ViewGroup _contentContainerView;

    @Nullable
    private EditStoryScreensAdapter _editStoryScreensAdapter;

    @Nullable
    private Mode _mode;

    @Nullable
    private View _nextStepView;

    @Nullable
    private ViewPager.OnPageChangeListener _onStepChangedListener;

    @Nullable
    private View _previousStepView;

    @Nullable
    private EditStorySavedState _savedState;

    @Nullable
    private ViewPager _stepPagerView;

    private long _storyId = Story.NO_ID;

    private void _updateNavigationButtons() {
        if (_stepPagerView != null) {
            final int position = _stepPagerView.getCurrentItem();

            if (_nextStepView != null) {
                final boolean lastStep = getEditStoryScreensAdapter().getCount() - 1 == position;
                final int nextStepVisibility = lastStep ? View.GONE : View.VISIBLE;
                AnimationViewUtils.animateSetVisibility(_nextStepView, nextStepVisibility,
                    R.anim.fade_in_short, R.anim.fade_out_short);
            }
            if (_previousStepView != null) {
                final boolean firstStep = position == 0;
                final int previousStepVisibility = firstStep ? View.GONE : View.VISIBLE;
                AnimationViewUtils.animateSetVisibility(_previousStepView, previousStepVisibility,
                    R.anim.fade_in_short, R.anim.fade_out_short);
            }
        }
    }

    @Nullable
    private ViewPager.OnPageChangeListener getOnStepChangedListener() {
        if (_onStepChangedListener == null) {
            _onStepChangedListener = new ViewPager.SimpleOnPageChangeListener() {
                @CallSuper
                @Override
                public void onPageSelected(final int position) {
                    _updateNavigationButtons();
                }
            };
        }
        return _onStepChangedListener;
    }

    @NonNull
    private EditStorySavedState getSavedState() {
        if (_savedState == null) {
            _savedState = new EditStorySavedState();
        }

        return _savedState;
    }

    public enum Mode {
        INSERT,
        EDIT
    }
}
