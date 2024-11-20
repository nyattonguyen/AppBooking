package com.example.appfood.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.appfood.Adapter.HotelListAdapter;
import com.example.appfood.Domain.Hotels;
import com.example.appfood.R;
import com.example.appfood.databinding.ActivityListHotelActitityBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListHotelActitity extends BaseActivity {
    ActivityListHotelActitityBinding binding;
    private RecyclerView.Adapter adapterListHotel;
    private int categoryId;
    private String categoryName, searchText;
    private boolean isSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListHotelActitityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntentExtra();
        initList();
    }

    private void initList() {
        DatabaseReference myRef = database.getReference("Hotels");
        binding.progressBar.setVisibility(View.VISIBLE);
        ArrayList<Hotels> list = new ArrayList<>();

        Query query;
        if(isSearch) {
            query = myRef.orderByChild("Title").startAt(searchText).endAt(searchText+'\uf8ff');
        }else {
            query = myRef.orderByChild("CategoryId").equalTo(categoryId);
        }
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for(DataSnapshot issue : snapshot.getChildren()) {
                        list.add(issue.getValue(Hotels.class));
                    }
                    if(list.size() > 0) {
                        binding.hotelListView.setLayoutManager(new GridLayoutManager(ListHotelActitity.this, 2));
                        adapterListHotel = new HotelListAdapter(list);
                        binding.hotelListView.setAdapter(adapterListHotel);
                    }
                    binding.progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getIntentExtra() {
        categoryId = getIntent().getIntExtra("categoryId", 0);
        categoryName = getIntent().getStringExtra("Category");
        searchText = getIntent().getStringExtra("text");
        isSearch = getIntent().getBooleanExtra("isSearch", false);

        binding.titleTxt.setText(categoryName);
        binding.backBtn.setOnClickListener(v -> finish());
    }
}