package com.christina.app.story.core.manager.search;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import lombok.experimental.Accessors;
import lombok.val;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;

import com.christina.common.contract.Contracts;

import java.util.ArrayList;
import java.util.List;

@Accessors(prefix = "_")
public final class GoogleStorySearchManager implements StorySearchManager {
    private static final String GOOGLE_CUSTOM_SEARCH_API_KEY =
        "AIzaSyAlZf5tERsE4MOmmqPGShwpQcI9oKIiZ-0";

    private static final String GOOGLE_CUSTOM_SEARCH_SEARCH_ENGINE_ID =
        "016882590624886787945:v-p_3x2zyka";

    @WorkerThread
    @Nullable
    @Override
    public final List<String> search(@NonNull final String query)
        throws Exception {
        Contracts.requireNonNull(query, "query == null");

        final List<String> results;

        final val customsearch =
            new Customsearch(new NetHttpTransport(), new JacksonFactory(), null);

        final val list = customsearch.cse().list(query);

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
}
