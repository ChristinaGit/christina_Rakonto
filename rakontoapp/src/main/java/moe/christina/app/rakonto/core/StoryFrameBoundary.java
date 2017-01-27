package moe.christina.app.rakonto.core;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
@ToString
public final class StoryFrameBoundary {
    public StoryFrameBoundary(final int start, final int end) {
        _start = start;
        _end = end;
    }

    @Getter
    private final int _end;

    @Getter
    private final int _start;
}
