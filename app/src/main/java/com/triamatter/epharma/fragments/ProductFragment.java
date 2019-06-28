package com.triamatter.epharma.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.triamatter.epharma.R;
import com.triamatter.epharma.activities.MainActivity;
import com.triamatter.epharma.network.web.KEYS;
import com.triamatter.epharma.utils.Utils;

public class ProductFragment extends Fragment implements View.OnClickListener{

    private String productName;
    private float productPrice;

    private TextView textViewProductName;
    private TextView textViewProductPrice;

    private ImageView buttonAddQuantity;
    private ImageView buttonRemoveQuantity;
    private TextView textViewQuantity;

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

        buttonAddQuantity = (ImageView) view.findViewById(R.id.button_add_quantity);
        buttonRemoveQuantity = (ImageView) view.findViewById(R.id.button_minus_quantity);
        textViewQuantity = (TextView) view.findViewById(R.id.textView_product_quantity);

        buttonAddQuantity.setOnClickListener((View.OnClickListener) this);
        buttonRemoveQuantity.setOnClickListener((View.OnClickListener) this);

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

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.button_add_quantity:
            {
                manageQuantity(true);
                break;
            }
            case R.id.button_minus_quantity:
            {
                manageQuantity(false);
                break;
            }
        }
    }

    private void manageQuantity(boolean addButton)
    {
        int quantity = Integer.valueOf(textViewQuantity.getText().toString());
        quantity = addButton ? quantity + 1 : quantity - 1;
        if(quantity <= 0)
        {
            quantity = 0;
        }
        textViewQuantity.setText(String.valueOf(quantity));
    }
}
