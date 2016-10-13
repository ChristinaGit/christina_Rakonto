package com.christina.app.story.operation.createStory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.christina.api.story.dao.StoryDaoManager;
import com.christina.api.story.model.Story;
import com.christina.app.story.R;
import com.christina.common.contract.Contracts;
import com.christina.common.view.AnimationViewUtils;

public class CreateStoryActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_OUT_STORY_ID = "out_story_id";

    @NonNull
    public static Intent getIntent(@NonNull final Context context) {
        Contracts.requireNonNull(context, "context == null");

        return new Intent(context, CreateStoryActivity.class);
    }

    public static void start(@NonNull final Context context) {
        Contracts.requireNonNull(context, "activity == null");

        context.startActivity(getIntent(context));
    }

    public static void startForResult(@NonNull final Activity activity, final int requestCode) {
        Contracts.requireNonNull(activity, "activity == null");

        activity.startActivityForResult(getIntent(activity), requestCode);
    }

    public static long getStoryId(@NonNull final Intent intent) {
        Contracts.requireNonNull(intent, "intent == null");

        return intent.getLongExtra(EXTRA_OUT_STORY_ID, Story.NO_ID);
    }

    private static void putStoryId(@NonNull final Intent intent, final long storyId) {
        Contracts.requireNonNull(intent, "intent == null");

        intent.putExtra(EXTRA_OUT_STORY_ID, storyId);
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
    protected final CreateStoryStepsAdapter getCreateStoryStepsAdapter() {
        if (_createStoryStepsAdapter == null) {
            _createStoryStepsAdapter = new CreateStoryStepsAdapter(getSupportFragmentManager());
        }

        return _createStoryStepsAdapter;
    }

    protected final void nextStep() {
        if (_stepPagerView != null) {
            final int nextStep = _stepPagerView.getCurrentItem() + 1;
            if (nextStep < getCreateStoryStepsAdapter().getCount()) {
                _stepPagerView.setCurrentItem(nextStep, true);
            }
        }
    }

    protected final void previousStep() {
        if (_stepPagerView != null) {
            final int previousStep = _stepPagerView.getCurrentItem() - 1;
            if (previousStep >= 0) {
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

        setContentView(R.layout.activity_create_story);

        findViews();

        if (_stepPagerView != null) {
            _stepPagerView.setAdapter(getCreateStoryStepsAdapter());
            _stepPagerView.addOnPageChangeListener(getOnStepChangedListener());
            _stepPagerView.setCurrentItem(0, false);
        }

        if (_previousStepView != null) {
            _previousStepView.setOnClickListener(/*Listener*/ this);
        }
        if (_nextStepView != null) {
            _nextStepView.setOnClickListener(/*Listener*/ this);
        }

        _updateNavigationButtons();

        _story = StoryDaoManager.getStoryDao().create();
        if (_story != null) {
            _story.setCreateDate();
            _story.setModifyDate();

            final Intent resultData = new Intent();
            putStoryId(resultData, _story.getId());
            setResult(RESULT_OK, resultData);

            getCreateStoryStepsAdapter().setStoryId(_story.getId());
        } else {
            setResult(RESULT_CANCELED);
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

    @Nullable
    private ViewGroup _contentContainerView;

    @Nullable
    private CreateStoryStepsAdapter _createStoryStepsAdapter;

    @Nullable
    private View _nextStepView;

    @Nullable
    private ViewPager.OnPageChangeListener _onStepChangedListener;

    @Nullable
    private View _previousStepView;

    @Nullable
    private ViewPager _stepPagerView;

    private Story _story;

    private void _updateNavigationButtons() {
        if (_stepPagerView != null) {
            final int position = _stepPagerView.getCurrentItem();

            if (_nextStepView != null) {
                final boolean lastStep = getCreateStoryStepsAdapter().getCount() - 1 == position;
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
}
