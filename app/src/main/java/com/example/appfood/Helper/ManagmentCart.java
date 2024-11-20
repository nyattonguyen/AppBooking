package com.example.appfood.Helper;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;


import com.example.appfood.Domain.Hotels;
import com.example.appfood.Helper.TinyDB;

import java.util.ArrayList;


public class ManagmentCart {
    private Context context;
    private TinyDB tinyDB;

    public ManagmentCart(Context context) {
        this.context = context;
        this.tinyDB=new TinyDB(context);
    }

//    public void insertHotel(Hotels item) {
//        ArrayList<Hotels> listpop = getListCart();
//        boolean existAlready = false;
//        int n = 0;
//        for (int i = 0; i < listpop.size(); i++) {
//            if (listpop.get(i).getTitle().equals(item.getTitle())) {
//                existAlready = true;
//                n = i;
//                break;
//            }
//        }
//        if(existAlready){
//            listpop.get(n).setNumberInCart(item.getNumberInCart());
//        }else{
//            listpop.add(item);
//        }
//        tinyDB.putListObject("CartList",listpop);
//        Toast.makeText(context, "Added to your Cart", Toast.LENGTH_SHORT).show();
//    }
    public void insertHotel(Hotels item) {
        ArrayList<Hotels> listCart = getListCart();
        boolean existAlready = false;

        // Check for existing item by ID instead of title
        for (Hotels existingItem : listCart) {
            if (existingItem.getId() == (item.getId())) {
                existAlready = true;
                existingItem.setNumberInCart(existingItem.getNumberInCart() + item.getNumberInCart());
                break;
            }
        }

        if (!existAlready) {
            listCart.add(item);
        }

        tinyDB.putListObject("CartList", listCart);
        Toast.makeText(context, "Added to your Cart", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<Hotels> getListCart() {
        ArrayList<Hotels> listCart = tinyDB.getListObject("CartList");
        if (listCart == null) {
            listCart = new ArrayList<>(); // Create an empty list if it's null
        }

        return listCart;
    }

    public void clearList() {
        tinyDB.remove("CartList"); // Clear the cart list from TinyDB
        Toast.makeText(context, "Cart cleared successfully", Toast.LENGTH_SHORT).show();
    }

    public void removeItem(ArrayList<Hotels> listItem, int id, ChangeNumberItemsListener changeNumberItemsListener) {
        int positionToRemove = -1;

        // Find item by ID
        for (int i = 0; i < listItem.size(); i++) {
            if (listItem.get(i).getId() == id) {
                positionToRemove = i;
                break;
            }
        }

        if (positionToRemove != -1) {
            listItem.remove(positionToRemove);
            tinyDB.putListObject("CartList", listItem);
            Toast.makeText(context, "Item removed from cart", Toast.LENGTH_SHORT).show();
            // Notify adapter of data change to update UI
            tinyDB.putListObject("CartList",listItem);
            changeNumberItemsListener.change();
        } else {
            Toast.makeText(context, "Item not found in cart", Toast.LENGTH_SHORT).show();
        }
    }

    public Double getTotalFee(){
        ArrayList<Hotels> listItem=getListCart();
        double fee=0;
        for (int i = 0; i < listItem.size(); i++) {
            fee=fee+(listItem.get(i).getPrice()*listItem.get(i).getNumberInCart());
        }
        return fee;
    }
    public void minusNumberItem(ArrayList<Hotels> listItem,int position,ChangeNumberItemsListener changeNumberItemsListener){
        if(listItem.get(position).getNumberInCart()==1){
            listItem.remove(position);
        }else{
            listItem.get(position).setNumberInCart(listItem.get(position).getNumberInCart()-1);
        }
        tinyDB.putListObject("CartList",listItem);
        changeNumberItemsListener.change();
    }
    public void plusNumberItem(ArrayList<Hotels> listItem,int position,ChangeNumberItemsListener changeNumberItemsListener){
        listItem.get(position).setNumberInCart(listItem.get(position).getNumberInCart()+1);
        tinyDB.putListObject("CartList",listItem);
        changeNumberItemsListener.change();
    }
}
