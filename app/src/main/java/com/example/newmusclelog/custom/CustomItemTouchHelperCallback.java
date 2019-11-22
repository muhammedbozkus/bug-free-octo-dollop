package com.example.newmusclelog.custom;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class CustomItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final ItemTouchHelperAdapter referenceToRecyclerViewAdapter;

    public CustomItemTouchHelperCallback(ItemTouchHelperAdapter referenceToRecyclerViewAdapter)
    {
        this.referenceToRecyclerViewAdapter = referenceToRecyclerViewAdapter;


    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull
            RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    // notify RecyclerViewAdapter that an item may have been moved
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull
            RecyclerView.ViewHolder sourceViewHolder, @NonNull RecyclerView.ViewHolder
                                  targetViewHolder1) {

        referenceToRecyclerViewAdapter.onItemMove(sourceViewHolder.getAdapterPosition(),
                targetViewHolder1.getAdapterPosition());

        return true;
    }

    // works for both left & right swipes since ignoring direction param
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        referenceToRecyclerViewAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }
}
