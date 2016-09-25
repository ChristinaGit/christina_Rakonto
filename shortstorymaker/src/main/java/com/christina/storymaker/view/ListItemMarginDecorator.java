package com.christina.storymaker.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ListItemMarginDecorator extends RecyclerView.ItemDecoration {
    public ListItemMarginDecorator(final int verticalMargin, final int horizontalMargin) {
        _horizontalMargin = horizontalMargin;
        _verticalMargin = verticalMargin;
    }

    @Override
    public void getItemOffsets(final Rect outRect, final View view, final RecyclerView parent,
                               final RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        outRect.bottom += _verticalMargin / 2;
        outRect.top += _verticalMargin / 2;

        outRect.left += _horizontalMargin / 2;
        outRect.right += _horizontalMargin / 2;
    }

    private final int _horizontalMargin;

    private final int _verticalMargin;
}
