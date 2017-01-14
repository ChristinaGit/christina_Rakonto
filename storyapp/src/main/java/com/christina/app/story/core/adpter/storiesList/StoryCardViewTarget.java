package com.christina.app.story.core.adpter.storiesList;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.graphics.Palette;

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
    }

    @Override
    public void onGenerated(final Palette palette) {
        final Palette.Swatch swatch = palette.getMutedSwatch();
        if (swatch != null) {
            final int backgroundColor = swatch.getRgb();
            final int titleColor = swatch.getTitleTextColor();
            final int bodyColor = swatch.getBodyTextColor();

            final val holder = getHolder();

            holder.cardView.setCardBackgroundColor(backgroundColor);
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
}
