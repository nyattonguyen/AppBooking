package com.example.appfood.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appfood.Activity.HistoryActivity;
import com.example.appfood.Domain.Order;
import com.example.appfood.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private ArrayList<Order> list;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm",  Locale.forLanguageTag("vi-VN"));

    public HistoryAdapter(ArrayList<Order> list, HistoryActivity historyActivity) {
        this.list = list;
    }
    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_item_history, parent, false);
        return new ViewHolder(view);
    }
    // Định dạng ngày và giờ thành "yyyy-MM-dd HH:mm"

    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder holder, int position) {
        Order order = list.get(position);

        holder.feeEachItem.setText("$" + order.getTotalPrice());
        holder.num.setText(String.valueOf(order.getQuantity()));
        holder.title.setText(order.getNameHotel() != null ? order.getNameHotel() : "Unknown Hotel");
        if (order.getDateCheckin() != null && order.getDateCheckout()  != null ) {
            String formattedDateCheckIn = dateFormat.format(order.getDateCheckin());
            String formattedDateCheckOut = dateFormat.format(order.getDateCheckout());

            holder.date.setText("Date: " + formattedDateCheckIn + "  " + formattedDateCheckOut);
        } else {
            holder.date.setText("Date: Unknown");
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, feeEachItem, totalEachItem, num, date;
        ImageView pic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.txtTitle);
            feeEachItem = itemView.findViewById(R.id.txtFeeEachItem);
            totalEachItem = itemView.findViewById(R.id.txtTotalEachItem);
            num = itemView.findViewById(R.id.txtNumberItem);
            pic = itemView.findViewById(R.id.imgItemHistory);
            date = itemView.findViewById(R.id.txtDate);
        }
    }
}
