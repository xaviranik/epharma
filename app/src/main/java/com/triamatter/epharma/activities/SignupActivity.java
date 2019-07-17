package com.triamatter.epharma.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.triamatter.epharma.R;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView textViewLogin;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        init();
    }

    private void init()
    {
        textViewLogin = (TextView) findViewById(R.id.textView_login_to_signup);
        nextButton = (Button) findViewById(R.id.button_next);

        textViewLogin.setOnClickListener(this);
        nextButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.textView_login_to_signup:
            {
                Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                break;
            }
            case R.id.button_next:
            {
                Intent i = new Intent(SignupActivity.this, InfoActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                break;
            }
        }
    }
}
