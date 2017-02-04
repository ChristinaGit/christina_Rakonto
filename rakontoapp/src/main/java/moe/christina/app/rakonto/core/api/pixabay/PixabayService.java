package moe.christina.app.rakonto.core.api.pixabay;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import rx.Observable;

import moe.christina.app.rakonto.core.api.pixabay.serachImages.SearchImagesResponse;

import retrofit2.http.Field;
import retrofit2.http.GET;

public interface PixabayService {
    @GET
    Observable<SearchImagesResponse> searchImages(
        @NonNull @Field("key") String apiKey,
        @NonNull @Field("q") String query,
        @Nullable @Field("lang") String language,
        @Nullable @Field("response_group") String responseGroup,
        @Nullable @Field("image_type") String imageType,
        @Nullable @Field("orientation") String orientation,
        @Nullable @Field("category") String category,
        @Nullable @Field("min_width") Integer minWidth,
        @Nullable @Field("min_height") Integer minHeight,
        @Nullable @Field("editors_choice") Boolean editorsChoice,
        @Nullable @Field("safesearch") Boolean safeSearch,
        @Nullable @Field("order") String order,
        @Nullable @Field("page") Integer page,
        @Nullable @Field("per_page") Integer perPage,
        @Nullable @Field("callback") String callback,
        @Nullable @Field("pretty") Boolean pretty);
}
