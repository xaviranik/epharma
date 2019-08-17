package com.medicine.emedic.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.medicine.emedic.R;
import com.medicine.emedic.network.NetworkSingleton;
import com.medicine.emedic.network.web.API;
import com.medicine.emedic.network.web.KEYS;
import com.medicine.emedic.utils.GLOBAL;
import com.medicine.emedic.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        
        getUserEmail();
        getUserProfile();
    }

    private void getUserProfile()
    {
        String url = API.POST_PROFILE;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String user_id = jsonObject.getString(KEYS.USER_ID);
                            String first_name = jsonObject.getString(KEYS.USER_FIRST_NAME);
                            String last_name = jsonObject.getString(KEYS.USER_LAST_NAME);
                            String user_phone = jsonObject.getString(KEYS.USER_PHONE);
                            String user_address = jsonObject.getString(KEYS.USER_ADDRESS);

                            SharedPreferences.Editor editor = getSharedPreferences(GLOBAL.AUTH_PREF, MODE_PRIVATE).edit();
                            editor.putString(KEYS.USER_ID, user_id);
                            editor.putString(KEYS.USER_EMAIL, userEmail);
                            editor.putString(KEYS.USER_FIRST_NAME, first_name);
                            editor.putString(KEYS.USER_LAST_NAME, last_name);
                            editor.putString(KEYS.USER_PHONE, user_phone);
                            editor.putString(KEYS.USER_ADDRESS, user_address);

                            editor.putBoolean(GLOBAL.AUTH_STATUS, true);
                            editor.apply();

                            Intent i = new Intent(ProfileActivity.this, MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        } catch (JSONException e) {
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
                params.put(KEYS.USER_EMAIL, userEmail);
                return params;
            }
        };

        NetworkSingleton.getInstance(this).addToRequestQueue(postRequest);
    }

    private void getUserEmail()
    {
        userEmail = getIntent().getStringExtra(GLOBAL.SIGNUP_EMAIL);
    }
}
