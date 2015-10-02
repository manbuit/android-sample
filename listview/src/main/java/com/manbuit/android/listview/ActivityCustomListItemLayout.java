package com.manbuit.android.listview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ActivityCustomListItemLayout extends AppCompatActivity {

    ListView listView;
    List<String> list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_list_item_layout);

        listView = (ListView) findViewById(R.id.listView1);

        list.add("custom_list_item.xml");
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(
                this,
                R.layout.custom_list_item,
                list
        );
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ActivityCustomListItemLayout.this, list.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
