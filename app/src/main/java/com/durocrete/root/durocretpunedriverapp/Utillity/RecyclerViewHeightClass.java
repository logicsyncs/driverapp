package com.durocrete.root.durocretpunedriverapp.Utillity;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewHeightClass {

    public static void setRecyclerViewHeightBasedOnChildren(RecyclerView recyclerView) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter == null) {
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(recyclerView.getWidth(), View.MeasureSpec.EXACTLY);

        for (int i = 0; i < adapter.getItemCount(); i++) {
            View listItem = adapter.onCreateViewHolder(recyclerView, adapter.getItemViewType(i)).itemView;
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
        params.height = totalHeight + (recyclerView.getPaddingTop() + recyclerView.getPaddingBottom());
        recyclerView.setLayoutParams(params);
        recyclerView.requestLayout();
    }
}
