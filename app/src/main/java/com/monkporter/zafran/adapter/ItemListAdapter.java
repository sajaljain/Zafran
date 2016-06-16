package com.monkporter.zafran.adapter;

/**
 * Created by Vaibhav on 6/15/2016.
 */
//public class ItemListAdapter {

//}




import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.monkporter.zafran.R;
import com.monkporter.zafran.model.OrderItem;

import java.security.AccessController;
import java.util.Collections;
import java.util.List;

/**
 * Created by Vaibhav on 5/24/2016.
 */
public class ItemListAdapter  extends RecyclerView.Adapter<ItemListAdapter.MyViewHolder>  {
    List<OrderItem> itemList = Collections.emptyList();
    private LayoutInflater inflator;
    private int pos = 0;
    public ItemListAdapter(Context context, List<OrderItem> itemList){
        this.itemList = itemList;
        inflator = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = inflator.inflate(R.layout.order_item_view,parent ,false);
        MyViewHolder holder = new MyViewHolder(layoutView);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder,final int position) {
        final OrderItem current = itemList.get(position);
        holder.listTitle.setText("Rs "+current.price);
        holder.listDetail.setText("Set of "+current.setOFCups);
        holder.listImage.setImageResource(current.photo);
        holder.listButton.setText("ADD");
        holder.listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos = position;
                holder.listButton.setVisibility(View.GONE);
                holder.incre.setText("+");
                holder.incre.setVisibility(View.VISIBLE);
                holder.decre.setText("-");
                holder.decre.setVisibility(View.VISIBLE);
                holder.quantity.setText(""+current.setOFCups);
                holder.quantity.setVisibility(View.VISIBLE);

                SpannableStringBuilder builder = new SpannableStringBuilder();
                builder.append(" ");
                ImageSpan i = new ImageSpan(inflator.getContext(), R.drawable.ic_add_shopping_cart);
                builder.setSpan(i,0,builder.length(), 0);
                builder.append(" Rs "+current.price);
                Snackbar snackbar = Snackbar.make(holder.itemView, builder, Snackbar.LENGTH_LONG).setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(Color.RED);
                snackbar.show();


            }
        });
        holder.incre.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                current.setOFCups += 1;
                current.price += 10;
                holder.quantity.setText(""+current.setOFCups);

                SpannableStringBuilder builder = new SpannableStringBuilder();
                builder.append(" ");
                ImageSpan i = new ImageSpan(inflator.getContext(), R.drawable.ic_add_shopping_cart);
                builder.setSpan(i,0,builder.length(), 0);
                builder.append(" Rs "+current.price);
                Snackbar snackbar = Snackbar.make(holder.itemView, builder, Snackbar.LENGTH_LONG).setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(Color.RED);
                snackbar.show();
            }
        });


        holder.decre.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(current.setOFCups > 0) {
                    current.setOFCups -= 1;
                    current.price -= 10;
                }
                holder.quantity.setText(""+current.setOFCups);

                SpannableStringBuilder builder = new SpannableStringBuilder();
                builder.append(" ");
                ImageSpan i = new ImageSpan(inflator.getContext(), R.drawable.ic_add_shopping_cart);
                builder.setSpan(i,0,builder.length(), 0);
                builder.append(" Rs "+current.price);
                Snackbar snackbar = Snackbar.make(holder.itemView, builder, Snackbar.LENGTH_LONG).setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(Color.RED);
                snackbar.show();
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
            Toast.makeText(view.getContext(), "Clicked Position = " + getPosition(), Toast.LENGTH_LONG).show();
        }
    }
}

