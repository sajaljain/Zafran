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
    int count = 0;
    MyViewHolder myViewHolder = null;
    //View layoutScrollView;
    int pos,poss = -1;
    int p;
    int product_set;
    Context context;
    SharedPreferences sharedPreferences;
    public ItemListAdapter(Context contexti, List<OrderItem> itemList, TextView cartPrice, TextView cartQuantity, View cartBar){
        this.cartQuantity = cartQuantity;
        this.cartPrice = cartPrice;
        this.cartBar = cartBar;
        this.itemList = itemList;
        context = contexti;
        inflator = LayoutInflater.from(context);

        // layoutScrollView = inflator.inflate(R.id.scroll_view, LinearLayout,false);
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.PREF_FILE),context.MODE_PRIVATE);
        pos = sharedPreferences.getInt(context.getString(R.string.SET_POSITION),-1);
        product_set = sharedPreferences.getInt(context.getString(R.string.PRODUCT_SET),0);
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
        OrderItem cur;
        holder.listTitle.setText("Rs "+current.price);
        holder.listDetail.setText("Set of "+current.set);
        holder.listImage.setImageResource(current.photo);
        if(pos != -1 && pos == position){
            current.set = product_set;
            holder.listButton.setText("ADD");
            holder.listButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    myViewHolder.listButton.setText("ADD");
                    myViewHolder.listButton.setVisibility(View.VISIBLE);
                    myViewHolder.incre.setText("+");
                    myViewHolder.incre.setVisibility(View.GONE);
                    myViewHolder.decre.setText("-");
                    myViewHolder.decre.setVisibility(View.GONE);
                    myViewHolder.quantity.setText("" + product_set);
                    myViewHolder.quantity.setVisibility(View.GONE);

                    myViewHolder = holder;
                    p = position;
                    count = 1;
                    product_set = current.set;
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(context.getString(R.string.PRODUCT_SET),product_set);
                    pos = position;
                    editor.putInt(context.getString(R.string.SET_POSITION),pos);
                    editor.commit();
                    //notifyItem(position);
                    //new ItemListAdapter(context,itemList,cartPrice,cartQuantity,cartBar);

                    holder.listButton.setVisibility(View.GONE);
                    holder.incre.setText("+");
                    holder.incre.setVisibility(View.VISIBLE);
                    holder.decre.setText("-");
                    holder.decre.setVisibility(View.VISIBLE);
                    holder.quantity.setText(""+current.set);
                    holder.quantity.setVisibility(View.VISIBLE);

                    cartPrice.setText("Rs."+current.price);
                    cartQuantity.setText(""+current.set);
                    cartBar.setVisibility(View.VISIBLE);


                }
            });
            holder.listButton.setVisibility(View.GONE);
            holder.incre.setText("+");
            holder.incre.setVisibility(View.VISIBLE);
            holder.decre.setText("-");
            holder.decre.setVisibility(View.VISIBLE);
            holder.quantity.setText(""+product_set);
            holder.quantity.setVisibility(View.VISIBLE);
            cartPrice.setText("Rs."+product_set*10);

            cartQuantity.setText(""+product_set);
            cartBar.setVisibility(View.VISIBLE);
            myViewHolder = holder;
        }
        else{
            holder.listButton.setText("ADD");
            holder.listButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    myViewHolder.listButton.setText("ADD");
                    myViewHolder.listButton.setVisibility(View.VISIBLE);
                    myViewHolder.incre.setText("+");
                    myViewHolder.incre.setVisibility(View.GONE);
                    myViewHolder.decre.setText("-");
                    myViewHolder.decre.setVisibility(View.GONE);
                    myViewHolder.quantity.setText("" + product_set);
                    myViewHolder.quantity.setVisibility(View.GONE);

                    myViewHolder = holder;
                    p = position;
                    count = 1;
                    product_set = current.set;
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(context.getString(R.string.PRODUCT_SET),product_set);
                    pos = position;
                    editor.putInt(context.getString(R.string.SET_POSITION),pos);
                    editor.commit();
                    //notifyItem(position);
                    //new ItemListAdapter(context,itemList,cartPrice,cartQuantity,cartBar);

                    holder.listButton.setVisibility(View.GONE);
                    holder.incre.setText("+");
                    holder.incre.setVisibility(View.VISIBLE);
                    holder.decre.setText("-");
                    holder.decre.setVisibility(View.VISIBLE);
                    holder.quantity.setText(""+current.set);
                    holder.quantity.setVisibility(View.VISIBLE);

                    cartPrice.setText("Rs."+current.price);
                    cartQuantity.setText(""+current.set);
                    cartBar.setVisibility(View.VISIBLE);


                }
            });}
        holder.incre.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                current.set += 1;

                product_set = current.set;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(context.getString(R.string.PRODUCT_SET),product_set);
                pos = position;
                editor.putInt(context.getString(R.string.SET_POSITION),pos);
                editor.commit();
                current.price = 10*current.set;
                holder.quantity.setText(""+current.set);
                cartPrice.setText("Rs."+current.price);
                cartQuantity.setText(""+current.set);
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
                    product_set = current.set;
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(context.getString(R.string.PRODUCT_SET),product_set);
                    pos = position;
                    editor.putInt(context.getString(R.string.SET_POSITION),pos);
                    editor.commit();
                    current.price = 10*current.set;
                    cartBar.setVisibility(View.VISIBLE);
                }
                if(current.set == 0){
                    cartBar.setVisibility(View.GONE);
                }

                holder.quantity.setText(""+current.set);
                cartPrice.setText("Rs."+current.price);
                cartQuantity.setText(""+current.set);


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
        public Button listButton;
        public Button incre;
        public Button decre;
        public TextView quantity;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            listTitle = (TextView) itemView.findViewById(R.id.item_title);
            listDetail = (TextView) itemView.findViewById(R.id.item_detail);
            listImage = (ImageView) itemView.findViewById(R.id.item_image);
            listButton = (Button) itemView.findViewById(R.id.item_button);
            incre = (Button) itemView.findViewById(R.id.item_inc);
            decre = (Button) itemView.findViewById(R.id.item_dec);
            quantity = (TextView) itemView.findViewById(R.id.item_quantity);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), "Clicked Position = " + getAdapterPosition(), Toast.LENGTH_LONG).show();
        }
    }
}

