package com.manbuit.android.listview;

import android.provider.DocumentsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.manbuit.android.listview.Entity.Person;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActivityCustomListItemObjectAndLayout extends AppCompatActivity {

    ListView listView;
    List<Person> persons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_list_item_object_and_layout);

        listView = (ListView) findViewById(R.id.listView1);

        /*ArrayAdapter arrayAdapter = new ArrayAdapter<Person>(
                this,
                R.layout.custom_list_item2,
                persons
        );*/

        persons.add(new Person("陈宏伟", new Date(71, 8, 26)));
        persons.add(new Person("陈健雄", new Date(97, 9, 23)));

        /*for(int i=0;i<100;i++) {
            persons.add(new Person("陈宏伟", new Date(71, 8, 26)));
            persons.add(new Person("陈健雄", new Date(97, 9, 23)));
        }*/

        listView.setAdapter(adapter);
    }

    private BaseAdapter adapter = new BaseAdapter() {

        @Override
        public int getCount() {
            return persons.size();
        }

        @Override
        public Object getItem(int position) {
            return persons.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Person person = (Person) this.getItem(position);

            View view = convertView;
            if(view == null) {
                //textView = new TextView(ActivityCustomListItemObjectAndLayout.this);
                view = LayoutInflater
                        .from(ActivityCustomListItemObjectAndLayout.this)
                        .inflate(R.layout.custom_list_item2, null);
            }

            //((TextView)textView).setText(person.getName());

            TextView nameView = (TextView) ((LinearLayout)view).findViewById(R.id.nameView);
            TextView birthDayView = (TextView) ((LinearLayout)view).findViewById(R.id.birthDayView);

            nameView.setText(person.getName());
            birthDayView.setText(person.getBirthDay().toString());

            return view;
        }
    };
}
