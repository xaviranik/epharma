package com.triamatter.epharma.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.triamatter.epharma.R;
import com.triamatter.epharma.model.Product;

import java.util.ArrayList;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        getProductList();
    }

    private void getProductList()
    {
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("PRODUCTLIST");
        List<Product> productlist = (ArrayList<Product>) args.getSerializable("ARRAYLIST");

        for(Product product : productlist) {
            Log.i("PRODUCT", "getProductList: " + product.getProductName() + ": " + product.getProductQuantity());
        }
    }
}
