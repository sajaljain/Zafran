package com.monkporter.zafran.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.monkporter.zafran.R;
import com.monkporter.zafran.adapter.OrderHistoryAdapter;
import com.monkporter.zafran.model.OrderHistoryAttribute;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vaibhav on 6/28/2016.
 */
public class OrderHistoryActivity extends AppCompatActivity{
    private RecyclerView recyclerView;
    private OrderHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_history_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.order_history_recyclerview);
        adapter = new OrderHistoryAdapter(OrderHistoryActivity.this,getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(OrderHistoryActivity.this));
    }

    private List<OrderHistoryAttribute> getData() {
        List<OrderHistoryAttribute> data = new ArrayList<>();
        String orderNo[] = {"A123","A123","A123","A123","A123"};
        String timeOFOrder[] = {"28 Jun 16,4:00-5:00PM","28 Jun 16,4:00-5:00PM","28 Jun 16,4:00-5:00PM","28 Jun 16,4:00-5:00PM","28 Jun 16,4:00-5:00PM"};
        String amount[] = {"Rs.100","Rs.100","Rs.100","Rs.100","Rs.100"};
        String name[] = {"Shyam","Shyam","Shyam","Shyam","Shyam"};
        String workHours[] = {"7:00AM-8:00PM","7:00AM-8:00PM","7:00AM-8:00PM","7:00AM-8:00PM","7:00AM-8:00PM"};
        String contact[] = {"123456789","9876543","8802017888","2222222","55555555"};
        for( int i = 0;i < name.length;i++){
            OrderHistoryAttribute current = new OrderHistoryAttribute();
            current.orderNumber = orderNo[i];
            current.orderTime = timeOFOrder[i];
            current.price = amount[i];
            current.venderName = name[i];
            current.venderWorkHours = workHours[i];
            current.mobileNumber = contact[i];
            data.add(current);
        }
        return data;
    }

}
