package moe.christina.app.rakonto.core.api.pixabay.serachImages;

import android.support.annotation.NonNull;

import lombok.Getter;
import lombok.experimental.Accessors;

import moe.christina.common.contract.Contracts;

public final class SearchImages {
    private SearchImages() {
        Contracts.unreachable();
    }

    @Accessors(prefix = "_")
    public enum Category {
        FASHION("fashion"),
        NATURE("nature"),
        BACKGROUNDS("backgrounds"),
        SCIENCE("science"),
        EDUCATION("education"),
        PEOPLE("people"),
        FEELINGS("feelings"),
        RELIGION("religion"),
        HEALTH("health"),
        PLACES("places"),
        ANIMALS("animals"),
        INDUSTRY("industry"),
        FOOD("food"),
        COMPUTER("computer"),
        SPORTS("sports"),
        TRANSPORTATION("transportation"),
        TRAVEL("travel"),
        BUILDINGS("buildings"),
        BUSINESS("business"),
        MUSIC("music");

        @Getter
        private final String _parameterValue;

        Category(@NonNull final String parameterValue) {
            Contracts.requireNonNull(parameterValue, "parameterValue == null");

            _parameterValue = parameterValue;
        }
    }

    @Accessors(prefix = "_")
    public enum ImageType {
        DEFAULT("all"),
        ALL("all"),
        PHOTO("photo"),
        ILLUSTRATION("illustration"),
        VECTOR("vector");

        @Getter
        private final String _parameterValue;

        ImageType(@NonNull final String parameterValue) {
            Contracts.requireNonNull(parameterValue, "parameterValue == null");

            _parameterValue = parameterValue;
        }
    }

    @Accessors(prefix = "_")
    public enum Lang {
        DEFAULT("en"),
        CZECH("cs"),
        DANISH("da"),
        GERMAN("de"),
        ENGLISH("en"),
        SPANISH("es"),
        FRENCH("fr"),
        INDONESIAN("id"),
        ITALIAN("it"),
        HUNGARIAN("hu"),
        DUTCH("nl"),
        NORWEGIAN("no"),
        POLISH("pl"),
        PORTUGUESE("pt"),
        ROMANIAN("ro"),
        SLOVAK("sk"),
        FINNISH("fi"),
        SWEDISH("sv"),
        TURKISH("tr"),
        VIETNAMESE("vi"),
        THAI("th"),
        BULGARIAN("bg"),
        RUSSIAN("ru"),
        GREEK("el"),
        JAPANESE("ja"),
        KOREAN("ko"),
        CHINESE("zh");

        @Getter
        private final String _parameterValue;

        Lang(@NonNull final String parameterValue) {
            Contracts.requireNonNull(parameterValue, "parameterValue == null");

            _parameterValue = parameterValue;
        }
    }

    @Accessors(prefix = "_")
    public enum Order {
        DEFAULT("popular"),
        POPULAR("popular"),
        LATEST("latest");

        @Getter
        private final String _parameterValue;

        Order(@NonNull final String parameterValue) {
            Contracts.requireNonNull(parameterValue, "parameterValue == null");

            _parameterValue = parameterValue;
        }
    }

    @Accessors(prefix = "_")
    public enum Orientation {
        DEFAULT("all"),
        ALL("all"),
        HORIZONTAL("horizontal"),
        VERTICAL("vertical");

        @Getter
        private final String _parameterValue;

        Orientation(@NonNull final String parameterValue) {
            Contracts.requireNonNull(parameterValue, "parameterValue == null");

            _parameterValue = parameterValue;
        }
    }

    @Accessors(prefix = "_")
    public enum ResponseGroup {
        DEFAULT("image_details"),
        IMAGE_DETAILS("image_details"),
        HIGH_RESOLUTION("high_resolution");

        @Getter
        private final String _parameterValue;

        ResponseGroup(@NonNull final String parameterValue) {
            Contracts.requireNonNull(parameterValue, "parameterValue == null");

            _parameterValue = parameterValue;
        }
    }
}
