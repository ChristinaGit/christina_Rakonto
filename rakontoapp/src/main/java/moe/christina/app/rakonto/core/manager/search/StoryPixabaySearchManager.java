package moe.christina.app.rakonto.core.manager.search;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;

import rx.Observable;

import moe.christina.app.rakonto.core.api.pixabay.PixabayService;
import moe.christina.common.contract.Contracts;

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
    public final Observable<List<StoryFrameImage>> searchFrameImages(@NonNull final String query)
        throws Exception {

        return null;

        // TODO: 1/30/2017 Handle locales.
        //        return getPixabayService().searchImages(
        //            API_KEY_PIXABAY,
        //            query,
        //            SearchImages.Lang.RUSSIAN.getParameterValue(),
        //            null,
        //            null,
        //            null,
        //            null,
        //            null,
        //            null,
        //            null,
        //            null,
        //            null,
        //            null,
        //            null,
        //            null,
        //            null).flatMap(new Func1<SearchImagesResponse, SearchImagesResponse.Image>() {
        //            @Override
        //            public SearchImagesResponse.Image call(
        //                final SearchImagesResponse searchImagesResponse) {
        //                return searchImagesResponse.getImages();
        //            }
        //        }).filter(new Func1<Tuple2<String, String>, Boolean>() {
        //            @Override
        //            public Boolean call(final Tuple2<String, String> arg) {
        //                final val previewUri = arg.getFirst();
        //                final val uri = arg.getSecond();
        //
        //                return previewUri != null && uri != null;
        //            }
        //        }).map(new Func1<Tuple2<String, String>, StoryFrameImage>() {
        //            @Override
        //            public StoryFrameImage call(final Tuple2<String, String> arg) {
        //                final val previewUri = arg.getFirst();
        //                final val uri = arg.getSecond();
        //
        //                return new StoryFrameImage(previewUri, uri);
        //            }
        //        });
    }

    @Getter(AccessLevel.PROTECTED)
    @NonNull
    private final PixabayService _pixabayService;
}
