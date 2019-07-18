package com.triamatter.epharma.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.triamatter.epharma.R;
import com.triamatter.epharma.model.Product;
import com.triamatter.epharma.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

    private int totalQuantity = 0;
    private float subTotalPrice = 0;
    private float deliveryCharge = 0;
    private int discount = 0;
    private float totalPrice = 0;

    private TextView textViewTotalQuantity;
    private TextView textViewDeliveryCharge;
    private TextView textViewSubtotal;
    private TextView textViewTotal;
    private TextView textViewDiscount;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        textViewTotalQuantity = (TextView) findViewById(R.id.textView_total_quantity);
        textViewSubtotal = (TextView) findViewById(R.id.textView_subtotal);
        textViewDiscount = (TextView) findViewById(R.id.textView_discount_percent);
        textViewTotal = (TextView) findViewById(R.id.textView_total_price);
        textViewDeliveryCharge = (TextView) findViewById(R.id.textView_delivery_charge);


        getProductList();
    }

    private void getProductList()
    {
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("PRODUCTLIST");
        List<Product> productList = (ArrayList<Product>) args.getSerializable("ARRAYLIST");

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

    public void refreshCartDetails()
    {
        totalPrice = subTotalPrice - (subTotalPrice * discount) + deliveryCharge;

        textViewTotalQuantity.setText(String.valueOf(totalQuantity));
        textViewSubtotal.setText(Utils.formatPrice(subTotalPrice));
        textViewDeliveryCharge.setText(Utils.formatPrice(deliveryCharge));
        textViewDiscount.setText(String.valueOf(discount));
        textViewTotal.setText(Utils.formatPrice(totalPrice));
    }
}
