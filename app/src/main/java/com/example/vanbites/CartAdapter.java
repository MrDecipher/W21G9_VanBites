package com.example.vanbites;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.vanbites.entities.Food;
import com.example.vanbites.entities.OrderItem;

import java.text.DecimalFormat;
import java.util.List;

public class CartAdapter extends BaseAdapter {

    List<OrderItem> orderItems;
    DecimalFormat decFormat;

    public CartAdapter(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    @Override
    public int getCount() {
        return orderItems.size();
    }

    @Override
    public OrderItem getItem(int position) {
        return orderItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        // Create the DecimalFormat Instance
        decFormat = new DecimalFormat("$###,###.##");

        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cart, parent, false);
        }

        // Get the current OrderItem
        OrderItem orderItem = getItem(position);
        // Get the current Food
        Food food = orderItem.getFood();

        TextView textViewFoodName = view.findViewById(R.id.textViewFoodName);
        TextView textViewQuantity = view.findViewById(R.id.textViewFoodQuantity);
        TextView txtViewPrice = view.findViewById(R.id.txtViewPrice);
        Button btnIncrementFood = view.findViewById(R.id.btnIncrementFood);
        Button btnDecrementFood = view.findViewById(R.id.btnDecrementFood);
        Button btnDeleteFromOrder = view.findViewById(R.id.btnDeleteFromOrder);

        textViewFoodName.setText(food.getName());
        txtViewPrice.setText(decFormat.format(orderItem.getCost()));
        textViewQuantity.setText(String.valueOf(orderItem.getQuantity()));

        btnIncrementFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderItem.incrementQuantity();
                textViewQuantity.setText(String.valueOf(orderItem.getQuantity()));
                notifyDataSetChanged();
            }
        });

        btnDecrementFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderItem.decrementQuantity();
                textViewQuantity.setText(String.valueOf(orderItem.getQuantity()));
                notifyDataSetChanged();
            }
        });

        btnDeleteFromOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderItems.remove(position);
                notifyDataSetChanged();
            }
        });

        return view;
    }
}
