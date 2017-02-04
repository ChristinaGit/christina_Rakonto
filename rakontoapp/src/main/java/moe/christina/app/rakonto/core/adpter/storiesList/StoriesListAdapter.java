package moe.christina.app.rakonto.core.adpter.storiesList;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.val;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import moe.christina.app.rakonto.R;
import moe.christina.app.rakonto.core.eventArgs.StoryEventArgs;
import moe.christina.app.rakonto.model.ui.UIStory;
import moe.christina.common.contract.Contracts;
import moe.christina.common.event.Events;
import moe.christina.common.event.generic.Event;
import moe.christina.common.event.generic.ManagedEvent;
import moe.christina.common.extension.delegate.loading.LoadingViewDelegate;
import moe.christina.common.extension.delegate.loading.ProgressVisibilityHandler;
import moe.christina.common.extension.view.recyclerView.adapter.RecyclerViewListAdapter;

import java.util.List;

@Accessors(prefix = "_")
public final class StoriesListAdapter extends RecyclerViewListAdapter<UIStory, StoryViewHolder> {
    public static long getStoryId(@NonNull final RecyclerView.ViewHolder holder) {
        Contracts.requireNonNull(holder, "holder == null");

        return (long) holder.itemView.getTag(R.id.tag_story_id);
    }

    public StoriesListAdapter() {
        setHasStableIds(true);
    }

    @NonNull
    public final Event<StoryEventArgs> getEditStoryEvent() {
        return _editStoryEvent;
    }

    @NonNull
    public final Event<StoryEventArgs> getShareStoryEvent() {
        return _shareStoryEvent;
    }

    @NonNull
    public final Event<StoryEventArgs> getViewStoryEvent() {
        return _viewStoryEvent;
    }

    @Override
    public StoryViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final val inflater = LayoutInflater.from(parent.getContext());
        final val view = inflater.inflate(R.layout.fragment_stories_list_item, parent, false);
        final val holder = new StoryViewHolder(view);

        final val cardViewTarget = new StoryCardViewTarget(holder);
        final val loadingViewDelegate = LoadingViewDelegate
            .builder()
            .setContentView(holder.storyPreviewView)
            .setLoadingView(holder.storyPreviewLoadingView)
            .setErrorView(holder.storyLoadFailView)
            .setLoadingVisibilityHandler(getStoryPreviewLoadingVisibilityHandler())
            .build();
        final val storyPreviewLoadingListener =
            new StoryPreviewRequestListener(loadingViewDelegate);

        final val storyViewCache =
            new StoryViewCache(cardViewTarget, loadingViewDelegate, storyPreviewLoadingListener);
        holder.itemView.setTag(R.id.tag_recycler_cache, storyViewCache);

        return holder;
    }

    @Override
    public final long getItemId(final int position) {
        return getItem(position).getId();
    }

    @Override
    protected void onBindViewHolder(
        @NonNull final StoryViewHolder holder, @NonNull final UIStory item, final int position) {
        super.onBindViewHolder(
            Contracts.requireNonNull(holder, "holder == null"),
            Contracts.requireNonNull(item, "item == null"),
            position);

        final val storyViewCache = (StoryViewCache) holder.itemView.getTag(R.id.tag_recycler_cache);

        final val loadingViewDelegate = storyViewCache.getLoadingViewDelegate();
        loadingViewDelegate.setLoadingVisible(false);
        holder.storyPreviewLoadingView.reset(false);
        loadingViewDelegate.showLoading();

        holder.itemView.setTag(R.id.tag_story_id, item.getId());

        holder.cardView.setTag(R.id.tag_story_id, item.getId());
        holder.cardView.setOnClickListener(_viewStoryOnClick);

        holder.shareStoryView.setTag(R.id.tag_story_id, item.getId());
        holder.shareStoryView.setOnClickListener(_shareStoryOnClick);

        holder.editStoryView.setTag(R.id.tag_story_id, item.getId());
        holder.editStoryView.setOnClickListener(_editStoryOnClick);

        final val storyName = item.getName();
        holder.storyNameView.setText(storyName);
        holder.storyNameView.setVisibility(!TextUtils.isEmpty(storyName)
                                           ? View.VISIBLE
                                           : View.GONE);

        final val storyText = item.getText();
        holder.storyTextView.setText(storyText);
        holder.storyTextView.setVisibility(!TextUtils.isEmpty(storyText)
                                           ? View.VISIBLE
                                           : View.GONE);

        holder.storyLoadRetryView.setTag(R.id.tag_adapter_position, holder.getAdapterPosition());
        holder.storyLoadRetryView.setOnClickListener(_retryLoadStoryPreviewOnClick);

        Glide
            .with(holder.getContext())
            .load(item.getPreviewUri())
            .asBitmap()
            .listener(storyViewCache.getLoadingListener())
            .animate(R.anim.fade_in_short)
            .centerCrop()
            .into(storyViewCache.getViewTarget());
    }

    @NonNull
    private final ManagedEvent<StoryEventArgs> _editStoryEvent = Events.createEvent();

    private final View.OnClickListener _editStoryOnClick = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            final long storyId = (long) v.getTag(R.id.tag_story_id);
            _editStoryEvent.rise(new StoryEventArgs(storyId));
        }
    };

    @NonNull
    private final View.OnClickListener _retryLoadStoryPreviewOnClick = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            final int adapterPosition = (int) v.getTag(R.id.tag_adapter_position);
            notifyItemChanged(adapterPosition);
        }
    };

    @NonNull
    private final ManagedEvent<StoryEventArgs> _shareStoryEvent = Events.createEvent();

    @NonNull
    private final View.OnClickListener _shareStoryOnClick = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            final long storyId = (long) v.getTag(R.id.tag_story_id);
            _shareStoryEvent.rise(new StoryEventArgs(storyId));
        }
    };

    @Getter(AccessLevel.PROTECTED)
    @NonNull
    private final ProgressVisibilityHandler _storyPreviewLoadingVisibilityHandler =
        new ProgressVisibilityHandler();

    @NonNull
    private final ManagedEvent<StoryEventArgs> _viewStoryEvent = Events.createEvent();

    @NonNull
    private final View.OnClickListener _viewStoryOnClick = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            final long storyId = (long) v.getTag(R.id.tag_story_id);
            _viewStoryEvent.rise(new StoryEventArgs(storyId));
        }
    };

    @Getter(onMethod = @__(@Override))
    @Setter
    @Nullable
    private List<UIStory> _items;

    private static final class StoryPreviewRequestListener
        implements RequestListener<String, Bitmap> {
        public StoryPreviewRequestListener(
            @NonNull final LoadingViewDelegate loadingViewDelegate) {
            Contracts.requireNonNull(loadingViewDelegate, "loadingViewDelegate == null");

            _loadingViewDelegate = loadingViewDelegate;
        }

        @Override
        public final boolean onException(
            final Exception e,
            final String model,
            final Target<Bitmap> target,
            final boolean isFirstResource) {
            _loadingViewDelegate.showError();

            return false;
        }

        @Override
        public final boolean onResourceReady(
            final Bitmap resource,
            final String model,
            final Target<Bitmap> target,
            final boolean isFromMemoryCache,
            final boolean isFirstResource) {
            _loadingViewDelegate.showContent();

            return false;
        }

        @NonNull
        private final LoadingViewDelegate _loadingViewDelegate;
    }
}
