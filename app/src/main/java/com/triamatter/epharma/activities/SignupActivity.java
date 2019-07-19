package com.triamatter.epharma.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.triamatter.epharma.R;
import com.triamatter.epharma.utils.GLOBAL;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView textViewLogin;
    private Button nextButton;

    private EditText editTextUserEmail;
    private EditText editTextUserPassword;
    private EditText editTextUserConfirmPassword;

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
        editTextUserEmail = (EditText) findViewById(R.id.editText_email);
        editTextUserPassword = (EditText) findViewById(R.id.editText_password);
        editTextUserConfirmPassword = (EditText) findViewById(R.id.editText_password_confirm);

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
                validateInfo();
                break;
            }
        }
    }

    private void validateInfo()
    {
        String userEmail = editTextUserEmail.getText().toString();
        String userPassword = editTextUserPassword.getText().toString();
        String userConfirmPassword = editTextUserConfirmPassword.getText().toString();

        //Validation
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

        if(userPassword.isEmpty())
        {
            editTextUserPassword.setError("Password can't be empty!");
            editTextUserPassword.requestFocus();
            return;
        }

        if(userConfirmPassword.isEmpty())
        {
            editTextUserConfirmPassword.setError("Please re-enter the password!");
            editTextUserConfirmPassword.requestFocus();
            return;
        }

        if(!userPassword.equals(userConfirmPassword))
        {
            editTextUserPassword.setError("Password should be same!");
            editTextUserPassword.requestFocus();
            return;
        }

        if(userPassword.length() < 6)
        {
            editTextUserPassword.setError("Minimum password length should be six characters!");
            editTextUserPassword.requestFocus();
            return;
        }

        Intent i = new Intent(SignupActivity.this, InfoActivity.class);
        i.putExtra(GLOBAL.SIGNUP_EMAIL, userEmail);
        i.putExtra(GLOBAL.SIGNUP_PASSWORD, userPassword);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}
