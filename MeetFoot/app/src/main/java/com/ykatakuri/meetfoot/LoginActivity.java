package com.ykatakuri.meetfoot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText mEmailEditText, mPasswordEditText;
    private Button mLoginButton, mSignupButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailEditText = findViewById(R.id.signup_edittext_email);
        mPasswordEditText = findViewById(R.id.signup_edittext_password);
        mLoginButton = findViewById(R.id.login_button_login);
        mSignupButton = findViewById(R.id.login_button_signup);
        mAuth=FirebaseAuth.getInstance();

        mLoginButton.setOnClickListener(v -> {
            String email = mEmailEditText.getText().toString().trim();
            String password = mPasswordEditText.getText().toString().trim();

            if(email.isEmpty())
            {
                mEmailEditText.setError("Entrez l'email");
                mEmailEditText.requestFocus();

                return;
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            {
                mEmailEditText.setError("Email non valide");
                mEmailEditText.requestFocus();

                return;
            }
            if(password.isEmpty())
            {
                mPasswordEditText.setError("Entrez le mot de passe");
                mPasswordEditText.requestFocus();

                return;
            }
            if(password.length() < 8)
            {
                mPasswordEditText.setError("Votre mot de passe doit contenir au moins 8 caractères");
                mPasswordEditText.requestFocus();

                return;
            }
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                if(task.isSuccessful())
                {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
                else
                {
                    Toast.makeText(LoginActivity.this,
                            "Please Check Your login Credentials",
                            Toast.LENGTH_SHORT).show();
                }

            });
        });
        mSignupButton.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this,SignUpActivity.class )));
    }

}