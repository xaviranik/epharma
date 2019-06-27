package com.triamatter.epharma.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.triamatter.epharma.R;
import com.triamatter.epharma.activities.MainActivity;
import com.triamatter.epharma.network.KEYS;
import com.triamatter.epharma.utils.Utils;

public class ProductFragment extends Fragment {

    private String productName;
    private float productPrice;

    private TextView textViewProductName;
    private TextView textViewProductPrice;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        init(view);
        ((MainActivity)getActivity()).setAppTitle("Product Name");
        return view;
    }

    private void init(View view)
    {
        textViewProductName = (TextView) view.findViewById(R.id.textView_product_name);
        textViewProductPrice = (TextView) view.findViewById(R.id.textView_product_price);

        getArgumentsFromPreviousFragment();
    }

    private void getArgumentsFromPreviousFragment()
    {
        if(getArguments() != null)
        {
            productName = getArguments().getString(KEYS.PRODUCT_NAME);
            productPrice = getArguments().getFloat(KEYS.PRODUCT_PRICE);

            textViewProductName.setText(productName);
            textViewProductPrice.setText(Utils.formatPrice(productPrice));
        }
    }

}
