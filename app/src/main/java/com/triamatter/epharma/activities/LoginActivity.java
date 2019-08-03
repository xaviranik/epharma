package com.triamatter.epharma.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textViewSignup;
    private EditText editTextUserEmail;
    private EditText editTextUserPassword;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init()
    {
        textViewSignup = (TextView) findViewById(R.id.textView_signup);
        editTextUserEmail = (EditText) findViewById(R.id.editText_email);
        editTextUserPassword = (EditText) findViewById(R.id.editText_password);
        loginButton = (Button) findViewById(R.id.button_login);

        textViewSignup.setOnClickListener(this);
        loginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.textView_signup:
            {
                Intent i = new Intent(LoginActivity.this, SignupActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                break;
            }
            case R.id.button_login:
            {
                loginUser();
                break;
            }
        }
    }

    private void loginUser()
    {
        final String userEmail = editTextUserEmail.getText().toString();
        final String userPass = editTextUserPassword.getText().toString();
        if(userEmail.isEmpty())
        {
            editTextUserEmail.setError("Email can't be empty!");
            editTextUserEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches())
        {
            editTextUserEmail.setError("Please enter a valid Email address!");
            editTextUserEmail.requestFocus();
            return;
        }
        if(userPass.isEmpty())
        {
            editTextUserPassword.setError("Password can't be empty!");
            editTextUserPassword.requestFocus();
            return;
        }

        loginButton.setEnabled(false);

        String url = API.POST_SIGNIN;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        loginButton.setEnabled(true);
                        try
                        {
                            JSONArray jsonArray = new JSONArray(response);

                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String res = jsonObject.getString("signin");
                            if(res.equals("true"))
                            {
                                SharedPreferences.Editor editor = getSharedPreferences(GLOBAL.AUTH_PREF, MODE_PRIVATE).edit();
                                editor.putBoolean(GLOBAL.AUTH_STATUS, true);
                                editor.apply();

                                Intent i = new Intent(LoginActivity.this, ProfileActivity.class);
                                i.putExtra(GLOBAL.SIGNUP_EMAIL, userEmail);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            }
                            else if(res.equals("wrong"))
                            {
                                Utils.makeToast(getApplicationContext(), "Wrong Email or password combination!");
                            }
                            else
                            {
                                Utils.makeToast(getApplicationContext(), "Login failed!");
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
                        loginButton.setEnabled(true);
                        Utils.makeToast(getApplicationContext(), "Login failed! Check your internet connection");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put(KEYS.USER_EMAIL, userEmail);
                params.put(KEYS.USER_PASSWORD, userPass);

                return params;
            }
        };

        NetworkSingleton.getInstance(this).addToRequestQueue(postRequest);
    }
}
