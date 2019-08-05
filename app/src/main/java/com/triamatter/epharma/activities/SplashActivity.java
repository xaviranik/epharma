package com.triamatter.epharma.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.triamatter.epharma.R;
import com.triamatter.epharma.utils.GLOBAL;

import java.nio.file.WatchEvent;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        checkForNewUser();
    }

    private void checkForNewUser()
    {
        SharedPreferences prefs = getSharedPreferences(GLOBAL.AUTH_PREF, MODE_PRIVATE);
        boolean newUser = prefs.getBoolean(GLOBAL.NEW_USER_STATUS, true);

        if(!newUser)
        {
            View easySplashScreenView = new EasySplashScreen(SplashActivity.this)
                    .withFullScreen()
                    .withTargetActivity(MainActivity.class)
                    .withSplashTimeOut(1000)
                    .withLogo(R.drawable.ic_emedic_logo)
                    .create();
            setContentView(easySplashScreenView);
        }
        else
        {
            View easySplashScreenView = new EasySplashScreen(SplashActivity.this)
                    .withFullScreen()
                    .withTargetActivity(WelcomeActivity.class)
                    .withSplashTimeOut(1000)
                    .withLogo(R.drawable.ic_emedic_logo)
                    .create();
            setContentView(easySplashScreenView);
        }
    }
}
