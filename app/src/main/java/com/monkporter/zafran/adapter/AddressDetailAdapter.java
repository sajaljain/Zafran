package com.monkporter.zafran.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.monkporter.zafran.R;
import com.monkporter.zafran.model.AddressDetailAttributes;
import com.monkporter.zafran.model.OrderHistoryAttribute;

import java.util.Collections;
import java.util.List;

/**
 * Created by Vaibhav on 6/29/2016.
 */
public class AddressDetailAdapter extends RecyclerView.Adapter<AddressDetailAdapter.MyViewHolder> {
    private final LayoutInflater inflator;


    List<AddressDetailAttributes> addressList = Collections.emptyList();
    Context context;

    public AddressDetailAdapter(Context context, List<AddressDetailAttributes> addressList){
        this.context = context;
        this.addressList = addressList;
        inflator = LayoutInflater.from(context);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = inflator.inflate(R.layout.address_detail_row_view,parent,false);
        MyViewHolder holder = new MyViewHolder(layoutView);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final AddressDetailAttributes current = addressList.get(position);
        holder.user_name.setText(current.userName);
        holder.address_line_1.setText(current.addressLine1);
        holder.address_line_2.setText(current.addressLine2);
        holder.city.setText(current.areaAndCity);
        holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(position);
            }
        });
    }

    private void delete(int position) {
        addressList.remove(position);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public void insert(AddressDetailAttributes current) {
        addressList.add(0,current);
        notifyItemInserted(0);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView address_line_1;
        TextView address_line_2;
        TextView city;
        TextView user_name;
        ImageView deleteIcon;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            user_name = (TextView) itemView.findViewById(R.id.user_name_id);
            address_line_1 = (TextView) itemView.findViewById(R.id.user_add1_id);
            address_line_2 = (TextView) itemView.findViewById(R.id.user_add2_id);
            city = (TextView) itemView.findViewById(R.id.user_city_id);
            deleteIcon = (ImageView) itemView.findViewById(R.id.delete_address_id);
        }

        @Override
        public void onClick(View v) {

        }
    }


}


