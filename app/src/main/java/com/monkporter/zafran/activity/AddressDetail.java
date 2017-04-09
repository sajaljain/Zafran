package com.monkporter.zafran.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.monkporter.zafran.R;
import com.monkporter.zafran.adapter.AddressDetailAdapter;
import com.monkporter.zafran.model.AddressDetailAttributes;

import java.util.ArrayList;
import java.util.List;

public class AddressDetail extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AddressDetailAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar_id);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

    /*  actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.actionbar_address_layout,null);
        actionBar.setCustomView(v);
    */

        recyclerView = (RecyclerView) findViewById(R.id.address_detail_recyclerview);
        adapter = new AddressDetailAdapter(AddressDetail.this, getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(AddressDetail.this));
    }

    private List<AddressDetailAttributes> getData() {
        List<AddressDetailAttributes> data = new ArrayList<>();
        String usrName[] = {"Ram", "Sham", "Kam", "Num", "Bum"};
        String addLine1[] = {"H.No - 1093", "H.No - 1093", "H.No - 1093", "H.No - 1093", "H.No - 1093"};
        String addLine2[] = {"Sector - 7, Urban Estate", "Sector - 7, Urban Estate", "Sector - 7, Urban Estate", "Sector - 7, Urban Estate", "Sector - 7, Urban Estate"};
        String city[] = {"Karnal", "Karnal", "Karnal", "Karnal", "Karnal"};
        for (int i = 0; i < usrName.length; i++) {
            AddressDetailAttributes current = new AddressDetailAttributes();
            current.userName = usrName[i];
            current.addressLine1 = addLine1[i];
            current.addressLine2 = addLine2[i];
            current.areaAndCity = city[i];
            data.add(current);
        }
        return data;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem menuItem = menu.findItem(R.id.action_notification);
        menuItem.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_button_id) {
            Toast.makeText(AddressDetail.this, "Add Button Clicked", Toast.LENGTH_SHORT).show();
            AddressDetailAttributes current = new AddressDetailAttributes();
            current.userName = "Vaibhav";
            current.addressLine1 = "H.No - 1093";
            current.addressLine2 = "Sector - 7, Urban Estate";
            current.areaAndCity = "Karnal";
            adapter.insert(current);
            return true;
        }
        if (id == android.R.id.home) {
            this.finish();
            // Toast.makeText(AddressDetail.this, "Back Button", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
