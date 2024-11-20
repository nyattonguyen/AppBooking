package com.example.appfood.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import androidx.annotation.NonNull;

import com.example.appfood.R;
import com.example.appfood.databinding.ActivitySignInBinding;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends BaseActivity {
    ActivitySignInBinding binding;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        FirebaseAuth.getInstance().setLanguageCode("en");
        setVariabless();
    }
    private void setVariabless() {
        binding.btnSignInNew.setOnClickListener(v -> {
            String email = binding.emailEdt.getText().toString();
            String password= binding.passwordEdt.getText().toString();

            if(!email.isEmpty() && !password.isEmpty()) {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignInActivity.this, task -> {
                            if (task.isSuccessful()) {
                                Log.i("SignInActivity", "Login successful, transitioning to MainActivity.");
                                startActivity(new Intent(SignInActivity.this, MainActivity.class));
                            } else {
                                Log.e("SignInActivity", "Login failed: " + (task.getException() != null ? task.getException().getMessage() : "Unknown error"));
                                Toast.makeText(SignInActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                            }
                        });
            }else {
                Toast.makeText(SignInActivity.this, "Please fill email and password", Toast.LENGTH_SHORT).show();
            }
        });

        binding.txtSignUp.setOnClickListener(v -> {
            startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            Toast.makeText(SignInActivity.this, "OK", Toast.LENGTH_LONG).show();
        });
    }
}