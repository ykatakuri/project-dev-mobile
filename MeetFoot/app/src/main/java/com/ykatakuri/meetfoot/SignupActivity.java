package com.ykatakuri.meetfoot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    private Button mSignupButton;
    private EditText mEmailEditText, mPasswordEditText;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mEmailEditText = findViewById(R.id.signup_edittext_email);
        mPasswordEditText = findViewById(R.id.signup_edittext_password);
        mSignupButton = findViewById(R.id.signup_button_sign);
        mAuth = FirebaseAuth.getInstance();

        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailEditText.getText().toString().trim();
                String password= mPasswordEditText.getText().toString().trim();

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
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(SignUpActivity.this, "Inscription réussie!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(SignUpActivity.this, "Echec de l'inscription! Veuillez réessayer",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }
}
