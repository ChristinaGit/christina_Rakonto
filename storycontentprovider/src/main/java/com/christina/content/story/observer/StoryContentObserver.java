package com.christina.content.story.observer;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.christina.common.data.model.Model;
import com.christina.common.event.BaseEvent;
import com.christina.common.event.Event;
import com.christina.content.story.contract.StoryCode;
import com.christina.content.story.contract.StoryContract;
import com.christina.content.story.contract.StoryFrameContract;

public final class StoryContentObserver extends ContentObserver {
    public StoryContentObserver() {
        this(new Handler(Looper.getMainLooper()));
    }

    public StoryContentObserver(final Handler handler) {
        super(handler);
    }

    @Override
    public void onChange(final boolean selfChange, final Uri uri) {
        super.onChange(selfChange, uri);

        if (uri != null) {
            final int code = StoryCode.get(uri);
            final int entityTypeCode = StoryCode.getEntityTypeCode(code);

            if (StoryContract.ENTITY_TYPE_CODE == entityTypeCode) {
                final String type = StoryContract.getType(code);

                if (StoryContract.ITEM_TYPE.equals(type)) {
                    final String idString = StoryContract.extractStoryId(uri);
                    long id;
                    try {
                        id = Long.parseLong(idString);
                    } catch (NumberFormatException e) {
                        id = Model.NO_ID;
                    }

                    _storyChanged.onEvent(this, new StoryObserverEventArgs(id));
                } else if (StoryContract.DIR_TYPE.equals(type)) {
                    _storyChanged.onEvent(this, new StoryObserverEventArgs());
                }
            } else if (StoryFrameContract.ENTITY_TYPE_CODE == entityTypeCode) {
                final String type = StoryFrameContract.getType(code);

                if (StoryFrameContract.ITEM_TYPE.equals(type)) {
                    final String idString = StoryFrameContract.extractStoryFrameId(uri);
                    long id;
                    try {
                        id = Long.parseLong(idString);
                    } catch (NumberFormatException e) {
                        id = Model.NO_ID;
                    }

                    _storyFrameChanged.onEvent(this, new StoryObserverEventArgs(id));
                } else if (StoryFrameContract.DIR_TYPE.equals(type)) {
                    _storyFrameChanged.onEvent(this, new StoryObserverEventArgs());
                }
            }
        }
    }

    @NonNull
    public final Event<StoryObserverEventArgs> getStoryChanged() {
        return _storyChanged;
    }

    @NonNull
    public final Event<StoryObserverEventArgs> getStoryFrameChanged() {
        return _storyFrameChanged;
    }

    @NonNull
    private final BaseEvent<StoryObserverEventArgs> _storyFrameChanged = new BaseEvent<>();

    @NonNull
    private final BaseEvent<StoryObserverEventArgs> _storyChanged = new BaseEvent<>();
}