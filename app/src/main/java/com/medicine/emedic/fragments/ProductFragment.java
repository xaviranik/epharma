package com.medicine.emedic.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.carteasy.v1.lib.Carteasy;
import com.medicine.emedic.R;
import com.medicine.emedic.activities.MainActivity;
import com.medicine.emedic.network.web.KEYS;
import com.medicine.emedic.utils.Utils;

import java.util.Map;

public class ProductFragment extends Fragment implements View.OnClickListener{

    private String productID;
    private  String productImage;
    private String descrip;
    private String short_description;
    private String productName;
    private float productPrice;
    private int productQuantity;

    private TextView textViewProductName;
    private TextView textViewProductPrice;
    private TextView textViewQuantity;
    private TextView description;
    private TextView textShortDescription;

    private ImageView buttonAddQuantity;
    private ImageView buttonRemoveQuantity;
    private ImageView imageViewProduct;
    private Button buttonAddToCart;

    private Carteasy cart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        init(view);
        ((MainActivity)getActivity()).setAppTitle("eMedic");
        return view;
    }

    private void init(View view)
    {
        textViewProductName = (TextView) view.findViewById(R.id.textView_product_name);
        textViewProductPrice = (TextView) view.findViewById(R.id.textView_product_price);
        description = (TextView) view.findViewById(R.id.description);
        textShortDescription = (TextView) view.findViewById(R.id.shortdescription);

        buttonAddQuantity = (ImageView) view.findViewById(R.id.button_add_quantity);
        imageViewProduct = (ImageView) view.findViewById(R.id.imageView_product);
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
        Utils.makeSuccessAlert(getContext(), "Product added to cart!", null, R.drawable.ic_add_to_cart);
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
            cart.update(productID, KEYS.PRODUCT_QUANTITY, productQuantity, getContext());
        }

        Utils.updateCartQuantity(getContext());
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
            productImage = getArguments().getString(KEYS.PRODUCT_IMAGE);
            descrip = getArguments().getString(KEYS.PRODUCT_DESCRIPTION);
            short_description = getArguments().getString(KEYS.PRODUCT_Short_DESCRIPTION);
            Log.e("description","asda :: "+descrip);
            Log.e("getimagedata", ""+productImage);

            textViewProductName.setText(productName);
            textViewProductPrice.setText(Utils.formatPrice(productPrice));
            Glide.with(getContext())
                    .load(productImage)
                    .placeholder(R.drawable.ic_medicine_default)
                    .into(imageViewProduct);
           description.setText(Html.fromHtml(descrip));
            textShortDescription.setText(Html.fromHtml(short_description));
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
