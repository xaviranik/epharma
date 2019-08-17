package com.medicine.emedic.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.medicine.emedic.R;
import com.medicine.emedic.network.web.KEYS;
import com.medicine.emedic.utils.GLOBAL;

public class ShowProfileActivity extends AppCompatActivity {
    TextView userName;
    TextView userAddress;
    TextView userPhone;
    TextView userEmail;

    String first_name, last_name, user_email, user_address, user_phone;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);

        userName = (TextView) findViewById(R.id.user_name_textview);
        userAddress = (TextView) findViewById(R.id.user_address_textview);
        userEmail = (TextView) findViewById(R.id.user_email_textview);
        userPhone = (TextView) findViewById(R.id.user_phone_textview);

        getUserProfile();
    }

    private void getUserProfile()
    {
        SharedPreferences prefs = getSharedPreferences(GLOBAL.AUTH_PREF, MODE_PRIVATE);
        first_name = prefs.getString(KEYS.USER_FIRST_NAME, "");
        last_name = prefs.getString(KEYS.USER_LAST_NAME, "");
        user_email = prefs.getString(KEYS.USER_EMAIL, "");
        user_address = prefs.getString(KEYS.USER_ADDRESS, "");
        user_phone = "+" + 88 + prefs.getString(KEYS.USER_PHONE, "");

        setUserProfile();
    }

    private void setUserProfile()
    {
        userName.setText(first_name + " " + last_name);
        userAddress.setText(user_address);
        userPhone.setText(user_phone);
        userEmail.setText(user_email);
    }
}
