package com.example.appfood.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.appfood.Activity.DetailActivity;
import com.example.appfood.Domain.Hotels;
import com.example.appfood.R;

import java.util.ArrayList;

public class HotelListAdapter extends RecyclerView.Adapter<HotelListAdapter.viewholder> {
    ArrayList<Hotels> items;
    Context context;

    public HotelListAdapter(ArrayList<Hotels> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public HotelListAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.viewholder_list_hotel, parent, false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelListAdapter.viewholder holder, int position) {

        holder.titleTxt.setText(items.get(position).getTitle());
        holder.timeTxt.setText(items.get(position).getTimeValue() + "min");
        holder.priceTxt.setText("$"+items.get(position).getPrice());
        holder.rateTxt.setText(""+items.get(position).getStar());

        Glide.with(context)
                .load(items.get(position).getImagePath())
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.pic);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("object", items.get(position));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{
        TextView titleTxt, priceTxt, rateTxt, timeTxt;
        ImageView pic;
        public viewholder(@NonNull View itemView) {
            super(itemView);


            titleTxt = itemView.findViewById(R.id.txtTitleItem);
            priceTxt = itemView.findViewById(R.id.priceTxtItem);
            rateTxt = itemView.findViewById(R.id.rateTxtItem);
            timeTxt = itemView.findViewById(R.id.timeTxtItem);
            pic = itemView.findViewById(R.id.imgItem);
        }
    }
}
