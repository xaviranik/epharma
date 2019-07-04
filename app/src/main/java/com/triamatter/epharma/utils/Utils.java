package com.triamatter.epharma.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.carteasy.v1.lib.Carteasy;
import com.tapadoo.alerter.Alerter;
import com.triamatter.epharma.R;
import com.triamatter.epharma.activities.MainActivity;
import com.triamatter.epharma.network.web.KEYS;

import java.util.Map;

public class Utils {
    public static void makeToast(Context context, String message)
    {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static String formatPrice(Float productPrice)
    {
        return  "BDT.  " + String.format("%.2f", productPrice);
    }

    public static void makeDangerAlert(Context context, String title, String text, int icon)
    {
        if(Alerter.isShowing())
        {
            return;
        }
        Alerter.create((Activity) context)
                .setTitle(title)
                .setText(text)
                .setIcon(icon)
                .setBackgroundColorRes(R.color.colorAccent)
                .show();
    }

    public static void makeSuccessAlert(Context context, String title, String text, int icon)
    {
        if(Alerter.isShowing())
        {
            return;
        }
        Alerter.create((Activity) context)
                .setTitle(title)
                .setText(text)
                .setIcon(icon)
                .setBackgroundColorRes(R.color.colorSuccess)
                .show();
    }

    public static void responseErrorHandler(Context context, VolleyError error)
    {
        if(error instanceof NoConnectionError)
        {
            Utils.makeDangerAlert(
                context,
                "Lost Connection to Server!",
                "Make sure you are connected to internet",
                R.drawable.ic_no_internet
            );
        }
        else if(error instanceof TimeoutError)
        {
            Utils.makeDangerAlert(
                context,
                "Connection Timeout. Server might be busy or down!",
                "Try again later ",
                R.drawable.ic_no_internet
            );
        }
        else if(error instanceof NetworkError)
        {
            Utils.makeDangerAlert(
                context,
                "Server error!",
                "Make sure you are connected to internet",
                R.drawable.ic_no_internet
            );
        }
        else if(error instanceof ServerError)
        {
            Utils.makeDangerAlert(
                context,
                "Server Error!",
                "Please try again later",
                R.drawable.ic_no_internet
            );
        }
    }

    public static void updateCartQuantity(Context context)
    {
        int quantity = 0;
        GLOBAL.CART_QUANTITY = 0;
        Map<Integer, Map> data;
        Carteasy cs = new Carteasy();
        data = cs.ViewAll(context);

        for (Map.Entry<Integer, Map> entry : data.entrySet())
        {
            Map<String, String> innerData = entry.getValue();
            quantity += Integer.valueOf(innerData.get(KEYS.PRODUCT_QUANTITY));
            Log.i("GLOBAL UTIL", "" + quantity);
        }

        GLOBAL.CART_QUANTITY = quantity;
        ((MainActivity)context).updateCartQuantity();
    }
}
