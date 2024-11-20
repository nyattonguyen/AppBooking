package com.example.appfood.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appfood.R;
import com.example.appfood.databinding.IntroMainBinding;

public class IntroActivity extends BaseActivity {
    IntroMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = IntroMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setVariable();
        getWindow().setStatusBarColor(Color.parseColor("#FFE4B5"));

    }

    private void setVariable() {
        binding.btnSigIn.setOnClickListener(v -> {
            if (mAuth.getCurrentUser() != null) {
                startActivity(new Intent(IntroActivity.this, MainActivity.class));
            } else {
                startActivity(new Intent(IntroActivity.this, SignInActivity.class));
            }
        });

        binding.btnSignUp.setOnClickListener(v -> startActivity(new Intent(IntroActivity.this, SignUpActivity.class)));
    }
}
