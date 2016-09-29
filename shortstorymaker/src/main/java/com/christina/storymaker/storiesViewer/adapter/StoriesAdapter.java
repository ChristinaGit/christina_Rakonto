package com.christina.storymaker.storiesViewer.adapter;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

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

public final class StoriesAdapter
    extends AbstractRecyclerViewAdapter<Story, StoriesItemViewHolder> {

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
    public StoriesItemViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(R.layout.stories_viewer_item, parent, false);
        return new StoriesItemViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull final StoriesItemViewHolder holder,
        @NonNull final ListItem listItem, final int position) {
        Contracts.requireNonNull(holder, "holder == null");
        Contracts.requireNonNull(listItem, "listItem == null");

        if (listItem instanceof StoriesItem) {
            final Story story = ((StoriesItem) listItem).getStory();

            holder.storyNameView.setText(story.getName());
            holder.storyTextView.setText(story.getText());

            holder.cardView.setTag(R.id.holder_story_id, story.getId());
            holder.cardView.setOnClickListener(_viewStoryOnClick);

            holder.shareStoryView.setTag(R.id.holder_story_id, story.getId());
            holder.shareStoryView.setOnClickListener(_shareStoryOnClick);

            holder.editStoryView.setTag(R.id.holder_story_id, story.getId());
            holder.editStoryView.setOnClickListener(_editStoryOnClick);

            Glide
                .with(holder.getContext())
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

        return new StoriesItem(story);
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

    private static final class StoryCardViewTarget extends BitmapImageViewTarget
        implements Palette.PaletteAsyncListener {
        public StoryCardViewTarget(@NonNull final StoriesItemViewHolder holder) {
            super(holder.storyPreviewView);

            Contracts.requireNonNull(holder, "holder == null");

            _holder = holder;
        }

        @Override
        public void onGenerated(final Palette palette) {
            final Palette.Swatch swatch = palette.getMutedSwatch();
            if (swatch != null) {
                final int backgroundColor = swatch.getRgb();
                final int titleColor = swatch.getTitleTextColor();
                final int bodyColor = swatch.getBodyTextColor();

                final int animationDuration = _holder
                    .getContext()
                    .getResources()
                    .getInteger(android.R.integer.config_shortAnimTime);

                animateCardBackgroundColor(_holder.cardView, backgroundColor, animationDuration);
                _holder.storyNameView.setTextColor(titleColor);
                _holder.storyTextView.setTextColor(bodyColor);
                _holder.editStoryView.setTextColor(titleColor);
                _holder.shareStoryView.setTextColor(titleColor);
            }
        }

        @Override
        public void onResourceReady(final Bitmap resource,
            final GlideAnimation<? super Bitmap> glideAnimation) {
            super.onResourceReady(resource, glideAnimation);

            Palette.from(resource).generate(this);
        }

        @Override
        protected void setResource(final Bitmap resource) {
            super.setResource(resource);

            getView().startAnimation(getFadeInAnimation());
        }

        @NonNull
        private final StoriesItemViewHolder _holder;

        private void animateCardBackgroundColor(@NonNull final CardView cardView,
            @ColorInt final int colorTo, final long duration) {
            final int colorFrom = cardView.getCardBackgroundColor().getDefaultColor();
            final ValueAnimator animation =
                ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
            animation.setDuration(duration);
            animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(final ValueAnimator animator) {
                    cardView.setCardBackgroundColor((int) animator.getAnimatedValue());
                }
            });
            animation.start();
        }

        @NonNull
        private Animation getFadeInAnimation() {
            return AnimationUtils.loadAnimation(_holder.getContext(), android.R.anim.fade_in);
        }
    }
}
