package com.christina.storymaker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.christina.common.contract.Contracts;
import com.christina.storymaker.storyCreator.StoryCreationStepsAdapter;
import com.christina.storymaker.storyCreator.StoryCreationStepsPagerView;
import com.christina.storymaker.storyCreator.StoryTextInputFragment;

import java.util.Arrays;

public class NewStoryActivity extends AppCompatActivity {
    @NonNull
    public static Intent getIntent(@NonNull final Context context) {
        Contracts.requireNonNull(context, "context == null");

        return new Intent(context, NewStoryActivity.class);
    }

    public static void start(@NonNull final Context context) {
        Contracts.requireNonNull(context, "activity == null");

        context.startActivity(getIntent(context));
    }

    public static void startForResult(@NonNull final Activity activity, final int requestCode) {
        Contracts.requireNonNull(activity, "activity == null");

        activity.startActivityForResult(getIntent(activity), requestCode);
    }

    @CallSuper
    protected void findViews() {
        _contentContainerView = (ViewGroup) findViewById(R.id.content_container);
        _creationStepsView = (StoryCreationStepsPagerView) findViewById(R.id.creation_steps);
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.new_story);

        findViews();

        if (_creationStepsView != null) {
            final StoryCreationStepsAdapter storyCreationStepsAdapter =
                new StoryCreationStepsAdapter(getSupportFragmentManager());

            storyCreationStepsAdapter.setCreationSteps(
                Arrays.asList((Fragment) StoryTextInputFragment.create()));
            _creationStepsView.setAdapter(storyCreationStepsAdapter);
        }
    }

    @Nullable
    private ViewGroup _contentContainerView;

    @Nullable
    private StoryCreationStepsPagerView _creationStepsView;
}
