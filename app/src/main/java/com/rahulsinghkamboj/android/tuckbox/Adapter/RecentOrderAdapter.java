package com.rahulsinghkamboj.android.tuckbox.Adapter;

import android.support.v7.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rahulsinghkamboj.android.tuckbox.Model.Orders;
import com.rahulsinghkamboj.android.tuckbox.R;

import java.util.List;

public class RecentOrderAdapter extends RecyclerView.Adapter<RecentOrderAdapter.MyViewHolder> {

    private List<Orders> ordersList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mealName, mealOption, location, address, timings;

        public MyViewHolder(View view) {
            super(view);
            mealName = (TextView) view.findViewById(R.id.tv_mealname);
            mealOption = (TextView) view.findViewById(R.id.tv_mealoption);
            location = (TextView) view.findViewById(R.id.tv_location);
            address = (TextView) view.findViewById(R.id.tv_address);
            timings = (TextView) view.findViewById(R.id.tv_delivery);
        }
    }


    public RecentOrderAdapter(List<Orders> ordersList) {
        this.ordersList = ordersList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recent_orders_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Orders orders = ordersList.get(position);
        holder.mealName.setText(orders.getMealName());
        holder.mealOption.setText(orders.getMealOption());
        holder.location.setText("Location: "+orders.getLocation());
        holder.address.setText("Address: "+orders.getAddress());
        holder.timings.setText("Delivery Timings: "+orders.getTimings());
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }
}