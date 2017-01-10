package com.christina.app.story.core.adpter.storiesList;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.christina.api.story.dao.story.StoryFullProjection;
import com.christina.api.story.model.Story;
import com.christina.app.story.R;
import com.christina.app.story.core.StoryEventArgs;
import com.christina.common.contract.Contracts;
import com.christina.common.event.BaseEvent;
import com.christina.common.event.Event;
import com.christina.common.view.recyclerView.adapter.DataCursorRecyclerViewAdapter;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;

@Accessors(prefix = "_")
public final class StoriesListAdapter
    extends DataCursorRecyclerViewAdapter<Story, StoryViewHolder> {
    public static long getStoryId(@NonNull final RecyclerView.ViewHolder holder) {
        Contracts.requireNonNull(holder, "holder == null");

        return (long) holder.itemView.getTag(R.id.holder_story_id);
    }

    public StoriesListAdapter() {
        setHasStableIds(true);
    }

    @NonNull
    public final Event<StoryEventArgs> getOnEditStoryEvent() {
        return _onEditStoryEvent;
    }

    @NonNull
    public final Event<StoryEventArgs> getOnShareStoryEvent() {
        return _onShareStoryEvent;
    }

    @NonNull
    public final Event<StoryEventArgs> getOnViewStoryEvent() {
        return _onViewStoryEvent;
    }

    @Override
    public StoryViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final val inflater = LayoutInflater.from(parent.getContext());
        final val view = inflater.inflate(R.layout.fragment_stories_list_item, parent, false);
        return new StoryViewHolder(view);
    }

    @Override
    public long getItemId(final int position) {
        final long itemId;

        final val dataCursor = getDataCursor();
        if (dataCursor != null && dataCursor.moveToPosition(position)) {
            itemId = getStoryFullProjection().getId(dataCursor);
        } else {
            itemId = Story.NO_ID;
        }

        return itemId;
    }

    @Override
    protected void onBindViewHolder(
        @NonNull final StoryViewHolder holder, @NonNull final Story item, final int position) {
        Contracts.requireNonNull(holder, "holder == null");
        Contracts.requireNonNull(item, "item == null");

        holder.itemView.setTag(R.id.holder_story_id, item.getId());

        holder.cardView.setTag(R.id.holder_story_id, item.getId());
        holder.cardView.setOnClickListener(getViewStoryOnClick());

        holder.shareStoryView.setTag(R.id.holder_story_id, item.getId());
        holder.shareStoryView.setOnClickListener(getShareStoryOnClick());

        holder.editStoryView.setTag(R.id.holder_story_id, item.getId());
        holder.editStoryView.setOnClickListener(getEditStoryOnClick());

        holder.storyNameView.setText(item.getName());
        holder.storyTextView.setText(item.getText());

        Glide
            .with(holder.getContext())
            .load(item.getPreviewUri())
            .asBitmap()
            .fallback(R.drawable.im_loading_image_placeholder)
            .error(R.drawable.im_loading_image_placeholder)
            .animate(android.R.anim.fade_in)
            .centerCrop()
            .into(new StoryCardViewTarget(holder));
    }

    @NonNull
    private final BaseEvent<StoryEventArgs> _onEditStoryEvent = new BaseEvent<>();

    @Getter(value = AccessLevel.PRIVATE, lazy = true)
    private final View.OnClickListener _editStoryOnClick = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            final long storyId = (long) v.getTag(R.id.holder_story_id);
            _onEditStoryEvent.rise(new StoryEventArgs(storyId));
        }
    };

    @NonNull
    private final BaseEvent<StoryEventArgs> _onShareStoryEvent = new BaseEvent<>();

    @NonNull
    private final BaseEvent<StoryEventArgs> _onViewStoryEvent = new BaseEvent<>();

    @Getter(value = AccessLevel.PRIVATE, lazy = true)
    private final View.OnClickListener _shareStoryOnClick = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            final long storyId = (long) v.getTag(R.id.holder_story_id);
            _onShareStoryEvent.rise(new StoryEventArgs(storyId));
        }
    };

    @Getter(value = AccessLevel.PROTECTED, lazy = true)
    @NonNull
    private final StoryFullProjection _storyFullProjection = new StoryFullProjection();

    @Getter(value = AccessLevel.PRIVATE, lazy = true)
    private final View.OnClickListener _viewStoryOnClick = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            final long storyId = (long) v.getTag(R.id.holder_story_id);
            _onViewStoryEvent.rise(new StoryEventArgs(storyId));
        }
    };
}
