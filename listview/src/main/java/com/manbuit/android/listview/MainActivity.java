package com.manbuit.android.listview;

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

        list.add("自定义布局");
        list.add("自定义对象");
        list.add("自定义对象和布局");
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_expandable_list_item_1,
                list
        );
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
                Toast.makeText(MainActivity.this,list.get(position), Toast.LENGTH_SHORT).show();
                switch(position){
                    case 0:
                        startActivity(new Intent(MainActivity.this,ActivityCustomListItemLayout.class));
                        break;
                    case 1:
                        startActivity(new Intent(MainActivity.this,ActivityCustomListItemObject.class));
                        break;
                    case 2:
                        startActivity(new Intent(MainActivity.this,ActivityCustomListItemObjectAndLayout.class));
                        break;
                    default: break;
                }
            }
        });
    }
}
