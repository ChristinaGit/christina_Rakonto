package com.christina.app.story.fragment.storiesViewer.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.christina.api.story.dao.story.StoryFullProjection;
import com.christina.api.story.model.Story;
import com.christina.app.story.R;
import com.christina.app.story.core.StoryContentEventArgs;
import com.christina.common.contract.Contracts;
import com.christina.common.data.cursor.dataCursor.DataCursor;
import com.christina.common.event.BaseEvent;
import com.christina.common.event.Event;
import com.christina.common.view.recyclerView.adapter.DataCursorRecyclerViewAdapter;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;

@Accessors(prefix = "_")
public final class StoriesAdapter extends DataCursorRecyclerViewAdapter<Story, StoryViewHolder> {
    public static long getStoryId(@NonNull final RecyclerView.ViewHolder holder) {
        Contracts.requireNonNull(holder, "holder == null");

        return (long) holder.itemView.getTag(R.id.holder_story_id);
    }

    public StoriesAdapter() {
        setHasStableIds(true);
    }

    @NonNull
    public final Event<StoryContentEventArgs> onEditStory() {
        return _onEditStory;
    }

    @NonNull
    public final Event<StoryContentEventArgs> onShareStory() {
        return _onShareStory;
    }

    @NonNull
    public final Event<StoryContentEventArgs> onViewStory() {
        return _onViewStory;
    }

    @Override
    public StoryViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final val inflater = LayoutInflater.from(parent.getContext());
        final val view = inflater.inflate(R.layout.fragment_stories_viewer_item, parent, false);
        return new StoryViewHolder(view);
    }

    @Override
    public long getItemId(final int position) {
        final long itemId;

        final DataCursor<Story> dataCursor = getDataCursor();
        if (dataCursor != null && dataCursor.moveToPosition(position)) {
            itemId = _storyFullProjection.getId(dataCursor);
        } else {
            itemId = Story.NO_ID;
        }

        return itemId;
    }

    @Override
    protected void onBindViewHolder(
        @NonNull final StoryViewHolder holder, @Nullable final Story item, final int position) {
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
            .animate(android.R.anim.fade_in)
            .centerCrop()
            .into(new StoryCardViewTarget(holder));
    }

    @NonNull
    private final BaseEvent<StoryContentEventArgs> _onEditStory = new BaseEvent<>();

    @Getter(value = AccessLevel.PRIVATE, lazy = true)
    private final View.OnClickListener _editStoryOnClick = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            final long storyId = (long) v.getTag(R.id.holder_story_id);
            _onEditStory.rise(new StoryContentEventArgs(storyId));
        }
    };

    @NonNull
    private final BaseEvent<StoryContentEventArgs> _onShareStory = new BaseEvent<>();

    @NonNull
    private final BaseEvent<StoryContentEventArgs> _onViewStory = new BaseEvent<>();

    @Getter(value = AccessLevel.PRIVATE, lazy = true)
    private final View.OnClickListener _shareStoryOnClick = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            final long storyId = (long) v.getTag(R.id.holder_story_id);
            _onShareStory.rise(new StoryContentEventArgs(storyId));
        }
    };

    @Getter(value = AccessLevel.PRIVATE, lazy = true)
    private final View.OnClickListener _viewStoryOnClick = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            final long storyId = (long) v.getTag(R.id.holder_story_id);
            _onViewStory.rise(new StoryContentEventArgs(storyId));
        }
    };

    private StoryFullProjection _storyFullProjection = new StoryFullProjection();
}
