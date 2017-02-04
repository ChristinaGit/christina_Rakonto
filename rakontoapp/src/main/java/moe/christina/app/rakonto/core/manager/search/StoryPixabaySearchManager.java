package moe.christina.app.rakonto.core.manager.search;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;

import moe.christina.app.rakonto.core.api.pixabay.PixabayService;
import moe.christina.app.rakonto.core.api.pixabay.serachImages.SearchImages;
import moe.christina.common.contract.Contracts;

import java.util.ArrayList;
import java.util.List;

@Accessors(prefix = "_")
public final class StoryPixabaySearchManager implements StorySearchManager {
    public static final String API_KEY_PIXABAY = "4384678-3cfff891bc64f5364be17dfa6";

    public StoryPixabaySearchManager(@NonNull final PixabayService pixabayService) {
        Contracts.requireNonNull(pixabayService, "pixabayService == null");

        _pixabayService = pixabayService;
    }

    @Nullable
    @Override
    public final List<StoryFrameImage> searchFrameImages(@NonNull final String query)
        throws Exception {

        // TODO: 1/30/2017 Handle locales.
        final val searchImagesResponse = getPixabayService().searchImages(
            API_KEY_PIXABAY,
            query,
            SearchImages.Lang.RUSSIAN.getParameterValue(),
            SearchImages.ResponseGroup.DEFAULT.getParameterValue(),
            SearchImages.ImageType.DEFAULT.getParameterValue(),
            SearchImages.Orientation.HORIZONTAL.getParameterValue(),
            null,
            null,
            null,
            null,
            false,
            null,
            1,
            10,
            null,
            null).execute().body();

        final val images = searchImagesResponse.getImages();

        final List<StoryFrameImage> storyFrameImages;
        if (images != null) {
            storyFrameImages = new ArrayList<>(images.size());

            for (final val image : images) {
                final val uri = image.getWebUri();
                final val previewUri = image.getPreviewUri();

                if (uri != null && previewUri != null) {
                    storyFrameImages.add(new StoryFrameImage(uri, previewUri));
                }
            }
        } else {
            storyFrameImages = null;
        }

        return storyFrameImages;
    }

    @Getter(AccessLevel.PROTECTED)
    @NonNull
    private final PixabayService _pixabayService;
}
