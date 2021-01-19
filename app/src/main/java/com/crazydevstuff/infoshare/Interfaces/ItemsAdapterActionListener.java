package com.crazydevstuff.infoshare.Interfaces;

public interface ItemsAdapterActionListener {
    void onViewClicked(int clickedViewId, int clickedItemPosition, String itemKey);
    void onViewLongClicked(int clickedViewId, int clickedItemPosition);
}
