package com.manbuit.android.fragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.manbuit.android.fragment.R;
import com.manbuit.android.fragment.dataRequest.DataRequest;
import com.manbuit.android.fragment.dataRequest.DataRequestUnit;
import com.manbuit.android.fragment.dataRequest.Filter;
import com.manbuit.android.fragment.model.StdEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MB on 2015/11/25.
 */
public class StdListAdapter extends BaseAdapter {
    List<StdEntity> stdEntities = new ArrayList<StdEntity>();

    private LayoutInflater mInflater = null;
    public StdListAdapter(Context context, List<StdEntity> stdEntities){
        super();
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.stdEntities = stdEntities;
    }

    @Override
    public int getCount() {
        //return cities.length;
        return stdEntities.size();
    }

    @Override
    public StdEntity getItem(int position) {
        //return cities[position];
        return stdEntities.get(position);
    }

    @Override
    public long getItemId(int position) {
        //return position;
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if(view == null) {
            /*view = LayoutInflater
                    .from(ActivityCustomListItemObjectAndLayout.this)
                    .inflate(R.layout.std_list_item, null);*/

            view = mInflater.inflate(R.layout.std_list_item, null);
        }

        //((TextView)textView).setText(person.getName());

        TextView codeView = (TextView) ((LinearLayout)view).findViewById(R.id.sliCode);
        TextView nameView = (TextView) ((LinearLayout)view).findViewById(R.id.sliName);

        StdEntity stdEntity = (StdEntity) this.getItem(position);
        codeView.setText(stdEntity.getCode());
        nameView.setText(stdEntity.getName());

        return view;
    }
}
