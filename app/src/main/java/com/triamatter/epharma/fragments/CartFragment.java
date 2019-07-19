package com.triamatter.epharma.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.carteasy.v1.lib.Carteasy;
import com.triamatter.epharma.R;
import com.triamatter.epharma.activities.CheckoutActivity;
import com.triamatter.epharma.activities.MainActivity;
import com.triamatter.epharma.adapter.CartAdapter;
import com.triamatter.epharma.model.Product;
import com.triamatter.epharma.network.web.KEYS;
import com.triamatter.epharma.utils.EmptyRecyclerView;
import com.triamatter.epharma.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CartFragment extends Fragment implements CartAdapter.OnItemClickListener{

    private EmptyRecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private List<Product> productList;

    private TextView textViewEmpty;

    private TextView textViewTotalQuantity;
    private TextView textViewDeliveryCharge;
    private TextView textViewSubtotal;
    private TextView textViewTotal;
    private TextView textViewDiscount;

    private Button checkoutButton;

    private int totalQuantity = 0;
    private float subTotalPrice = 0;
    private float deliveryCharge = 0;
    private int discount = 0;
    private float totalPrice = 0;

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

        textViewTotalQuantity = (TextView) view.findViewById(R.id.textView_total_quantity);
        textViewSubtotal = (TextView) view.findViewById(R.id.textView_subtotal);
        textViewDiscount = (TextView) view.findViewById(R.id.textView_discount_percent);
        textViewTotal = (TextView) view.findViewById(R.id.textView_total_price);
        textViewDeliveryCharge = (TextView) view.findViewById(R.id.textView_delivery_charge);

        checkoutButton = (Button) view.findViewById(R.id.button_checkout_cart);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                checkout();
            }
        });

        setupCartRecyclerView();
    }

    private void refreshCheckoutButton()
    {
        if(productList.isEmpty())
        {
            checkoutButton.setEnabled(false);
            checkoutButton.setAlpha(0.5f);
        }
        else
        {
            checkoutButton.setEnabled(true);
            checkoutButton.setAlpha(1f);
        }
    }

    private void checkout()
    {
        List<Product> object = productList;
        Intent intent = new Intent(getActivity(), CheckoutActivity.class);
        Bundle args = new Bundle();

        args.putSerializable("ARRAYLIST",(Serializable)object);
        intent.putExtra("PRODUCTLIST",args);
        startActivity(intent);
    }

    private void setupCartRecyclerView()
    {
        productList =  new ArrayList<>();

        getCartDetails();

        cartAdapter = new CartAdapter(productList, getContext());
        cartAdapter.setOnItemClickListener(CartFragment.this);

        cartRecyclerView.setHasFixedSize(true);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartRecyclerView.setAdapter(cartAdapter);

        cartRecyclerView.setEmptyView(textViewEmpty);

        refreshCartDetails();
    }

    private void getCartDetails()
    {
        Map<Integer, Map> data;
        Carteasy cs = new Carteasy();
        data = cs.ViewAll(getContext());

        for (Map.Entry<Integer, Map> entry : data.entrySet())
        {
            Map<String, String> innerData = entry.getValue();
            int productID = Integer.valueOf(innerData.get(KEYS.PRODUCT_ID));
            String productName = innerData.get(KEYS.PRODUCT_NAME);
            float productPrice = Float.valueOf(innerData.get(KEYS.PRODUCT_PRICE));
            int productQuantity = Integer.valueOf(innerData.get(KEYS.PRODUCT_QUANTITY));

            totalQuantity += productQuantity;
            subTotalPrice += productPrice * productQuantity;

            Product product = new Product(productID, productName, productPrice, productQuantity);
            productList.add(product);
        }

        refreshCheckoutButton();
    }

    public void refreshCartDetails()
    {
        totalPrice = subTotalPrice - (subTotalPrice * discount) + deliveryCharge;

        textViewTotalQuantity.setText(String.valueOf(totalQuantity));
        textViewSubtotal.setText(Utils.formatPrice(subTotalPrice));
        textViewDeliveryCharge.setText(Utils.formatPrice(deliveryCharge));
        textViewDiscount.setText(String.valueOf(discount));
        textViewTotal.setText(Utils.formatPrice(totalPrice));

        refreshCheckoutButton();
    }

    @Override
    public void onAddRemoveButtonClick(int position, List<Product> productList)
    {
        totalQuantity = 0;
        subTotalPrice = 0;
        totalPrice = 0;

        for (int i=0; i < productList.size(); i++)
        {
            Product product = productList.get(i);
            totalQuantity += product.getProductQuantity();
            subTotalPrice += product.getProductPrice() * product.getProductQuantity();
        }

        refreshCartDetails();
    }
}
