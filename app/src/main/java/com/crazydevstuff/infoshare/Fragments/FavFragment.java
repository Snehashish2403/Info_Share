package com.crazydevstuff.infoshare.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crazydevstuff.infoshare.Adapters.FavItemsAdapter;
import com.crazydevstuff.infoshare.Interfaces.ItemsAdapterActionListener;
import com.crazydevstuff.infoshare.Models.ProductModel;
import com.crazydevstuff.infoshare.R;

import java.util.List;


public class FavFragment extends Fragment implements ItemsAdapterActionListener {

    private RecyclerView favItemsRV;
    private FavItemsAdapter favItemsAdapter;
    private List<ProductModel> favProductsList;
    public FavFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_fav, container, false);

        favItemsRV = view.findViewById(R.id.favFragmentRV);
        return view;
    }

    @Override
    public void onViewClicked(int clickedViewId, int clickedItemPosition, String itemKey) {

    }

    @Override
    public void onViewLongClicked(int clickedViewId, int clickedItemPosition) {

    }
}