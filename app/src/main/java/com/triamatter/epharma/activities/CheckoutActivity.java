package com.triamatter.epharma.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.carteasy.v1.lib.Carteasy;
import com.triamatter.epharma.R;
import com.triamatter.epharma.model.Product;
import com.triamatter.epharma.network.NetworkSingleton;
import com.triamatter.epharma.network.web.API;
import com.triamatter.epharma.network.web.KEYS;
import com.triamatter.epharma.utils.GLOBAL;
import com.triamatter.epharma.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {

    private int numberOfRequestsToMake = 0;
    private boolean hasRequestFailed = false;

    List<Product> productList;

    private int totalQuantity = 0;
    private float subTotalPrice = 0;
    private float deliveryCharge = 0;
    private int discount = 0;
    private float totalPrice = 0;

    int productID;
    String productName;
    float productPrice;
    int productQuantity;

    private TextView textViewTotalQuantity;
    private TextView textViewDeliveryCharge;
    private TextView textViewSubtotal;
    private TextView textViewTotal;
    private TextView textViewDiscount;

    private Button checkoutButton;

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

        checkoutButton = (Button) findViewById(R.id.button_place_order);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                checkoutOrder();
            }
        });


        getProductList();
    }

    private void checkoutOrder()
    {
        checkoutButton.setEnabled(false);
        long orderID = generateOrderID();

        numberOfRequestsToMake = productList.size();

        if(productList != null)
        {
            for (int i=0; i < productList.size(); i++)
            {
                Product product = productList.get(i);
                productID = product.getProductID();
                productName = product.getProductName();
                productPrice = product.getProductPrice();
                productQuantity = product.getProductQuantity();

                float lineTotal = productQuantity * productPrice;
                String orderItemType = "line_item";

                insertCartToDatabase(productID, productName, productQuantity, lineTotal, orderItemType, orderID);
            }
        }
    }

    private void clearCart()
    {
        GLOBAL.CART_QUANTITY = 0;
        productList.clear();
        Carteasy cs = new Carteasy();
        cs.clearCart(getApplicationContext());

        Intent i = new Intent(CheckoutActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    private void insertCartToDatabase(final int productID, final String productName, final int productQuantity, final float lineTotal, final String orderItemType, final long orderID)
    {
        String url = API.POST_INSERT_ORDER;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try
                        {
                            JSONArray jsonArray = new JSONArray(response);

                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String res = jsonObject.getString("success");

                            numberOfRequestsToMake--;

                            if(numberOfRequestsToMake == 0)
                            {
                                if(!hasRequestFailed)
                                {
                                    //All requests finished correctly
                                    clearCart();
                                    Utils.makeToast(getApplicationContext(), "ORDER PLACED!");
                                }
                                else
                                {
                                    //At least one request failed
                                    Utils.makeToast(getApplicationContext(), "ERROR");
                                }
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        numberOfRequestsToMake--;
                        hasRequestFailed = true;

                        if(numberOfRequestsToMake == 0)
                        {
                            //The last request failed
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put(KEYS.PRODUCT_ID, String.valueOf(productID));
                params.put(KEYS.PRODUCT_NAME, productName);
                params.put(KEYS.PRODUCT_QUANTITY, String.valueOf(productQuantity));
                params.put(KEYS.ORDER_ID, String.valueOf(orderID));
                params.put(KEYS.ORDER_ITEM_TYPE, orderItemType);
                params.put(KEYS.LINE_TOTAL, String.valueOf(lineTotal));

                return params;
            }
        };

        NetworkSingleton.getInstance(this).addToRequestQueue(postRequest);
    }

    private long generateOrderID()
    {
        //method 1
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        //return number of milliseconds since January 1, 1970, 00:00:00 GMT
        return timestamp.getTime();
    }

    private void getProductList()
    {
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("PRODUCTLIST");
        productList = (ArrayList<Product>) args.getSerializable("ARRAYLIST");

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
