package com.christina.app.story.fragment.storiesViewer.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.christina.common.contract.Contracts;
import com.christina.common.event.BaseEvent;
import com.christina.common.event.Event;
import com.christina.common.view.recyclerView.BaseStableRecyclerViewAdapter;
import com.christina.api.story.model.Story;
import com.christina.app.story.R;
import com.christina.app.story.core.StoryContentEventArgs;

public final class StoriesAdapter
    extends BaseStableRecyclerViewAdapter<Story, StoryListItem, StoryItemViewHolder> {

    @NonNull
    public final Event<StoryContentEventArgs> onEditStory() {
        return _editStory;
    }

    @NonNull
    public final Event<StoryContentEventArgs> onShareStory() {
        return _shareStory;
    }

    @NonNull
    public final Event<StoryContentEventArgs> onViewStory() {
        return _viewStory;
    }

    @Override
    public StoryItemViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(R.layout.fragment_stories_viewer_item, parent, false);
        return new StoryItemViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull final StoryItemViewHolder holder,
        @NonNull final StoryListItem listItem, final int position) {
        Contracts.requireNonNull(holder, "holder == null");
        Contracts.requireNonNull(listItem, "listItem == null");

        holder.storyNameView.setText(listItem.getStoryName());
        holder.storyTextView.setText(listItem.getStoryText());

        holder.cardView.setTag(R.id.holder_story_id, listItem.getId());
        holder.cardView.setOnClickListener(_viewStoryOnClick);

        holder.shareStoryView.setTag(R.id.holder_story_id, listItem.getId());
        holder.shareStoryView.setOnClickListener(_shareStoryOnClick);

        holder.editStoryView.setTag(R.id.holder_story_id, listItem.getId());
        holder.editStoryView.setOnClickListener(_editStoryOnClick);

        Glide
            .with(holder.getContext())
            .load(listItem.getStoryPreviewUri())
            .asBitmap()
            .animate(android.R.anim.fade_in)
            .centerCrop()
            .into(new StoryCardViewTarget(holder))
            .getRequest()
            .begin();
    }

    @NonNull
    @Override
    protected StoryListItem onWrapItem(@NonNull final Story item) {
        Contracts.requireNonNull(item, "item == null");

        return new ModelStoryListItem(item);
    }

    @NonNull
    private final BaseEvent<StoryContentEventArgs> _editStory = new BaseEvent<>();

    private final View.OnClickListener _editStoryOnClick = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            final long storyId = (long) v.getTag(R.id.holder_story_id);
            _editStory.rise(new StoryContentEventArgs(storyId));
        }
    };

    @NonNull
    private final BaseEvent<StoryContentEventArgs> _shareStory = new BaseEvent<>();

    private final View.OnClickListener _shareStoryOnClick = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            final long storyId = (long) v.getTag(R.id.holder_story_id);
            _shareStory.rise(new StoryContentEventArgs(storyId));
        }
    };

    @NonNull
    private final BaseEvent<StoryContentEventArgs> _viewStory = new BaseEvent<>();

    private final View.OnClickListener _viewStoryOnClick = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            final long storyId = (long) v.getTag(R.id.holder_story_id);
            _viewStory.rise(new StoryContentEventArgs(storyId));
        }
    };
}
