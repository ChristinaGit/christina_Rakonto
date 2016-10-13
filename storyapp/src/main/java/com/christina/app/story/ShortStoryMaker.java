package com.christina.app.story;

import android.app.Application;
import android.util.Log;

import com.christina.api.story.dao.StoryDaoManager;
import com.christina.app.story.core.StoryTextUtils;
import com.christina.app.story.debug.FakeDatabase;

import java.util.List;

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
 * <li>Implement Leave-behinds;</li>
 * </ul>
 */
public class ShortStoryMaker extends Application {
    private static final String LOG_TAG = "ShortStoryMaker";

    @Override
    public void onCreate() {
        super.onCreate();

        final List<String> defaultSplit = StoryTextUtils.defaultSplit(
            "Привет! Это моя первая короткая история. Она сделана для теста. Как она вам?");

        int i = 0;
        for (final String part : defaultSplit) {
            Log.d(LOG_TAG, "onCreate: " + i++ + " :\"" + part + "\"");
        }

        StoryDaoManager.initialize(getContentResolver());

        new FakeDatabase(false).create(getApplicationContext());
    }
}
