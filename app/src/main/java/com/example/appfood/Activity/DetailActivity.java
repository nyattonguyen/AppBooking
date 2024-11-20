package com.example.appfood.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.appfood.Domain.Hotels;
import com.example.appfood.Helper.ManagmentCart;
import com.example.appfood.R;
import com.example.appfood.databinding.ActivityDetailBinding;

public class DetailActivity extends BaseActivity {
    ActivityDetailBinding binding;
    private Hotels object;
    private int num = 1;
    private ManagmentCart managmentCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        getIntentExtra();
        getVariable();
    }

    private void getVariable() {
        managmentCart = new ManagmentCart(this);

        binding.backDetailBtn.setOnClickListener(v->finish());

        Glide.with(DetailActivity.this)
                .load(object.getImagePath())
                .into(binding.imgDetail);

        binding.priceDetailTxt.setText("$"+object.getPrice());
        binding.titleDetailTxt.setText(object.getTitle());
        binding.descriptionTxt.setText(object.getDescription());
        binding.rateDetailTxt.setText(object.getStar()+" Rating");
        binding.ratingBar.setRating((float) object.getStar());
        binding.totalTxt.setText(("$ "+num*object.getPrice()));

        binding.plusBtn.setOnClickListener(v -> {
            num = num+1;
            binding.numTxt.setText(num+" ");
            binding.totalTxt.setText("$"+(num * object.getPrice()));
        });
        binding.minusBtn.setOnClickListener(v -> {
            num = num-1;
            binding.numTxt.setText(num+" ");
            binding.totalTxt.setText("$"+(num * object.getPrice()));
        });

        binding.addBtn.setOnClickListener(v -> {
            object.setNumberInCart(num);
            managmentCart.insertHotel(object);
        });

    }

    private void getIntentExtra() {
        object = (Hotels) getIntent().getSerializableExtra("object");
    }

}