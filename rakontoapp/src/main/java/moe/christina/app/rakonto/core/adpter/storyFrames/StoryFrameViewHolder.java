package moe.christina.app.rakonto.core.adpter.storyFrames;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;

import moe.christina.common.contract.Contracts;
import moe.christina.common.extension.view.recyclerView.viewHolder.ExtendedRecyclerViewHolder;

public final class StoryFrameViewHolder extends ExtendedRecyclerViewHolder {
    @BindView(moe.christina.app.rakonto.R.id.story_frame_image)
    @NonNull
    public ImageView storyFrameImageView;

    @BindView(moe.christina.app.rakonto.R.id.story_frame_text)
    @NonNull
    public TextView storyFrameTextView;

    public StoryFrameViewHolder(@NonNull final View itemView) {
        super(Contracts.requireNonNull(itemView, "itemView == null"));

        bindViews();

        Contracts.requireNonNull(storyFrameImageView, "storyFrameImageView == null");
        Contracts.requireNonNull(storyFrameTextView, "storyFrameTextView == null");
    }
}
