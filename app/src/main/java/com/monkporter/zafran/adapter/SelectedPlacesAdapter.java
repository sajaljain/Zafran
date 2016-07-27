package com.monkporter.zafran.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.monkporter.zafran.R;
import com.monkporter.zafran.helper.PrefManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by Vaibhav on 7/5/2016.
 */
public class SelectedPlacesAdapter extends RecyclerView.Adapter<SelectedPlacesAdapter.MyViewHolder> {
    private ArrayList<PlacesAutoCompleteAdapter.PlaceAutocomplete> mResultList;
    LayoutInflater inflater;
    Context context;
    public SelectedPlacesAdapter(Context context,ArrayList<PlacesAutoCompleteAdapter.PlaceAutocomplete> arrayList){
        this.context = context;
        mResultList = arrayList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.searchview_adapter,parent,false);
        MyViewHolder holder = new MyViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.delivery_address.setText(mResultList.get(position).description);
    }

    @Override
    public int getItemCount() {
        if(mResultList != null)
            return mResultList.size();
        else
            return 0;
    }

    public void insertItem(PlacesAutoCompleteAdapter.PlaceAutocomplete item){
        mResultList.add(0,item);
        notifyItemInserted(0);
    //    PrefManager prefManager = new PrefManager(context);
     //   prefManager.saveLocations(mResultList);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView delivery_address;
        public MyViewHolder(View itemView) {
            super(itemView);
            delivery_address = (TextView) itemView.findViewById(R.id.address);
        }
    }
}
