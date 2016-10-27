package com.christina.app.story.fragment.storyTextPartsEditor.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.christina.app.story.R;
import com.christina.common.contract.Contracts;
import com.christina.common.view.recyclerView.BaseRecyclerViewAdapter;

public final class StoryTextPartsAdapter
    extends BaseRecyclerViewAdapter<String, StoryTextPartListItem, StoryTextPartViewHolder> {
    @Override
    public StoryTextPartViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view =
            inflater.inflate(R.layout.fragment_story_text_parts_editor_item, parent, false);
        return new StoryTextPartViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull final StoryTextPartViewHolder holder,
        @NonNull final StoryTextPartListItem listItem, final int position) {
        Contracts.requireNonNull(holder, "holder == null");
        Contracts.requireNonNull(listItem, "listItem == null");

        String rawStoryFrameText = listItem.getRawStoryFrameText();
        rawStoryFrameText = rawStoryFrameText.trim();
        holder.storyFrameTextView.setText(rawStoryFrameText);
    }

    @NonNull
    @Override
    protected StoryTextPartListItem onWrapItem(@NonNull final String item) {
        Contracts.requireNonNull(item, "item == null");

        return new StoryTextPartListItem(item);
    }
}
