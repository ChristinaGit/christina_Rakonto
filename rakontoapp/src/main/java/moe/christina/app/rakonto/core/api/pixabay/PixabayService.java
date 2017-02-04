package moe.christina.app.rakonto.core.api.pixabay;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import rx.Observable;

import moe.christina.app.rakonto.core.api.pixabay.serachImages.SearchImagesResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PixabayService {
    @GET(".")
    Call<SearchImagesResponse> searchImages(
        @NonNull @Query("key") String apiKey,
        @NonNull @Query("q") String query,
        @Nullable @Query("lang") String language,
        @Nullable @Query("response_group") String responseGroup,
        @Nullable @Query("image_type") String imageType,
        @Nullable @Query("orientation") String orientation,
        @Nullable @Query("category") String category,
        @Nullable @Query("min_width") Integer minWidth,
        @Nullable @Query("min_height") Integer minHeight,
        @Nullable @Query("editors_choice") Boolean editorsChoice,
        @Nullable @Query("safesearch") Boolean safeSearch,
        @Nullable @Query("order") String order,
        @Nullable @Query("page") Integer page,
        @Nullable @Query("per_page") Integer perPage,
        @Nullable @Query("callback") String callback,
        @Nullable @Query("pretty") Boolean pretty);
}
