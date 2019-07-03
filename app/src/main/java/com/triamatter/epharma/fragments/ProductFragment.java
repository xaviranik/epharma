package com.triamatter.epharma.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.carteasy.v1.lib.Carteasy;
import com.triamatter.epharma.R;
import com.triamatter.epharma.activities.MainActivity;
import com.triamatter.epharma.network.web.KEYS;
import com.triamatter.epharma.utils.Utils;

import java.util.Map;

public class ProductFragment extends Fragment implements View.OnClickListener{

    private String productID;
    private String productName;
    private float productPrice;
    private int productQuantity;

    private TextView textViewProductName;
    private TextView textViewProductPrice;
    private TextView textViewQuantity;

    private ImageView buttonAddQuantity;
    private ImageView buttonRemoveQuantity;
    private Button buttonAddToCart;

    private Carteasy cart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        init(view);
        ((MainActivity)getActivity()).setAppTitle("E-Pharma");
        return view;
    }

    private void init(View view)
    {
        textViewProductName = (TextView) view.findViewById(R.id.textView_product_name);
        textViewProductPrice = (TextView) view.findViewById(R.id.textView_product_price);

        buttonAddQuantity = (ImageView) view.findViewById(R.id.button_add_quantity);
        buttonRemoveQuantity = (ImageView) view.findViewById(R.id.button_minus_quantity);
        textViewQuantity = (TextView) view.findViewById(R.id.textView_product_quantity);

        buttonAddToCart = (Button) view.findViewById(R.id.button_add_to_cart);

        buttonAddQuantity.setOnClickListener((View.OnClickListener) this);
        buttonRemoveQuantity.setOnClickListener((View.OnClickListener) this);
        buttonAddToCart.setOnClickListener((View.OnClickListener) this);

        quantityWatcher();
        cartInit();
        getArgumentsFromPreviousFragment();
    }

    private void cartInit()
    {
        cart = new Carteasy();
        cart.persistData(getContext(), true);
    }

    private void addToCart()
    {
        Utils.makeToast(getContext(), "Added Product");
        Map<String, String> data = cart.ViewData(productID, getContext());

        if(data.isEmpty())
        {
            cart.add(productID, KEYS.PRODUCT_ID, productID);
            cart.add(productID, KEYS.PRODUCT_NAME, productName);
            cart.add(productID, KEYS.PRODUCT_PRICE, productPrice);
            cart.add(productID, KEYS.PRODUCT_QUANTITY, productQuantity);
            cart.commit(getContext());
        }
        else
        {
            long prevQuantity = cart.getLong(productID, KEYS.PRODUCT_QUANTITY, getContext());
            cart.update(productID, KEYS.PRODUCT_QUANTITY, prevQuantity + productQuantity, getContext());
        }

        Utils.updateCartQuantity(getContext());
        ((MainActivity)getActivity()).updateCartQuantity();
    }

    private void manageQuantity(boolean addButton)
    {
        productQuantity = Integer.valueOf(textViewQuantity.getText().toString());
        productQuantity = addButton ? productQuantity + 1 : productQuantity - 1;
        if(productQuantity <= 0)
        {
            productQuantity = 0;
        }
        textViewQuantity.setText(String.valueOf(productQuantity));
    }

    private void quantityWatcher()
    {
        buttonAddToCart.setEnabled(false);
        textViewQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                if(charSequence.toString().equals("0"))
                {
                    buttonAddToCart.setEnabled(false);
                }
                else
                {
                    buttonAddToCart.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });
    }

    private void getArgumentsFromPreviousFragment()
    {
        if(getArguments() != null)
        {
            productID = String.valueOf(getArguments().getInt(KEYS.PRODUCT_ID));
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

            case R.id.button_add_to_cart:
            {
                addToCart();
                break;
            }
        }
    }
}
