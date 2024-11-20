
package com.example.appfood.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.appfood.Adapter.CartAdapter;
import com.example.appfood.Adapter.HistoryAdapter;
import com.example.appfood.Domain.Hotels;
import com.example.appfood.Domain.Order;
import com.example.appfood.Helper.ManagmentCart;
import com.example.appfood.R;
import com.example.appfood.databinding.ActivityCartBinding;
import com.example.appfood.databinding.ActivityHistoryBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class HistoryActivity extends BaseActivity {
    private ActivityHistoryBinding binding;
    private RecyclerView.Adapter adapter;
    private ArrayList<Order> ordersList = new ArrayList<>();
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseReference = FirebaseDatabase.getInstance().getReference("orders"); // Reference to "orders" node in Firebase

        fetchOrdersFromFirebase();
    }

    // Fetch Order data from Firebase and update RecyclerView
    private void fetchOrdersFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ordersList.clear(); // Clear the existing list

                // Loop through orders in Firebase and add to the list
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Order order = snapshot.getValue(Order.class);
                    if (order != null) {
                        ordersList.add(order);
                    }
                }

                // Update UI based on whether there are orders or not
                if (ordersList.isEmpty()) {
                    binding.viewEmpty.setVisibility(View.VISIBLE);
                    binding.scrollViewHistory.setVisibility(View.GONE);
                } else {
                    binding.viewEmpty.setVisibility(View.GONE);
                    binding.scrollViewHistory.setVisibility(View.VISIBLE);
                }

                updateRecyclerView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(HistoryActivity.this, "Error loading data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Update RecyclerView with the data from Firebase
    private void updateRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.historyView.setLayoutManager(linearLayoutManager);

        // Initialize the adapter if not already initialized
        if (adapter == null) {
            adapter = new HistoryAdapter(ordersList, this);  // Set the adapter for the first time
            binding.historyView.setAdapter(adapter); // Set it to the RecyclerView
        } else {
            // Notify the adapter that the data has changed
            adapter.notifyDataSetChanged();
        }
    }
}