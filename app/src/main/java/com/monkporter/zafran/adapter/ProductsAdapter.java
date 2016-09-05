package com.monkporter.zafran.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.monkporter.zafran.R;
import com.monkporter.zafran.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Sajal on 26-May-16.
 */
public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {
    private List<Product> products;
    private Context context;
    public ProductsAdapter(Context context, List<Product> productsList) {
        this.products = productsList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_products, null);
        ViewHolder vh = new ViewHolder(layoutView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        holder.prodName.setText(products.get(position).getProductName());
        holder.prodDetail.setText(products.get(position).getProductSummary());
        Picasso.with(context).load(products.get(position).getImageUrl()).into(holder.prodThumbnail);
       // holder.prodThumbnail.setImageResource(Integer.parseInt(products.get(position).getImageUrl()));
    }

    @Override
    public int getItemCount() {
        return this.products.size();
    }
    public Product getItem(int positions){
        return products.get(positions);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView prodName;
        public TextView prodDetail;
        public ImageView prodThumbnail;
        public ViewHolder(View itemView) {
            super(itemView);
            prodName = (TextView) itemView.findViewById(R.id.prod_name);
            prodDetail = (TextView) itemView.findViewById(R.id.prod_desc);
            prodThumbnail = (ImageView) itemView.findViewById(R.id.prod_thumbnail);
        }
    }
}