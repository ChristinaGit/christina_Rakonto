package com.christina.storymaker.storiesViewer.adapter;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.support.annotation.NonNull;
import android.support.v7.graphics.Palette;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.christina.common.contract.Contracts;
import com.christina.common.event.BaseEvent;
import com.christina.common.event.Event;
import com.christina.common.view.recyclerView.AbstractRecyclerViewAdapter;
import com.christina.common.view.recyclerView.ListItem;
import com.christina.content.story.model.Story;
import com.christina.storymaker.R;
import com.christina.storymaker.core.StoryContentEventArgs;

public final class StoryListAdapter
    extends AbstractRecyclerViewAdapter<Story, StoryListViewHolder> {

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
    public StoryListViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(R.layout.story_list_viewer_item, parent, false);
        return new StoryListViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull final StoryListViewHolder holder,
                                    @NonNull final ListItem listItem, final int position) {
        Contracts.requireNonNull(holder, "holder == null");
        Contracts.requireNonNull(listItem, "listItem == null");

        if (listItem instanceof StoryListItem) {
            final Story story = ((StoryListItem) listItem).story;

            final String storyName = story.getName();
            if (!TextUtils.isEmpty(storyName)) {
                holder.storyNameView.setVisibility(View.VISIBLE);
                holder.storyNameView.setText(storyName);
            } else {
                holder.storyNameView.setVisibility(View.GONE);
                holder.storyNameView.setText(null);
            }

            final String storyText = story.getText();
            if (!TextUtils.isEmpty(storyText)) {
                holder.storyTextView.setVisibility(View.VISIBLE);
                holder.storyTextView.setText(storyText);
            } else {
                holder.storyTextView.setVisibility(View.GONE);
                holder.storyTextView.setText(null);
            }

            holder.cardView.setTag(R.id.holder_story_id, story.getId());
            holder.cardView.setOnClickListener(_viewStoryOnClick);

            holder.shareStoryView.setTag(R.id.holder_story_id, story.getId());
            holder.shareStoryView.setOnClickListener(_shareStoryOnClick);

            holder.editStoryView.setTag(R.id.holder_story_id, story.getId());
            holder.editStoryView.setOnClickListener(_editStoryOnClick);

            Glide.with(holder.getContext())
                 .load(story.getPreviewUri())
                 .asBitmap()
                 .centerCrop()
                 .into(new StoryCardViewTarget(holder))
                 .getRequest()
                 .begin();
        } else {
            throw new IllegalArgumentException(
                "Unsupported list item type: " + listItem.getClass());
        }
    }

    @NonNull
    @Override
    protected ListItem onWrapItem(@NonNull final Story story) {
        Contracts.requireNonNull(story, "story == null");

        return new StoryListItem(story);
    }

    @NonNull
    private final BaseEvent<StoryContentEventArgs> _editStory = new BaseEvent<>();

    private final View.OnClickListener _editStoryOnClick = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            final long storyId = (long) v.getTag(R.id.holder_story_id);
            _editStory.onEvent(new StoryContentEventArgs(storyId));
        }
    };

    @NonNull
    private final BaseEvent<StoryContentEventArgs> _shareStory = new BaseEvent<>();

    private final View.OnClickListener _shareStoryOnClick = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            final long storyId = (long) v.getTag(R.id.holder_story_id);
            _shareStory.onEvent(new StoryContentEventArgs(storyId));
        }
    };

    @NonNull
    private final BaseEvent<StoryContentEventArgs> _viewStory = new BaseEvent<>();

    private final View.OnClickListener _viewStoryOnClick = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            final long storyId = (long) v.getTag(R.id.holder_story_id);
            _viewStory.onEvent(new StoryContentEventArgs(storyId));
        }
    };

    private static final class StoryCardViewTarget extends BitmapImageViewTarget
        implements Palette.PaletteAsyncListener {
        public StoryCardViewTarget(@NonNull final StoryListViewHolder holder) {
            super(holder.storyPreviewView);
            _holder = holder;

            Contracts.requireNonNull(holder, "holder == null");
        }

        @Override
        public void onGenerated(final Palette palette) {
            final Palette.Swatch swatch = palette.getMutedSwatch();
            if (swatch != null) {
                final int backgroundColor = swatch.getRgb();
                final int titleColor = swatch.getTitleTextColor();
                final int bodyColor = swatch.getBodyTextColor();

                _holder.cardView.setCardBackgroundColor(backgroundColor);
                _holder.storyNameView.setTextColor(titleColor);
                _holder.storyTextView.setTextColor(bodyColor);
                _holder.editStoryView.setTextColor(titleColor);
                _holder.shareStoryView.setTextColor(titleColor);

                _holder.cardView.setBackground(_createCardViewBackground(titleColor));
            }
        }

        @Override
        public void onResourceReady(final Bitmap resource,
                                    final GlideAnimation<? super Bitmap> glideAnimation) {
            super.onResourceReady(resource, glideAnimation);

            Palette.from(resource).generate(this);
        }

        @NonNull
        private final StoryListViewHolder _holder;

        private Drawable _createCardViewBackground(final int color) {
            return new RippleDrawable(ColorStateList.valueOf(color),
                                      _holder.cardView.getBackground(),
                                      _holder.cardView.getBackground());
        }
    }
}
