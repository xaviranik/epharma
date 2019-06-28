package com.triamatter.epharma.utils;

import android.content.Context;
import android.widget.Toast;

public class Utils {
    public static void makeToast(Context context, String message)
    {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static String formatPrice(Float productPrice)
    {
        return  "BDT. " + String.format("%.2f", productPrice);
    }
}
