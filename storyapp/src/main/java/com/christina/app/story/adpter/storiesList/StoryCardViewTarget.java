package com.christina.app.story.adpter.storiesList;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.christina.common.contract.Contracts;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;

@Accessors(prefix = "_")
/*package-private*/ final class StoryCardViewTarget extends BitmapImageViewTarget
    implements Palette.PaletteAsyncListener {
    public StoryCardViewTarget(@NonNull final StoryViewHolder holder) {
        super(Contracts.requireNonNull(holder, "holder == null").storyPreviewView);

        _holder = holder;
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
                .getInteger(android.R.integer.config_shortAnimTime);

            _animateCardBackgroundColor(holder.cardView, backgroundColor, animationDuration);
            holder.storyNameView.setTextColor(titleColor);
            holder.storyTextView.setTextColor(bodyColor);
            holder.editStoryView.setTextColor(titleColor);
            holder.shareStoryView.setTextColor(titleColor);
        }
    }

    @Override
    public void onResourceReady(
        final Bitmap resource, final GlideAnimation<? super Bitmap> glideAnimation) {
        super.onResourceReady(resource, glideAnimation);

        Palette.from(resource).generate(/*Listener*/ this);
    }

    @Getter(AccessLevel.PRIVATE)
    @NonNull
    private final StoryViewHolder _holder;

    private void _animateCardBackgroundColor(
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
