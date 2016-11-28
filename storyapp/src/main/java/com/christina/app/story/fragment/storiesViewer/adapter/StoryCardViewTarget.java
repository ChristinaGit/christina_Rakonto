package com.christina.app.story.fragment.storiesViewer.adapter;

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

import lombok.val;

public final class StoryCardViewTarget extends BitmapImageViewTarget
    implements Palette.PaletteAsyncListener {
    public StoryCardViewTarget(@NonNull final StoryViewHolder holder) {
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

            _animateCardBackgroundColor(_holder.cardView, backgroundColor, animationDuration);
            _holder.storyNameView.setTextColor(titleColor);
            _holder.storyTextView.setTextColor(bodyColor);
            _holder.editStoryView.setTextColor(titleColor);
            _holder.shareStoryView.setTextColor(titleColor);
        }
    }

    @Override
    public void onResourceReady(
        final Bitmap resource, final GlideAnimation<? super Bitmap> glideAnimation) {
        super.onResourceReady(resource, glideAnimation);

        Palette.from(resource).generate(/*Listener*/ this);
    }

    @NonNull
    private final StoryViewHolder _holder;

    private void _animateCardBackgroundColor(
        @NonNull final CardView cardView, @ColorInt final int colorTo, final long duration) {
        final int colorFrom = cardView.getCardBackgroundColor().getDefaultColor();
        final val animation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        animation.setDuration(duration);
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(final ValueAnimator animation) {
                cardView.setCardBackgroundColor((int) animation.getAnimatedValue());
            }
        });
        animation.start();
    }
}
