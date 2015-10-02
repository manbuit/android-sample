package com.manbuit.android.listview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.manbuit.android.listview.Entity.Person;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActivityCustomListItemObject extends AppCompatActivity {

    ListView listView;
    List<Person> persons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_list_item_object);

        listView = (ListView) findViewById(R.id.listView1);
        ArrayAdapter arrayAdapter = new ArrayAdapter<Person>(
                this,
                android.R.layout.simple_expandable_list_item_1,
                persons
        );

        persons.add(new Person("陈宏伟",new Date(71,8,26)));
        persons.add(new Person("陈健雄",new Date(97,9,23)));

        listView.setAdapter(arrayAdapter);
    }
}
