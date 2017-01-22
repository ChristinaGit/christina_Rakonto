package com.christina.app.story.core.manager.search;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;

import com.christina.common.AsyncCallback;
import com.christina.common.contract.Contracts;
import com.christina.common.control.manager.task.TaskManager;
import com.christina.common.extension.asyncTask.AsyncTaskBase;

import java.util.ArrayList;
import java.util.List;

@Accessors(prefix = "_")
public final class GoogleStorySearchManager implements StorySearchManager {
    private static final String GOOGLE_CUSTOM_SEARCH_API_KEY =
        "AIzaSyAlZf5tERsE4MOmmqPGShwpQcI9oKIiZ-0";

    private static final String GOOGLE_CUSTOM_SEARCH_SEARCH_ENGINE_ID =
        "016882590624886787945:v-p_3x2zyka";

    public GoogleStorySearchManager(@NonNull final TaskManager taskManager) {
        Contracts.requireNonNull(taskManager, "taskManager == null");

        _taskManager = taskManager;
    }

    @Override
    public void search(
        @NonNull final String query,
        @NonNull final AsyncCallback<List<String>, Exception> callback) {
        Contracts.requireNonNull(query, "query == null");
        Contracts.requireNonNull(callback, "callback == null");

        final val searchAsyncTask = new GoogleSearchAsyncTask(query, callback);
        searchAsyncTask.executeOnExecutor(getTaskManager().getIOExecutor());
    }

    @Getter(AccessLevel.PROTECTED)
    @NonNull
    private final TaskManager _taskManager;

    @Accessors(prefix = "_")
    private static final class GoogleSearchAsyncTask
        extends AsyncTaskBase<List<String>, Exception> {

        public GoogleSearchAsyncTask(
            @NonNull final String query,
            @NonNull final AsyncCallback<List<String>, Exception> asyncCallback) {
            super(Contracts.requireNonNull(asyncCallback, "asyncCallback == null"));
            Contracts.requireNonNull(query, "query == null");

            _query = query;
        }

        @NonNull
        @Override
        protected Exception convertExceptionToError(@NonNull final Exception e) {
            Contracts.requireNonNull(e, "e == null");

            return e;
        }

        @Nullable
        @Override
        protected List<String> doInBackground()
            throws Exception {
            final List<String> results;

            final val customsearch =
                new Customsearch(new NetHttpTransport(), new JacksonFactory(), null);

            final val list = customsearch.cse().list(getQuery());

            list.setKey(GOOGLE_CUSTOM_SEARCH_API_KEY);
            list.setCx(GOOGLE_CUSTOM_SEARCH_SEARCH_ENGINE_ID);
            list.setSearchType("image");
            list.setImgSize("large");
            list.setSafe("off");
            //TODO: Adjust locale.
            //list.setHl(Locale.getDefault().get);
            list.setFilter("1");
            list.setImgType("photo");

            final val resultList = list.execute().getItems();

            if (resultList != null) {
                results = new ArrayList<>(resultList.size());
                for (final val result : resultList) {
                    results.add(result.getLink());
                }
            } else {
                results = null;
            }

            return results;
        }

        @Getter(AccessLevel.PROTECTED)
        @NonNull
        private final String _query;
    }
}
