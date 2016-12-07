package com.christina.api.story.observer;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.christina.api.story.contract.StoryContentCode;
import com.christina.api.story.contract.StoryContract;
import com.christina.api.story.contract.StoryFrameContract;
import com.christina.common.data.model.Model;
import com.christina.common.event.BaseEvent;
import com.christina.common.event.Event;

public final class StoryContentObserver extends ContentObserver {
    public StoryContentObserver() {
        this(new Handler(Looper.getMainLooper()));
    }

    public StoryContentObserver(final Handler handler) {
        super(handler);
    }

    @NonNull
    public final Event<StoryObserverEventArgs> getOnStoryChangedEvent() {
        return _onStoryChangedEvent;
    }

    @NonNull
    public final Event<StoryObserverEventArgs> getOnStoryFrameChangedEvent() {
        return _onStoryFrameChangedEvent;
    }

    @Override
    public void onChange(final boolean selfChange, final Uri uri) {
        super.onChange(selfChange, uri);

        if (uri != null) {
            final int code = StoryContentCode.Matcher.get(uri);
            final int entityTypeCode = StoryContentCode.getEntityTypeCode(code);

            if (StoryContract.ENTITY_TYPE_CODE == entityTypeCode) {
                final String type = StoryContract.getType(code);

                if (StoryContract.ITEM_TYPE.equals(type)) {
                    final String idString = StoryContract.extractStoryId(uri);
                    long id;
                    try {
                        id = Long.parseLong(idString);
                    } catch (final NumberFormatException e) {
                        id = Model.NO_ID;
                    }

                    _onStoryChangedEvent.rise(new StoryObserverEventArgs(id));
                } else if (StoryContract.DIR_TYPE.equals(type)) {
                    _onStoryChangedEvent.rise(StoryObserverEventArgs.EMPTY);
                }
            } else if (StoryFrameContract.ENTITY_TYPE_CODE == entityTypeCode) {
                final String type = StoryFrameContract.getType(code);

                if (StoryFrameContract.ITEM_TYPE.equals(type)) {
                    final String idString = StoryFrameContract.extractStoryFrameId(uri);
                    long id;
                    try {
                        id = Long.parseLong(idString);
                    } catch (final NumberFormatException e) {
                        id = Model.NO_ID;
                    }

                    _onStoryFrameChangedEvent.rise(new StoryObserverEventArgs(id));
                } else if (StoryFrameContract.DIR_TYPE.equals(type)) {
                    _onStoryFrameChangedEvent.rise(StoryObserverEventArgs.EMPTY);
                }
            }
        }
    }

    @NonNull
    private final BaseEvent<StoryObserverEventArgs> _onStoryChangedEvent = new BaseEvent<>();

    @NonNull
    private final BaseEvent<StoryObserverEventArgs> _onStoryFrameChangedEvent = new BaseEvent<>();
}
