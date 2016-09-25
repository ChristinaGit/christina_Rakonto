package com.christina.storymaker;

import android.app.Application;

import com.christina.content.story.dao.StoryDaoManager;
import com.christina.content.story.database.StoryDatabase;
import com.christina.storymaker.debug.FakeDatabase;

public class ShortStoryMaker extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        StoryDaoManager.initialize(getContentResolver());

        deleteDatabase(StoryDatabase.NAME);
        new FakeDatabase().create(getApplicationContext());
    }
}
