package com.triamatter.epharma.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.carteasy.v1.lib.Carteasy;
import com.jaredrummler.materialspinner.MaterialSpinner;
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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {

    private String user_id, user_email, user_address, user_phone, first_name, last_name;

    private int numberOfRequestsToMake = 0;
    private boolean hasRequestFailed = false;

    List<Product> productList;

    private int totalQuantity = 0;
    private float subTotalPrice = 0;
    private float deliveryCharge = 60;
    private int discount = 0;
    private float totalPrice = 0;
    private long orderID;
    private String couponCode;
    private String city = "Dhaka";
    private float discountAmount;

    int productID;
    String productName;
    float productPrice;
    int productQuantity;

    private TextView textViewTotalQuantity;
    private TextView textViewDeliveryCharge;
    private TextView textViewSubtotal;
    private TextView textViewTotal;
    private TextView textViewDiscount;

    private EditText editTextCoupon;
    private EditText editTextAddress;

    private Button checkoutButton;
    private Button addCouponButton;

    private MaterialSpinner spinner;
    private ConstraintLayout loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        checkForAuth();
        textViewTotalQuantity = (TextView) findViewById(R.id.textView_total_quantity);
        textViewSubtotal = (TextView) findViewById(R.id.textView_subtotal);
        textViewDiscount = (TextView) findViewById(R.id.textView_discount_percent);
        textViewTotal = (TextView) findViewById(R.id.textView_total_price);
        textViewDeliveryCharge = (TextView) findViewById(R.id.textView_delivery_charge);
        editTextCoupon = (EditText) findViewById(R.id.editText_coupon);
        editTextAddress = (EditText) findViewById(R.id.editText_address_checkout);
        editTextAddress.setText(user_address);
        loadingView = (ConstraintLayout) findViewById(R.id.loading_view_checkout);
        spinner = (MaterialSpinner) findViewById(R.id.spinner);

        checkoutButton = (Button) findViewById(R.id.button_place_order);
        addCouponButton = (Button) findViewById(R.id.button_add_coupon);
        addCouponButton.setVisibility(View.GONE);

        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                checkoutOrder();
            }
        });

        couponTextWatcher();

        getDeliveryZone();
        getProductList();

    }

    private void checkoutOrder()
    {
        loadingView.setVisibility(View.VISIBLE);
        checkoutButton.setEnabled(false);
        orderID = generateOrderID();

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

    private void insertShippingToDatabase(final String productName, final String orderItemType, final long orderID)
    {
        String url = API.POST_INSERT_SHIPPING;

        StringRequest shippingRequest = new StringRequest(Request.Method.POST, url,
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

                            if(res.equals("true"))
                            {
                                //clearCart();
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
                        Utils.makeToast(getApplicationContext(), "Please check your internet connection!");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put(KEYS.PRODUCT_NAME, productName);
                params.put(KEYS.ORDER_ID, String.valueOf(orderID));
                params.put(KEYS.ORDER_ITEM_TYPE, orderItemType);

                return params;
            }
        };

        NetworkSingleton.getInstance(this).addToRequestQueue(shippingRequest);
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
                                    insertShippingToDatabase("Flat Rate", "shipping", orderID);
                                    insertOderInfo();
                                    //clearCart();
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
                        Utils.makeToast(getApplicationContext(), "Error! Please check your internet connection!");
                        Log.i("CART ERROR", "" + error.getMessage());
                        startActivity(new Intent(CheckoutActivity.this, MainActivity.class));
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

    private void insertOderInfo()
    {
        String url = API.POST_INSERT_ORDER_INFO;

        StringRequest shippingRequest = new StringRequest(Request.Method.POST, url,
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

                            if(res.equals("inserted"))
                            {
                                clearCart();
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
                        Utils.makeToast(getApplicationContext(), "Please check your internet connection!");
                        Log.i("CART ERROR", "" + error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put(KEYS.ORDER_ID, String.valueOf(orderID));
                params.put(KEYS.USER_ID, user_id);
                params.put(KEYS.USER_FIRST_NAME, first_name);
                params.put(KEYS.USER_LAST_NAME, last_name);
                params.put(KEYS.USER_EMAIL, user_email);
                params.put("user_phone", user_phone);
                params.put("address", editTextAddress.getText().toString());
                params.put("city", city);
                params.put("order_total", String.valueOf(totalPrice));
                params.put("_order_shipping", String.valueOf(deliveryCharge));
                params.put("cart_discount", "0");

                return params;
            }
        };

        NetworkSingleton.getInstance(this).addToRequestQueue(shippingRequest);
    }

    private void getDeliveryZone()
    {
        spinner.setItems("Inside Dhaka", "Outside Dhaka");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item)
            {
                if(position == 0)
                {
                    city = "Dhaka";
                    deliveryCharge = 60;
                    refreshCartDetails();
                }
                else if(position == 1)
                {
                    city = "Outside Dhaka";
                    deliveryCharge = 100;
                    refreshCartDetails();
                }
            }
        });
    }

    private void clearCart()
    {
        GLOBAL.CART_QUANTITY = 0;
        productList.clear();
        Carteasy cs = new Carteasy();
        cs.clearCart(getApplicationContext());

        Intent i = new Intent(CheckoutActivity.this, ThankYouActivity.class);
        i.putExtra(KEYS.ORDER_ID, orderID);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    private long generateOrderID()
    {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
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

    private void couponTextWatcher()
    {
        editTextCoupon.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                if(charSequence.length() == 0)
                {
                    addCouponButton.setVisibility(View.GONE);
                }
                else
                {
                    addCouponButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });
    }

    private void checkForAuth()
    {
        SharedPreferences prefs = getSharedPreferences(GLOBAL.AUTH_PREF, MODE_PRIVATE);
        boolean isAuthenticated  = prefs.getBoolean(GLOBAL.AUTH_STATUS, false);
        if (isAuthenticated)
        {
            Utils.makeToast(getApplicationContext(), "You are logged in!");
            checkForProfile();
        }
        else
        {
            Utils.makeToast(getApplicationContext(), "You are not logged in!");
            startActivity(new Intent(CheckoutActivity.this, LoginActivity.class));
        }
    }

    private void checkForProfile()
    {
        SharedPreferences prefs = getSharedPreferences(GLOBAL.AUTH_PREF, MODE_PRIVATE);
        user_id = prefs.getString(KEYS.USER_ID, "");
        first_name = prefs.getString(KEYS.USER_FIRST_NAME, "");
        last_name = prefs.getString(KEYS.USER_LAST_NAME, "");
        user_email = prefs.getString(KEYS.USER_EMAIL, "");
        user_address = prefs.getString(KEYS.USER_ADDRESS, "");
        user_phone = 88 + prefs.getString(KEYS.USER_PHONE, "");
    }

}
