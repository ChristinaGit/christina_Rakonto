package moe.christina.app.rakonto.core.api.pixabay.serachImages;

import android.support.annotation.Nullable;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@ToString(doNotUseGetters = true)
@Accessors(prefix = "_")
public final class SearchImagesResponse {
    @Getter
    @Nullable
    @SerializedName("hits")
    private List<Image> _images;

    @Getter
    @SerializedName("total")
    private int _total;

    @Getter
    @SerializedName("totalHits")
    private int _totalQuery;

    @ToString(doNotUseGetters = true)
    @Accessors(prefix = "_")
    public static final class Image {
        @Getter
        @SerializedName("comments")
        private long _commentsCount;

        @Getter
        @SerializedName("downloads")
        private long _downloadsCount;

        @Getter
        @SerializedName("favorites")
        private long _favoritesCount;

        @Getter
        @Nullable
        @SerializedName("imageHeight")
        private String _height;

        @Getter
        @SerializedName("id")
        private long _id;

        @Getter
        @SerializedName("likes")
        private long _likesCount;

        @Getter
        @Nullable
        @SerializedName("previewHeight")
        private String _previewHeight;

        @Getter
        @Nullable
        @SerializedName("pageURL")
        private String _previewUri;

        @Getter
        @Nullable
        @SerializedName("previewWidth")
        private String _previewWidth;

        @Getter
        @Nullable
        @SerializedName("tags")
        private String _tags;

        @Getter
        @Nullable
        @SerializedName("type")
        private String _type;

        @Getter
        @Nullable
        @SerializedName("previewURL")
        private String _uri;

        @Getter
        @SerializedName("user_id")
        private long _userId;

        @Getter
        @SerializedName("userImageURL")
        private long _userImageUri;

        @Getter
        @SerializedName("user")
        private long _userName;

        @Getter
        @SerializedName("views")
        private long _viewsCount;

        @Getter
        @Nullable
        @SerializedName("webformatHeight")
        private String _webHeight;

        @Getter
        @Nullable
        @SerializedName("webformatURL")
        private String _webUri;

        @Getter
        @Nullable
        @SerializedName("webformatWidth")
        private String _webWidth;

        @Getter
        @Nullable
        @SerializedName("imageWidth")
        private String _width;
    }
}
