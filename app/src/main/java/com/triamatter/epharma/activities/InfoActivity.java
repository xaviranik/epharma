package com.triamatter.epharma.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.triamatter.epharma.R;
import com.triamatter.epharma.network.NetworkSingleton;
import com.triamatter.epharma.network.web.API;
import com.triamatter.epharma.network.web.KEYS;
import com.triamatter.epharma.utils.GLOBAL;
import com.triamatter.epharma.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InfoActivity extends AppCompatActivity {
    private String userEmail;
    private String userPass;

    private EditText editTextUserName;
    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextAddress;

    private Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        editTextUserName = (EditText) findViewById(R.id.editText_username);
        editTextFirstName = (EditText) findViewById(R.id.editText_first_name);
        editTextLastName = (EditText) findViewById(R.id.editText_last_name);
        editTextAddress = (EditText) findViewById(R.id.editText_address);

        signupButton = (Button) findViewById(R.id.button_signup);

        getIntentInfo();

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                validateAndRegisterUser();
            }
        });
    }

    private void validateAndRegisterUser()
    {
        final String userName = editTextUserName.getText().toString();
        final String userFirstName = editTextFirstName.getText().toString();
        final String userLastName = editTextLastName.getText().toString();
        final String userAddress = editTextAddress.getText().toString();

        if(userName.isEmpty())
        {
            editTextUserName.setError("Username can't be empty!");
            editTextUserName.requestFocus();
            return;
        }

        if(userName.length() < 7)
        {
            editTextUserName.setError("Username length must be at least 7 characters");
            editTextUserName.requestFocus();
            return;
        }

        if(userName.contains(" "))
        {
            editTextUserName.setError("No spaces allowed in username!");
            editTextUserName.requestFocus();
            return;
        }

        if(userFirstName.isEmpty())
        {
            editTextFirstName.setError("First name can't be empty!");
            editTextFirstName.requestFocus();
            return;
        }

        if(userLastName.isEmpty())
        {
            editTextLastName.setError("Last name can't be empty!");
            editTextLastName.requestFocus();
            return;
        }

        if(userAddress.isEmpty())
        {
            editTextAddress.setError("Address can't be empty!");
            editTextAddress.requestFocus();
            return;
        }

        signupButton.setEnabled(false);

        String url = API.POST_SIGNUP;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        signupButton.setEnabled(true);
                        try
                        {
                            JSONArray jsonArray = new JSONArray(response);

                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String res = jsonObject.getString("signup");
                            if(res.equals("true"))
                            {
                                SharedPreferences.Editor editor = getSharedPreferences(GLOBAL.AUTH_PREF, MODE_PRIVATE).edit();
                                editor.putBoolean(GLOBAL.AUTH_STATUS, true);
                                editor.apply();

                                Intent i = new Intent(InfoActivity.this, MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            }
                            else
                            {
                                Utils.makeToast(getApplicationContext(), "Sign up failed!");
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
                        signupButton.setEnabled(true);
                        Utils.makeToast(getApplicationContext(), "Sign up failed! Check your internet connection");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put(KEYS.USER_EMAIL, userEmail);
                params.put(KEYS.USER_PASSWORD, userPass);
                params.put(KEYS.USER_NAME, userName);
                params.put(KEYS.USER_FIRST_NAME, userFirstName);
                params.put(KEYS.USER_LAST_NAME, userLastName);
                params.put(KEYS.USER_ADDRESS, userAddress);

                return params;
            }
        };

        NetworkSingleton.getInstance(this).addToRequestQueue(postRequest);

    }

    private void getIntentInfo()
    {
        userEmail = getIntent().getStringExtra(GLOBAL.SIGNUP_EMAIL);
        userPass = getIntent().getStringExtra(GLOBAL.SIGNUP_PASSWORD);
    }
}
