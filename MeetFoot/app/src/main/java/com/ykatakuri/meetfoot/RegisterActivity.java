package com.ykatakuri.meetfoot;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class RegisterActivity extends AppCompatActivity {

    TextView mAlreadyHaveAccount;
    EditText mEmailEditText, mPasswordEditText, mConfirmPasswordEditText;
    Button mRegisterButton;
    ProgressDialog progressDialog;

    FirebaseAuth mFirebaseAuth;
    FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAlreadyHaveAccount = findViewById(R.id.registerTextViewAlreadyHaveAccount);
        mEmailEditText = findViewById(R.id.registerEditTextEmail);
        mPasswordEditText = findViewById(R.id.registerEditTextPassword);
        mConfirmPasswordEditText = findViewById(R.id.registerEditTextConfirmPassword);
        mRegisterButton = findViewById(R.id.registerButtonRegister);
        progressDialog = new ProgressDialog(this);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        mAlreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performRegistration();
            }
        });
    }

    private void performRegistration() {
        String email = mEmailEditText.getText().toString();
        String password= mPasswordEditText.getText().toString();
        String confirmPassword = mConfirmPasswordEditText.getText().toString();

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Log.v("Signup", "Email non valide");
            mEmailEditText.setError("Email non valide");
            mEmailEditText.requestFocus();
        } else if(password.isEmpty() || password.length() < 8) {
            Log.v("Signup", "Mot de passe non valide");
            mPasswordEditText.setError("Mot de passe non valide");
        } else if(!password.equals(confirmPassword)) {
            Log.v("Signup", "Les mots de passe sont différents");
            mPasswordEditText.setError("Les deux mots de passe sont différents");
        } else{
            progressDialog.setMessage("Inscription en cours...");
            progressDialog.setTitle("Inscription");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        sendUserToLoginActivity();
                        Toast.makeText(RegisterActivity.this, "Inscription terminée!", Toast.LENGTH_SHORT).show();
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Echec de l'inscription! Veuillez réessayer"+task.getException(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void sendUserToLoginActivity() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
