package com.christina.storymaker.image_search;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.christina.common.AsyncCallback;

import java.util.ArrayList;
import java.util.List;

public class ImageSearcher {
    public void find(@NonNull final String text,
                     @NonNull final AsyncCallback<List<Uri>, Exception> callback) {
        final List<Uri> result = new ArrayList<>();
        result.add(
            Uri.parse("http://fullhdpictures.com/wp-content/uploads/2015/10/Sea-Images.jpg"));
        result.add(Uri.parse("http://www.villarsskirental.com/ski-hire-images/villars-bg-2.jpg"));
        callback.onSuccess(result);
    }
}