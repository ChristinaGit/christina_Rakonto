package com.christina.app.story;

import android.app.Application;
import android.support.annotation.NonNull;

import com.christina.app.story.debug.FakeDatabase;
import com.christina.app.story.di.StoryApplicationComponentProvider;
import com.christina.app.story.di.storyApplication.DaggerStoryApplicationComponent;
import com.christina.app.story.di.storyApplication.StoryApplicationComponent;
import com.christina.app.story.di.storyApplication.module.ApplicationContextModule;
import com.christina.app.story.di.storyApplication.module.ApplicationServiceModule;
import com.christina.app.story.di.storyApplication.module.StoryContentExtractorModule;
import com.christina.app.story.di.storyApplication.module.StoryDaoModule;
import com.christina.app.story.di.storyApplication.module.StoryFactoryModule;
import com.christina.app.story.di.storyApplication.module.StoryProjectionModule;

import lombok.Getter;
import lombok.experimental.Accessors;

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
@Accessors(prefix = "_")
public final class StoryApplication extends Application
    implements StoryApplicationComponentProvider {
    @Override
    public void onCreate() {
        super.onCreate();

        final StoryApplicationComponent component = getStoryApplicationComponent();
        new FakeDatabase(component.getStoryDao(), component.getStoryFrameDao(), false).create(
            getApplicationContext());
    }

    @NonNull
    @Getter(onMethod = @__(@Override), lazy = true)
    private final StoryApplicationComponent _storyApplicationComponent =
        DaggerStoryApplicationComponent
            .builder()
            .applicationContextModule(new ApplicationContextModule(getApplicationContext()))
            .applicationServiceModule(new ApplicationServiceModule())
            .storyContentExtractorModule(new StoryContentExtractorModule())
            .storyDaoModule(new StoryDaoModule())
            .storyFactoryModule(new StoryFactoryModule())
            .storyProjectionModule(new StoryProjectionModule())
            .build();
}
