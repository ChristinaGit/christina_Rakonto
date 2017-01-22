package com.christina.app.story.core.adpter.storiesList;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import com.christina.common.contract.Contracts;

@Accessors(prefix = "_")
/*package-private*/ final class StoryCardViewTarget extends BitmapImageViewTarget
    implements Palette.PaletteAsyncListener {
    public StoryCardViewTarget(@NonNull final StoryViewHolder holder) {
        super(Contracts.requireNonNull(holder, "holder == null").storyPreviewView);

        _holder = holder;

        _originalCardBackgroundColor = holder.cardView.getCardBackgroundColor().getDefaultColor();
        _originalEditStoryTextColor = holder.editStoryView.getTextColors().getDefaultColor();
        _originalShareStoryTextColor = holder.shareStoryView.getTextColors().getDefaultColor();
        _originalStoryNameTextColor = holder.storyNameView.getTextColors().getDefaultColor();
        _originalStoryTextTextColor = holder.storyTextView.getTextColors().getDefaultColor();
    }

    @Override
    public void onGenerated(final Palette palette) {
        final Palette.Swatch swatch = palette.getMutedSwatch();
        if (swatch != null) {
            final int backgroundColor = swatch.getRgb();
            final int titleColor = swatch.getTitleTextColor();
            final int bodyColor = swatch.getBodyTextColor();

            final val holder = getHolder();

            final int animationDuration = holder
                .getContext()
                .getResources()
                .getInteger(android.R.integer.config_longAnimTime);

            animateCardBackgroundColor(holder.cardView, backgroundColor, animationDuration);

            holder.storyNameView.setTextColor(titleColor);
            holder.storyTextView.setTextColor(bodyColor);
            holder.editStoryView.setTextColor(titleColor);
            holder.shareStoryView.setTextColor(titleColor);
        }
    }

    @Override
    public void onLoadFailed(final Exception e, final Drawable errorDrawable) {
        super.onLoadFailed(e, errorDrawable);

        final val holder = getHolder();

        holder.cardView.setCardBackgroundColor(_originalCardBackgroundColor);

        holder.storyNameView.setTextColor(_originalStoryNameTextColor);
        holder.storyTextView.setTextColor(_originalStoryTextTextColor);
        holder.editStoryView.setTextColor(_originalEditStoryTextColor);
        holder.shareStoryView.setTextColor(_originalShareStoryTextColor);
    }

    @Override
    public void onResourceReady(
        final Bitmap resource, final GlideAnimation<? super Bitmap> glideAnimation) {
        super.onResourceReady(resource, glideAnimation);

        Palette.from(resource).generate(/*Listener*/ this);
    }

    @Getter(AccessLevel.PROTECTED)
    @NonNull
    private final StoryViewHolder _holder;

    @Getter(AccessLevel.PROTECTED)
    @ColorInt
    private final int _originalCardBackgroundColor;

    @Getter(AccessLevel.PROTECTED)
    @ColorInt
    private final int _originalEditStoryTextColor;

    @Getter(AccessLevel.PROTECTED)
    @ColorInt
    private final int _originalShareStoryTextColor;

    @Getter(AccessLevel.PROTECTED)
    @ColorInt
    private final int _originalStoryNameTextColor;

    @Getter(AccessLevel.PROTECTED)
    @ColorInt
    private final int _originalStoryTextTextColor;

    private void animateCardBackgroundColor(
        @NonNull final CardView cardView, @ColorInt final int colorTo, final long duration) {
        final int colorFrom = cardView.getCardBackgroundColor().getDefaultColor();
        final val animation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        animation.setDuration(duration);
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(final ValueAnimator a) {
                cardView.setCardBackgroundColor((int) a.getAnimatedValue());
            }
        });
        animation.start();
    }
}
