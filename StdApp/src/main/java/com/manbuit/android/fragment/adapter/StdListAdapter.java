package com.manbuit.android.fragment.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.manbuit.android.fragment.R;
import com.manbuit.android.fragment.dataRequest.DataRequest;
import com.manbuit.android.fragment.dataRequest.DataRequestUnit;
import com.manbuit.android.fragment.dataRequest.Filter;
import com.manbuit.android.fragment.model.StdEntity;
import com.manbuit.android.fragment.model.StdFileEntity;

import org.json.JSONArray;
import org.json.JSONObject;

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

    public StdListAdapter(Context context, JSONArray jsonArray) throws Exception{
        super();

        List<StdEntity> stdEntities = new ArrayList<StdEntity>();
        for(int i=0;i< jsonArray.length();i++){
            JSONObject item = jsonArray.getJSONObject(i);
            String id = item.getString("id");
            String code = item.getString("code");
            String name = item.getString("name");
            //String status = item.getString("status");
            StdEntity stdEntity = new StdEntity(id,code,name,null);
            stdEntities.add(stdEntity);
        }

        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.stdEntities = stdEntities;
    }

    @Override
    public int getCount() {
        return stdEntities.size();
    }

    @Override
    public StdEntity getItem(int position) {
        return stdEntities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 参考：http://www.tuicool.com/articles/MJFNBfF
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
//            convertView = mInflater.inflate(R.layout.std_list_item, null);
//            convertView = mInflater.inflate(R.layout.std_list_item, parent, true); //java.lang.UnsupportedOperationException: addView(View, LayoutParams) is not supported in AdapterView
            convertView = mInflater.inflate(R.layout.std_list_item, parent, false);

            holder = new ViewHolder();
            holder.codeView = (TextView) convertView.findViewById(R.id.sliCode);
            holder.nameView = (TextView) convertView.findViewById(R.id.sliName);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        StdEntity stdEntity = (StdEntity) this.getItem(position);
        holder.nameView.setText(String.format("%s",stdEntity.getName()));
        if(stdEntity.getStatus()!=null && stdEntity.getStatus().equals("作废")) {
            //codeView.setTextColor(Color.rgb(255, 0, 0));
            //codeView.setText(String.format("%s (%s)", stdEntity.getCode(), "作废"));
            //codeView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            //codeView.setText(Html.fromHtml(String.format("<font color='red'>%s (%s)</font>", stdEntity.getCode(), "作废")));
            holder.codeView.setText(Html.fromHtml(String.format("<font color='red'><strong>%s</strong></font>", stdEntity.getCode())));
            holder.codeView.setPaintFlags(holder.codeView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else {
            holder.codeView.setText(Html.fromHtml(String.format("<font color='#007F00'><strong>%s</strong></font>", stdEntity.getCode())));
        }

        return convertView;
    }

    private final class ViewHolder {
        TextView codeView;
        TextView nameView;
    }
}