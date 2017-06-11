package com.monkporter.zafran.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.monkporter.zafran.R;
import com.monkporter.zafran.model.OrderItem;

import java.util.Collections;
import java.util.List;

/**
 * Created by Vaibhav on 5/24/2016.
 */
public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.MyViewHolder>  {
    List<OrderItem> itemList = Collections.emptyList();
    private LayoutInflater inflator;
    public TextView cartQuantity;
    public TextView cartPrice;
    View cartBar;
    int totalPrice,totalQuantity;
    int incre;
    Context context;

    public ItemListAdapter(Context contexti, List<OrderItem> itemList, TextView cartPrice, TextView cartQuantity, View cartBar){
        this.cartQuantity = cartQuantity;
        this.cartPrice = cartPrice;
        this.cartBar = cartBar;
        this.itemList = itemList;
        incre = itemList.get(itemList.size()-1).price;
        context = contexti;
        inflator = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = inflator.inflate(R.layout.order_item_view,parent ,false);
        MyViewHolder holder = new MyViewHolder(layoutView);

        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final OrderItem current = itemList.get(position);
        holder.listTitle.setText("Rs "+current.price);
        holder.listDetail.setText("Set of "+current.set);
        holder.listImage.setImageResource(current.photo);

        holder.listButton.setText("ADD");
        holder.listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.listButton.setVisibility(View.GONE);
                holder.incre.setText("+");
                holder.incre.setVisibility(View.VISIBLE);
                holder.decre.setText("-");
                holder.decre.setVisibility(View.VISIBLE);
                holder.quantity.setText(""+current.set);
                holder.quantity.setVisibility(View.VISIBLE);
                totalQuantity += current.set;
                totalPrice += current.price;
                cartPrice.setText("Rs."+totalPrice);
                cartQuantity.setText(""+totalQuantity);
                cartBar.setVisibility(View.VISIBLE);


            }
        });
        holder.incre.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                current.set+=1;

                current.price = current.price+incre;
                totalPrice += incre;
                totalQuantity++;
                holder.quantity.setText(""+current.set);
                cartPrice.setText("Rs."+totalPrice);
                cartQuantity.setText(""+totalQuantity);
                if(current.set > 0){
                    cartBar.setVisibility(View.VISIBLE);
                }


            }
        });


        holder.decre.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(current.set > 0) {
                    current.set -= 1;
                    totalQuantity -= 1;
                    current.price -= incre ;
                    totalPrice -= incre;
                    cartBar.setVisibility(View.VISIBLE);
                }
                if(totalQuantity == 0){
                    cartBar.setVisibility(View.GONE);
                }

                holder.quantity.setText(""+current.set);
                cartPrice.setText("Rs."+totalPrice);
                cartQuantity.setText(""+totalQuantity);


            }
        });


    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView listDetail;
        public TextView listTitle;
        public ImageView listImage;
        public TextView listButton;
        public TextView incre;
        public TextView decre;
        public TextView quantity;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            listTitle = (TextView) itemView.findViewById(R.id.item_title);
            listDetail = (TextView) itemView.findViewById(R.id.item_detail);
            listImage = (ImageView) itemView.findViewById(R.id.item_image);
            listButton = (TextView) itemView.findViewById(R.id.item_button);
            incre = (TextView) itemView.findViewById(R.id.item_inc);
            decre = (TextView) itemView.findViewById(R.id.item_dec);
            quantity = (TextView) itemView.findViewById(R.id.item_quantity);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), "Clicked Position = " + getAdapterPosition(), Toast.LENGTH_LONG).show();
        }
    }
}

