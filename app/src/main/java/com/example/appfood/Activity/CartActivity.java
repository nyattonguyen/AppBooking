package com.example.appfood.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appfood.Adapter.CartAdapter;
import com.example.appfood.Domain.Hotels;
import com.example.appfood.Domain.Order;
import com.example.appfood.Helper.ManagmentCart;
import com.example.appfood.R;
import com.example.appfood.databinding.ActivityCartBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CartActivity extends BaseActivity {
    private ActivityCartBinding binding;
    private RecyclerView.Adapter adapter;
    private ManagmentCart managmentCart;
    private double tax;

    private Calendar checkInDate;
    private Calendar checkOutDate;
    private double totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managmentCart = new ManagmentCart(this);

        setVariable();
        calculateCart();
        initList();
        ConfirmOrder();
        setupDatePickers();
    }

    private void setupDatePickers() {
        checkInDate = Calendar.getInstance();
        checkOutDate = Calendar.getInstance();

        binding.btnShowDateCheckIn.setOnClickListener(v -> showDatePickerDialog(binding.btnShowDateCheckIn, true));
        binding.btnShowDateCheckOut.setOnClickListener(v -> {
            if (checkInDate == null) {
                Toast.makeText(this, "Please select check-in date first!", Toast.LENGTH_SHORT).show();
            } else {
                showDatePickerDialog(binding.btnShowDateCheckOut, false);
            }
        });
    }

    private void showDatePickerDialog(EditText editText, boolean isCheckIn) {
        Calendar selectedDate = isCheckIn ? checkInDate : checkOutDate;

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    selectedDate.set(Calendar.YEAR, year);
                    selectedDate.set(Calendar.MONTH, month);
                    selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    editText.setText(sdf.format(selectedDate.getTime()));

                    if (isCheckIn) {
                        binding.btnShowDateCheckOut.setText("");
                        checkOutDate = Calendar.getInstance(); // Reset lại Check-Out
                    } else if (checkOutDate.before(checkInDate)) {
                        Toast.makeText(this, "Check-out date cannot be before check-in date!", Toast.LENGTH_SHORT).show();
                        checkOutDate.setTime(checkInDate.getTime());
                        binding.btnShowDateCheckOut.setText(sdf.format(checkOutDate.getTime()));
                    } else {
                        updateTotalPrice();
                    }
                },
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void initList() {
        if(managmentCart.getListCart().isEmpty()) {
            binding.viewEmpty.setVisibility(View.VISIBLE);
            binding.scrollViewCart.setVisibility(View.GONE);
        } else {
            binding.viewEmpty.setVisibility(View.GONE);
            binding.scrollViewCart.setVisibility(View.VISIBLE);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.cardView.setLayoutManager(linearLayoutManager);
        adapter = new CartAdapter(managmentCart.getListCart(), this, () -> calculateCart());
        binding.cardView.setAdapter(adapter);
    }

    private long calculateNumberOfNights(String checkIn, String checkOut) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date dateIn = dateFormat.parse(checkIn);
            Date dateOut = dateFormat.parse(checkOut);
            if (dateIn != null && dateOut != null) {
                long diffInMillis = dateOut.getTime() - dateIn.getTime();
                return TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "Invalid date format!", Toast.LENGTH_SHORT).show();
        }
        return 0;
    }

    private void updateTotalPrice() {
        String dateCheckIn = binding.btnShowDateCheckIn.getText().toString();
        String dateCheckOut = binding.btnShowDateCheckOut.getText().toString();

        if (!dateCheckIn.isEmpty() && !dateCheckOut.isEmpty()) {
            double totalPrice = calculateTotalPrice(dateCheckIn, dateCheckOut, managmentCart.getTotalFee());
            binding.txtTotalCart.setText(String.format(Locale.getDefault(), "Total Price: %.2f", totalPrice));
        }
    }

    private double calculateTotalPrice(String checkIn, String checkOut, double pricePerNight) {
        long numberOfNights = calculateNumberOfNights(checkIn, checkOut);
        return pricePerNight * numberOfNights;
    }

