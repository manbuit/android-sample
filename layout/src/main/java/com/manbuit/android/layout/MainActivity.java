package com.manbuit.android.layout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    List<String> list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);

        list.add("FrameLayout");
        list.add("LinearLayout");
        list.add("RelativeLayout");
        //list.add("AbsoluteLayout");  //已经淘汰，可以用RelativeLayout
        list.add("TableLayout");
        list.add("DyncLayout");
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_expandable_list_item_1,
                list
        );
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String layout = list.get(position);
                Toast.makeText(MainActivity.this, layout, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                intent.setClassName(MainActivity.this, "com.manbuit.android.layout.Activity_"+layout);
                startActivity(intent);
            }
        });
    }
}
