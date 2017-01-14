package com.christina.app.story.core.adpter.storiesList;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.val;

import com.bumptech.glide.Glide;

import com.christina.app.story.R;
import com.christina.app.story.core.StoryEventArgs;
import com.christina.app.story.data.model.Story;
import com.christina.common.contract.Contracts;
import com.christina.common.event.Events;
import com.christina.common.event.generic.Event;
import com.christina.common.event.generic.ManagedEvent;
import com.christina.common.view.recyclerView.adapter.RecyclerViewListAdapter;

import java.util.List;

@Accessors(prefix = "_")
public final class StoriesListAdapter extends RecyclerViewListAdapter<Story, StoryViewHolder> {
    public static long getStoryId(@NonNull final RecyclerView.ViewHolder holder) {
        Contracts.requireNonNull(holder, "holder == null");

        return (long) holder.itemView.getTag(R.id.holder_story_id);
    }

    public StoriesListAdapter() {
        setHasStableIds(true);
    }

    @NonNull
    public final Event<StoryEventArgs> getEditStoryEvent() {
        return _editStoryEvent;
    }

    @NonNull
    public final Event<StoryEventArgs> getShareStoryEvent() {
        return _shareStoryEvent;
    }

    @NonNull
    public final Event<StoryEventArgs> getViewStoryEvent() {
        return _viewStoryEvent;
    }

    @Override
    public StoryViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final val inflater = LayoutInflater.from(parent.getContext());
        final val view = inflater.inflate(R.layout.fragment_stories_list_item, parent, false);
        return new StoryViewHolder(view);
    }

    @Override
    public final long getItemId(final int position) {
        Contracts.requireInRange(position, 0, getItemCount() - 1, new IndexOutOfBoundsException());

        return getItem(position).getId();
    }

    @Override
    protected void onBindViewHolder(
        @NonNull final StoryViewHolder holder, @NonNull final Story item, final int position) {
        super.onBindViewHolder(Contracts.requireNonNull(holder, "holder == null"),
                               Contracts.requireNonNull(item, "item == null"),
                               Contracts.requireInRange(position,
                                                        0,
                                                        getItemCount() - 1,
                                                        new IndexOutOfBoundsException()));

        holder.itemView.setTag(R.id.holder_story_id, item.getId());

        holder.cardView.setTag(R.id.holder_story_id, item.getId());
        holder.cardView.setOnClickListener(_viewStoryOnClick);

        holder.shareStoryView.setTag(R.id.holder_story_id, item.getId());
        holder.shareStoryView.setOnClickListener(_shareStoryOnClick);

        holder.editStoryView.setTag(R.id.holder_story_id, item.getId());
        holder.editStoryView.setOnClickListener(_editStoryOnClick);

        holder.storyNameView.setText(item.getName());
        holder.storyTextView.setText(item.getText());

        Glide
            .with(holder.getContext())
            .load(item.getPreviewUri())
            .asBitmap()
            .fallback(R.drawable.im_loading_image_placeholder)
            .error(R.drawable.im_loading_image_placeholder)
            .animate(R.anim.fade_in_long)
            .centerCrop()
            .into(new StoryCardViewTarget(holder));
    }

    @NonNull
    private final ManagedEvent<StoryEventArgs> _editStoryEvent = Events.createEvent();

    private final View.OnClickListener _editStoryOnClick = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            final long storyId = (long) v.getTag(R.id.holder_story_id);
            _editStoryEvent.rise(new StoryEventArgs(storyId));
        }
    };

    @NonNull
    private final ManagedEvent<StoryEventArgs> _shareStoryEvent = Events.createEvent();

    private final View.OnClickListener _shareStoryOnClick = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            final long storyId = (long) v.getTag(R.id.holder_story_id);
            _shareStoryEvent.rise(new StoryEventArgs(storyId));
        }
    };

    @NonNull
    private final ManagedEvent<StoryEventArgs> _viewStoryEvent = Events.createEvent();

    private final View.OnClickListener _viewStoryOnClick = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            final long storyId = (long) v.getTag(R.id.holder_story_id);
            _viewStoryEvent.rise(new StoryEventArgs(storyId));
        }
    };

    @Getter(onMethod = @__(@Override))
    @Setter
    @Nullable
    private List<Story> _items;
}
