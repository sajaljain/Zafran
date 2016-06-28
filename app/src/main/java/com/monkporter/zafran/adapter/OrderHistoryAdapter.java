package com.monkporter.zafran.adapter;

/**
 * Created by Vaibhav on 6/28/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.monkporter.zafran.R;
import com.monkporter.zafran.model.OrderHistoryAttribute;

import java.util.Collections;
import java.util.List;

/**
 * Created by Vaibhav on 6/28/2016.
 */
public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.MyViewHolder> {
    private final LayoutInflater inflator;
    List<OrderHistoryAttribute> orderHistoryList = Collections.emptyList();
    Context context;

    public OrderHistoryAdapter(Context context, List<OrderHistoryAttribute> orderHistoryList){
        this.context = context;
        this.orderHistoryList = orderHistoryList;
        inflator = LayoutInflater.from(context);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = inflator.inflate(R.layout.order_history_row_view,parent,false);
        MyViewHolder holder = new MyViewHolder(layoutView);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final OrderHistoryAttribute current = orderHistoryList.get(position);
        holder.order_number.setText(current.orderNumber);
        holder.schedule_time.setText(current.orderTime);
        holder.total_amount.setText(current.price);
        holder.vender_name.setText(current.venderName);
        holder.vender_worktime.setText(current.venderWorkHours);
        holder.sub_total_amount.setText(current.price);
        holder.callIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall(current.mobileNumber);
            }
        });
    }

    void makeCall(String mobileNumber){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+mobileNumber));
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return orderHistoryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView order_number;
        TextView schedule_time;
        TextView sub_total_amount;
        TextView vender_name;
        TextView vender_worktime;
        TextView total_amount;
        ImageView callIcon;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            order_number = (TextView) itemView.findViewById(R.id.history_order_id);
            schedule_time = (TextView) itemView.findViewById(R.id.delivery_date_value_id);
            sub_total_amount = (TextView) itemView.findViewById(R.id.subtotal_amt_value_id);
            vender_name = (TextView) itemView.findViewById(R.id.vender_name_id);
            vender_worktime = (TextView) itemView.findViewById(R.id.work_hours_id);
            total_amount = (TextView) itemView.findViewById(R.id.total_amt_value_id);
            callIcon = (ImageView) itemView.findViewById(R.id.call_button_id);
        }

        @Override
        public void onClick(View v) {

        }
    }
}

