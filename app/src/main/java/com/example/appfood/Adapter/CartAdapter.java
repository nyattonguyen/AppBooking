package com.example.appfood.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.appfood.Activity.CartActivity;
import com.example.appfood.Domain.Hotels;
import com.example.appfood.Helper.ChangeNumberItemsListener;
import com.example.appfood.Helper.ManagmentCart;
import com.example.appfood.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.viewholder> {
    ArrayList<Hotels> list;
    private ManagmentCart managmentCart;
    public CartAdapter(ArrayList<Hotels> list, Context context, ChangeNumberItemsListener changeNumberItemsListener) {
        this.list = list;
        managmentCart = new ManagmentCart(context);
        this.changeNumberItemsListener = changeNumberItemsListener;
    }

    ChangeNumberItemsListener changeNumberItemsListener;
    @NonNull
    @Override
    public CartAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cart, parent, false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.viewholder holder, int position) {

        holder.title.setText(list.get(position).getTitle());
        holder.feeEachItem.setText(("$"+list.get(position).getNumberInCart() * list.get(position).getPrice()));
        holder.totalEachItem.setText(list.get(position).getNumberInCart()+" * $"+(
                list.get(position).getPrice()
                ));
        holder.num.setText(list.get(position).getNumberInCart()+"");

        Glide.with(holder.itemView.getContext())
                .load(list.get(position).getImagePath())
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.pic);

        holder.plusItem.setOnClickListener(v -> managmentCart.plusNumberItem(list, position, () -> {
            notifyDataSetChanged();
            changeNumberItemsListener.change();
        }));

        holder.minusItem.setOnClickListener(v -> managmentCart.minusNumberItem(list, position, () -> {
            notifyDataSetChanged();
            changeNumberItemsListener.change();
        }));

        holder.btnDeleteItem.setOnClickListener(v -> managmentCart.removeItem(list, list.get(position).getId(), () -> {
            notifyDataSetChanged();
            changeNumberItemsListener.change();
        }));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{
        TextView title, feeEachItem, plusItem, minusItem;
        ImageView pic;
        ImageView btnDeleteItem;
        TextView totalEachItem, num;
        AppCompatButton btnConfirmOrder;
        ConstraintLayout viewEmpty;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.txtTitle);
            pic = itemView.findViewById(R.id.imgItemCart);
            feeEachItem = itemView.findViewById(R.id.txtFeeEachItem);
            plusItem = itemView.findViewById(R.id.btnPlusCart);
            minusItem = itemView.findViewById(R.id.btnMinusCart);
            totalEachItem = itemView.findViewById(R.id.txtTotalEachItem);
            num = itemView.findViewById(R.id.txtNumberItem);
            btnConfirmOrder = itemView.findViewById(R.id.btnConfirmOrder);
            btnDeleteItem = itemView.findViewById(R.id.btnDeleteItem);
            viewEmpty = itemView.findViewById(R.id.viewEmpty);

        }
    }
}
