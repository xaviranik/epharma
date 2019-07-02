package com.triamatter.epharma.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.tapadoo.alerter.Alerter;
import com.triamatter.epharma.R;

public class Utils {
    public static void makeToast(Context context, String message)
    {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static String formatPrice(Float productPrice)
    {
        return  "BDT. " + String.format("%.2f", productPrice);
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
}
