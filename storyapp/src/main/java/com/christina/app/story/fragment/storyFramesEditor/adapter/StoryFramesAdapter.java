package com.christina.app.story.fragment.storyFramesEditor.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.christina.api.story.model.StoryFrame;
import com.christina.app.story.R;
import com.christina.common.contract.Contracts;
import com.christina.common.view.recyclerView.adapter.BaseRecyclerViewAdapter;

import java.util.Objects;

public final class StoryFramesAdapter
    extends BaseRecyclerViewAdapter<StoryFrame, StoryFrameListItem, StoryFrameViewHolder> {
    @Nullable
    public final String getStoryText() {
        return _storyText;
    }

    public final void setStoryText(@Nullable final String storyText) {
        if (!Objects.equals(_storyText, storyText)) {
            _storyText = storyText;

            notifyDataSetChanged();
        }
    }

    @Override
    public StoryFrameViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view =
            inflater.inflate(R.layout.fragment_story_frames_editor_item, parent, false);
        return new StoryFrameViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(
        @NonNull final StoryFrameViewHolder holder,
        @NonNull final StoryFrameListItem listItem,
        final int position) {
        Contracts.requireNonNull(holder, "holder == null");
        Contracts.requireNonNull(listItem, "listItem == null");

        final String storyFrameText;

        final String storyText = getStoryText();
        if (!TextUtils.isEmpty(storyText)) {
            final int storyTextLength = storyText.length();

            final StoryFrame storyFrame = listItem.getStoryFrame();

            final int textStart = storyFrame.getTextStartPosition();
            final int textEnd = storyFrame.getTextEndPosition();
            if (storyTextLength >= textEnd && textEnd > textStart) {
                storyFrameText = storyText.substring(textStart, textEnd);
            } else {
                storyFrameText = null;
            }
        } else {
            storyFrameText = null;
        }

        holder.storyFrameTextView.setText(storyFrameText);
    }

    @NonNull
    @Override
    protected StoryFrameListItem onWrapItem(@NonNull final StoryFrame item) {
        Contracts.requireNonNull(item, "item == null");

        return new StoryFrameListItem(item);
    }

    @Nullable
    private String _storyText;
}
