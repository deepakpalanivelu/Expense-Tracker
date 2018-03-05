package com.example.theon.expensetracker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends Activity  {
    Button loginBtn;
    TextView signupLink;
    EditText userName, password;

    TextView tagLine;
    private static final int REQUEST_SIGNUP = 0;
    private static final int REQUEST_HOME = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tagLine = (TextView)findViewById(R.id.tagLine);
        userName = (EditText)findViewById(R.id.email_id);
        password = (EditText)findViewById(R.id.pwd);
        loginBtn = (Button)findViewById(R.id.login);
        signupLink = (TextView)findViewById(R.id.signup);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });


        signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    private void login() {

        if (!validate()) {
            onLoginFailed();
            return;
        }

        loginBtn.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = userName.getText().toString();
        String pass = password.getText().toString();

        // Have to authenticate with the db

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    public boolean validate() {
        boolean valid = true;

        String email = userName.getText().toString();
        String pass = password.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            userName.setError("enter a valid email address");
            valid = false;
        } else {
            userName.setError(null);
        }

        if (pass.isEmpty() || pass.length() < 4) {
            password.setError("Password length should be greater than 4");
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        loginBtn.setEnabled(true);
    }



    public void onLoginSuccess() {
        loginBtn.setEnabled(true);
        // intent to home page from here.
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivityForResult(intent, REQUEST_HOME);
        //finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                // After sign up page automatically logs them in automatically
                onLoginSuccess();
            }
        }
        if(requestCode == REQUEST_HOME) {
            if(resultCode == RESULT_OK)
            finish();
        }
    }
}