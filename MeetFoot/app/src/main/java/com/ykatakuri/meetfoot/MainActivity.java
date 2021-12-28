package com.ykatakuri.meetfoot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    TextView createNewAccount;
    EditText mEmailEditText, mPasswordEditText;
    Button mLoginButton;

    ProgressDialog progressDialog;

    FirebaseAuth mFirebaseAuth;
    FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        createNewAccount = findViewById(R.id.mainTextViewCreateNewAccount);
        mEmailEditText = findViewById(R.id.mainEditTextEmail);
        mPasswordEditText = findViewById(R.id.mainEditTextPassword);
        mLoginButton = findViewById(R.id.mainButtonLogin);
        progressDialog = new ProgressDialog(this);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        createNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
            }
        });
    }

    private void performLogin() {
        String email = mEmailEditText.getText().toString();
        String password= mPasswordEditText.getText().toString();

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmailEditText.setError("Email incorrect");
            mEmailEditText.requestFocus();
        } else if(password.isEmpty() || password.length() < 8) {
            mPasswordEditText.setError("Mot de passe incorrect");
        } else {
            progressDialog.setMessage("Connexion en cours...");
            progressDialog.setTitle("Connexion");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        sendUserToHomeActivity();
                        Toast.makeText(MainActivity.this, "Connexion réussie!", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Connexion échouée! Veuillez réessayer" + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    private void sendUserToHomeActivity() {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}