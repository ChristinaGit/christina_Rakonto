package moe.christina.app.rakonto.core.adpter.storiesList;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;

import moe.christina.app.rakonto.R;
import moe.christina.common.contract.Contracts;
import moe.christina.common.extension.view.ContentLoaderProgressBar;
import moe.christina.common.extension.view.recyclerView.viewHolder.ExtendedRecyclerViewHolder;

@SuppressWarnings({"InstanceVariableNamingConvention", "NullableProblems"})
/*package-private*/ final class StoryViewHolder extends ExtendedRecyclerViewHolder {
    @BindView(R.id.story_card)
    @NonNull
    public CardView cardView;

    @BindView(R.id.edit_story)
    @NonNull
    public Button editStoryView;

    @BindView(R.id.share_story)
    @NonNull
    public Button shareStoryView;

    @BindView(R.id.story_load_fail)
    @NonNull
    public View storyLoadFailView;

    @BindView(R.id.story_load_retry)
    @NonNull
    public Button storyLoadRetryView;

    @BindView(R.id.story_name)
    @NonNull
    public TextView storyNameView;

    @BindView(R.id.story_preview_loading)
    @NonNull
    public ContentLoaderProgressBar storyPreviewLoadingView;

    @BindView(R.id.story_preview)
    @NonNull
    public ImageView storyPreviewView;

    @BindView(R.id.story_text)
    @NonNull
    public TextView storyTextView;

    public StoryViewHolder(@NonNull final View itemView) {
        super(Contracts.requireNonNull(itemView, "itemView == null"));

        bindViews();
    }
}
