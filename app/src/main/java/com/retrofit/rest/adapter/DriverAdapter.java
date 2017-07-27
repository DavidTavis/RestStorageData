package com.retrofit.rest.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.retrofit.rest.R;
import com.retrofit.rest.data.model.Driver;
import com.retrofit.rest.util.Util;

import java.util.List;

/**
 * Created by TechnoA on 24.07.2017.
 */

public class DriverAdapter extends RecyclerView.Adapter<DriverAdapter.DriverViewHolder>{

    List<Driver> drivers;
    private Context context;

    public static class DriverViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView firstName;
        TextView lastName;


        public DriverViewHolder(View itemView) {
            super(itemView);
            this.cardView = (CardView) itemView.findViewById(R.id.card_view);
            this.firstName = (TextView) itemView.findViewById(R.id.tv_first_name);
            this.lastName = (TextView) itemView.findViewById(R.id.tv_last_name);
        }

    }

    public DriverAdapter(List<Driver> drivers, Context context) {
        this.drivers = drivers;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return drivers.size();
    }

    public void addItem(Driver driver) {
        drivers.add(driver);
        notifyItemInserted(drivers.size());
    }

    public void removeItem(int position) {
        drivers.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, drivers.size());
    }
    @Override
    public DriverViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.driver_list_item, viewGroup, false);
        DriverViewHolder driverViewHolder = new DriverViewHolder(v);

        return driverViewHolder;
    }

    @Override
    public void onBindViewHolder(DriverViewHolder driverViewHolder, int i) {
        driverViewHolder.firstName.setText(drivers.get(i).getFirstName());
        driverViewHolder.lastName.setText(drivers.get(i).getLastName());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
