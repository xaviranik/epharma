package com.triamatter.epharma.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.triamatter.epharma.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textViewSignup;
    private EditText editTextUserEmailName;
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
        textViewSignup.setOnClickListener(this);
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
            }
        }
    }
}
