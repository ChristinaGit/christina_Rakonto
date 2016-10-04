package com.christina.storymaker;

import android.app.Application;

import com.christina.content.story.dao.StoryDaoManager;
import com.christina.storymaker.debug.FakeDatabase;

/**
 * TODO:
 * <ul>
 * <li>Animate FAB;</li>
 * <li>Implement two-phased loads;</li>
 * <li>Implement card view loading;</li>
 * <li>Implement stories list position restoring;</li>
 * <li>Hide FAB on scroll;</li>
 * <li>Fix fragment state;</li>
 * <li>Add custom provider permissions;</li>
 * <li>Convert layouts to ConstraintLayout;</li>
 * </ul>
 */
public class ShortStoryMaker extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        StoryDaoManager.initialize(getContentResolver());

        new FakeDatabase(false).create(getApplicationContext());
    }
}