//    private void ConfirmOrder() {
//        binding.btnConfirmOrder.setOnClickListener(v -> {
//            SweetAlertDialog alertDialog = new SweetAlertDialog(CartActivity.this, SweetAlertDialog.SUCCESS_TYPE);
//            alertDialog.setTitleText("Your order successfully!");
//
//            alertDialog.setConfirmClickListener(sDialog -> {
//                Order order = new Order();
//
//                ManagmentCart managmentCart = new ManagmentCart(CartActivity.this);
//                ArrayList<Hotels> cartList = managmentCart.getListCart();
//
//                order.setTotalPrice(managmentCart.getTotalFee());
//
//                //
//                setCustomerInfo(order);
////                order.setCustomerName(getCustomerName());
//                order.setPhone(getCustomerPhone());
//
//                if (!cartList.isEmpty()) {
//                    order.setIdHotel(cartList.get(0).getId());
//                }
//
//                sendOrderToDatabase(order, new OrderResponseCallback() {
//                    @Override
//                    public void onSuccess() {
//                        sDialog.dismissWithAnimation();
//                        managmentCart.clearList();
//                        adapter.notifyItemRangeRemoved(0, managmentCart.getListCart().size());
//                        adapter.notifyDataSetChanged();
//                        Button btn = (Button) alertDialog.findViewById(cn.pedant.SweetAlert.R.id.confirm_button);
//                        btn.setBackgroundColor(ContextCompat.getColor(CartActivity.this, R.color.red));
//                        Intent intent = new Intent(CartActivity.this, MainActivity.class);
//                        startActivity(intent);
//                    }
//
//                    @Override
//                    public void onFailure(String errorMessage) {
//                        Toast.makeText(CartActivity.this, "", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }).show();
//        });
//    }

    private void ConfirmOrder() {
        binding.btnConfirmOrder.setOnClickListener(v -> {
            SweetAlertDialog alertDialog = new SweetAlertDialog(CartActivity.this, SweetAlertDialog.SUCCESS_TYPE);
            alertDialog.setTitleText("Your order successfully!");

            alertDialog.setConfirmClickListener(sDialog -> {
                Order order = new Order();

                ManagmentCart managmentCart = new ManagmentCart(CartActivity.this);
                ArrayList<Hotels> cartList = managmentCart.getListCart();

                order.setTotalPrice(managmentCart.getTotalFee());

                String dateCheckIn = binding.btnShowDateCheckIn.getText().toString();
                String dateCheckOut = binding.btnShowDateCheckOut.getText().toString();

                totalPrice = calculateTotalPrice(dateCheckIn, dateCheckOut, managmentCart.getTotalFee());
                order.setTotalPrice(totalPrice);
                setCustomerInfo(order);
//                order.setCustomerName(getCustomerName());
                order.setPhone(getCustomerPhone());

                if (!cartList.isEmpty()) {
                    order.setIdHotel(cartList.get(0).getId());
                }

                sendOrderToDatabase(order, new OrderResponseCallback() {
                    @Override
                    public void onSuccess() {
                        sDialog.dismissWithAnimation();
                        managmentCart.clearList();
                        adapter.notifyItemRangeRemoved(0, managmentCart.getListCart().size());
                        adapter.notifyDataSetChanged();
                        Button btn = (Button) alertDialog.findViewById(cn.pedant.SweetAlert.R.id.confirm_button);
                        btn.setBackgroundColor(ContextCompat.getColor(CartActivity.this, R.color.red));
                        Intent intent = new Intent(CartActivity.this, MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(CartActivity.this, "", Toast.LENGTH_SHORT).show();
                    }
                });
            }).show();
        });
    }

    private void calculateCart() {
        double percentTax = 0.02; //percent 2% tax
        double delivery = 10;//Dollar

        tax = Math.round(managmentCart.getTotalFee()*percentTax*100.0) /100;

        double total = Math.round((managmentCart.getTotalFee() + tax + delivery)*100) / 100;
        double itemTotal = Math.round(managmentCart.getTotalFee()*100)/100;

        binding.txtTotalFee.setText("$" +itemTotal);
        binding.txtTotalCart.setText("$" +total);

    }
    private void setVariable() {
        binding.btnBackCart.setOnClickListener(v -> finish());
    }

    private void sendOrderToDatabase(Order order, OrderResponseCallback callback) {
        // Get a reference to the Firebase Realtime Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("orders");
        // Push the new order to the "orders" node
        String orderId = databaseReference.push().getKey(); // Automatically generate a unique ID for the order

        if (orderId != null) {
            // Set the order data
            databaseReference.child(orderId).setValue(order)
                    .addOnSuccessListener(aVoid -> {
                        // Order created successfully
                        callback.onSuccess();
                    })
                    .addOnFailureListener(e -> {
                        // Failure to create order
                        callback.onFailure(e.getMessage());
                    });
        } else {
            callback.onFailure("Failed to generate order ID");
        }
    }


    private void setCustomerInfo(Order order) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Log.d("CurrentUser ", " " +user);
        if (user != null) {
            String userName = user.getDisplayName(); // Lấy tên người dùng
            String userId = user.getUid(); // Lấy ID (UUID) của người dùng

            // Set tên người dùng và ID vào order
            order.setCustomerName(userName != null && !userName.isEmpty() ? userName : "Anonymous User");
            order.setCustomerId(userId); // Set ID người dùng
        } else {
            Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show();

            order.setCustomerName("No user logged in");
            order.setCustomerId("No user logged in");
        }
    }
    private String getCustomerPhone() {
        return "123-456-7890";
    }

    public interface OrderResponseCallback {
        void onSuccess();
        void onFailure(String errorMessage);
    }
}