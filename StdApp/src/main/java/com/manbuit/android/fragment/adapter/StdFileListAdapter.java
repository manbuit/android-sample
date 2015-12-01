package com.manbuit.android.fragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.manbuit.android.fragment.R;
import com.manbuit.android.fragment.model.StdEntity;
import com.manbuit.android.fragment.model.StdFileEntity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MB on 2015/11/25.
 */
public class StdFileListAdapter extends BaseAdapter {
    List<StdFileEntity> stdFileEntities = new ArrayList<StdFileEntity>();

    private LayoutInflater mInflater = null;
    public StdFileListAdapter(Context context, List<StdFileEntity> stdFileEntities){
        super();
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.stdFileEntities = stdFileEntities;
    }

    public StdFileListAdapter(Context context, JSONArray jsonArray) throws Exception{
        super();

        List<StdFileEntity> stdFileEntities = new ArrayList<StdFileEntity>();
        for(int i=0;i< jsonArray.length();i++){
            JSONObject item = jsonArray.getJSONObject(i);
            String id = item.getString("id");
            String name = item.getString("name");
            StdFileEntity stdFileEntity = new StdFileEntity(id,name);
            stdFileEntities.add(stdFileEntity);
        }

        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.stdFileEntities = stdFileEntities;
    }

    @Override
    public int getCount() {
        //return cities.length;
        return stdFileEntities.size();
    }

    @Override
    public StdFileEntity getItem(int position) {
        //return cities[position];
        return stdFileEntities.get(position);
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

            view = mInflater.inflate(R.layout.std_file_list_item, null);
        }

        //((TextView)textView).setText(person.getName());

        //TextView idView = (TextView) ((LinearLayout)view).findViewById(R.id.sliFileId);
        TextView nameView = (TextView) ((LinearLayout)view).findViewById(R.id.sliFileName);

        StdFileEntity stdFileEntity = (StdFileEntity) this.getItem(position);
        //idView.setText(stdFileEntity.getId());
        //nameView.setText(String.format("《%s》",stdFileEntity.getName()));
        nameView.setText(stdFileEntity.getName());

        return view;
    }
}
