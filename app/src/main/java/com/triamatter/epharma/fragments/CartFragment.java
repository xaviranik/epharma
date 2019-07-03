package com.triamatter.epharma.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.carteasy.v1.lib.Carteasy;
import com.triamatter.epharma.R;
import com.triamatter.epharma.activities.MainActivity;
import com.triamatter.epharma.adapter.CartAdapter;
import com.triamatter.epharma.adapter.ProductAdapter;
import com.triamatter.epharma.model.Product;
import com.triamatter.epharma.network.web.KEYS;
import com.triamatter.epharma.utils.EmptyRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CartFragment extends Fragment {

    private EmptyRecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private List<Product> productList;

    private TextView textViewEmpty;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        ((MainActivity)getActivity()).setAppTitle("My Cart");
        init(view);
        return view;
    }

    private void init(View view)
    {
        cartRecyclerView = (EmptyRecyclerView) view.findViewById(R.id.product_recyclerView_cart);
        textViewEmpty = (TextView) view.findViewById(R.id.emptyView);

        setupCartRecyclerView();
    }

    private void setupCartRecyclerView()
    {
        productList =  new ArrayList<>();

        Map<Integer, Map> data;
        Carteasy cs = new Carteasy();
        data = cs.ViewAll(getContext());

        for (Map.Entry<Integer, Map> entry : data.entrySet())
        {
            Map<String, String> innerdata = entry.getValue();
            String productID = innerdata.get(KEYS.PRODUCT_ID);
            String productName = innerdata.get(KEYS.PRODUCT_NAME);
            String productPrice = innerdata.get(KEYS.PRODUCT_PRICE);
            String productQuantity = innerdata.get(KEYS.PRODUCT_QUANTITY);

            Product product = new Product(Integer.valueOf(productID), productName, Float.valueOf(productPrice), Integer.valueOf(productQuantity));
            productList.add(product);
        }

        cartAdapter = new CartAdapter(productList, getContext());

        cartRecyclerView.setHasFixedSize(true);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartRecyclerView.setAdapter(cartAdapter);

        cartRecyclerView.setEmptyView(textViewEmpty);
    }
}
