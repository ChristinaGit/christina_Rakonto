package moe.christina.app.rakonto.screen.fragment.storiesList;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import org.parceler.Parcel;

@Parcel
@Accessors(prefix = "_")
/*package-private*/ final class StoriesListState {

    @Getter
    @Setter
    /*package-private*/ int _scrollPosition = 0;

    @Getter
    @Setter
    /*package-private*/ boolean _scrollPositionRestored = false;
}
