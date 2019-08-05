package com.triamatter.epharma.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import com.hololo.tutorial.library.PermissionStep;
import com.hololo.tutorial.library.Step;
import com.hololo.tutorial.library.TutorialActivity;
import com.triamatter.epharma.R;
import com.triamatter.epharma.network.web.KEYS;
import com.triamatter.epharma.utils.GLOBAL;

public class WelcomeActivity extends TutorialActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        addFragment(new Step.Builder().setTitle("ORDER MEDICAL ITEMS ON DEMAND")
                .setContent("Order and get your products in no time!")
                .setBackgroundColor(Color.parseColor("#9C27B0")) // int background color
                .setDrawable(R.drawable.ic_slider_2) // int top drawable
                .build());

        addFragment(new Step.Builder().setTitle("UPLOAD PRESCRIPTIONS")
                .setContent("Upload prescription image and order without any hassle!")
                .setBackgroundColor(Color.parseColor("#f44336")) // int background color
                .setDrawable(R.drawable.ic_slider_1)
                .build());

        // Permission Step
        addFragment(new PermissionStep.Builder().setTitle("PLEASE GRANT PERMISSIONS")
                .setContent("Emedic requires some permissions to work like a charm!")
                .setBackgroundColor(Color.parseColor("#009688"))
                .setDrawable(R.drawable.ic_slider_3) // int top drawable
                .setPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET, Manifest.permission.CALL_PHONE})
                .build());
    }

    @Override
    public void currentFragmentPosition(int position)
    {

    }

    @Override
    public void finishTutorial()
    {
        SharedPreferences.Editor editor = getSharedPreferences(GLOBAL.AUTH_PREF, MODE_PRIVATE).edit();
        editor.putBoolean(GLOBAL.NEW_USER_STATUS, false);
        editor.apply();

        Intent i = new Intent(WelcomeActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}
