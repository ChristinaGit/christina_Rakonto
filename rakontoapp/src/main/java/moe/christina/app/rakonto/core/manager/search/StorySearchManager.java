package moe.christina.app.rakonto.core.manager.search;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import rx.Observable;

import java.util.List;

public interface StorySearchManager {
    @WorkerThread
    @Nullable
    Observable<List<StoryFrameImage>> searchFrameImages(@NonNull String query)
        throws Exception;
}
