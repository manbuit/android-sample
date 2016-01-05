package com.manbuit.android.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.manbuit.android.fragment.adapter.StdFileListAdapter;
import com.manbuit.android.fragment.adapter.StdListAdapter;
import com.manbuit.android.fragment.dataRequest.Column;
import com.manbuit.android.fragment.dataRequest.DataRequest;
import com.manbuit.android.fragment.dataRequest.DataRequestUnit;
import com.manbuit.android.fragment.dataRequest.Filter;
import com.manbuit.android.fragment.dataRequest.OrderBy;
import com.manbuit.android.fragment.model.StdEntity;
import com.manbuit.android.fragment.utils.DownloadFileAsync;
import com.manbuit.android.fragment.utils.FileUtils;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class StdQueryDialogActivity extends AppCompatActivity/*Activity*//*AppCompatActivity*/ {

    StdApp global;

    RadioGroup rg_std_status;
    RadioGroup rg_std_category;
    EditText et_std_param;
    Button btnQuery;
    Button btnClear;
    Button btnExit;

    Bundle queryCond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_std_query_dialog);

        global = (StdApp) getApplication();

        rg_std_status = (RadioGroup) findViewById(R.id.rg_std_status);
        rg_std_category = (RadioGroup) findViewById(R.id.rg_std_category);
        et_std_param = (EditText) findViewById(R.id.et_std_param);

        //初始查询条件
        queryCond = getIntent().getExtras();

        { //初始化查询控件
            String status = queryCond.getString("status");
            switch (status) {
                case "在用":
                    ((RadioButton) rg_std_status.getChildAt(1)).setChecked(true);
                    break;
                case "作废":
                    ((RadioButton) rg_std_status.getChildAt(2)).setChecked(true);
                    break;
                default:
                    ((RadioButton) rg_std_status.getChildAt(0)).setChecked(true);
                    break;
            }

            String category = queryCond.getString("category");
            switch (category) {
                case "国家标准":
                    ((RadioButton) rg_std_category.getChildAt(1)).setChecked(true);
                    break;
                case "SN标准":
                    ((RadioButton) rg_std_category.getChildAt(2)).setChecked(true);
                    break;
                case "行业标准":
                    ((RadioButton) rg_std_category.getChildAt(3)).setChecked(true);
                    break;
                case "ISO标准":
                    ((RadioButton) rg_std_category.getChildAt(4)).setChecked(true);
                    break;
                case "ASTM标准":
                    ((RadioButton) rg_std_category.getChildAt(5)).setChecked(true);
                    break;
                case "JIS标准":
                    ((RadioButton) rg_std_category.getChildAt(6)).setChecked(true);
                    break;
                case "其他标准":
                    ((RadioButton) rg_std_category.getChildAt(7)).setChecked(true);
                    break;
                default:
                    ((RadioButton) rg_std_category.getChildAt(0)).setChecked(true);
                    break;
            }

            String codename = queryCond.getString("namecode");
            et_std_param.setText(codename);
        }

        btnQuery = (Button) findViewById(R.id.button_query);
        btnClear = (Button) findViewById(R.id.button_clear);
        btnExit = (Button) findViewById(R.id.button_exit);

        this.setTitle(null);

        final View.OnClickListener searchAction = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioButton rg_std_status_selected = (RadioButton) findViewById(rg_std_status.getCheckedRadioButtonId());
                RadioButton rg_std_category_selected = (RadioButton) findViewById(rg_std_category.getCheckedRadioButtonId());
                /*Toast.makeText(
                        StdQueryDialogActivity.this,
                        String.format("%s\r\n%s", rg_std_status_selected.getText(), rg_std_category_selected.getText()),
                        Toast.LENGTH_SHORT
                ).show();*/
                Intent resultIntent = new Intent();

                String status = String.valueOf(rg_std_status_selected.getText());
                if(status.equals("全部状态")){
                    status = "";
                }
                resultIntent.putExtra("status", status);

                String category = String.valueOf(rg_std_category_selected.getText());
                if(category.equals("全部类别")){
                    category = "";
                }
                resultIntent.putExtra("category",category);

                resultIntent.putExtra("namecode", String.valueOf(et_std_param.getText()));

                setResult(RESULT_OK, resultIntent);
                StdQueryDialogActivity.this.finish();
            }
        };

        btnQuery.setOnClickListener(searchAction);

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((RadioButton) rg_std_status.getChildAt(0)).setChecked(true);
                ((RadioButton) rg_std_category.getChildAt(0)).setChecked(true);
                et_std_param.setText(null);
                searchAction.onClick(view);
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                StdQueryDialogActivity.this.finish();
            }
        });

        et_std_param.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        searchAction.onClick(v);
                        break;
                }
                //Toast.makeText(this, v.getText()+"--" + actionId, Toast.LENGTH_LONG).show();
                return true;
            }
        });
    }

    /**
     * 加载标准类别
     * 暂未使用
     */
    private void loadStdCategory(){
        DataRequest dataRequest = new DataRequest();

        dataRequest.getNodes().put(
                "data",
                DataRequestUnit
                        .create("328524e7-1328-3557-baa7-ec6ede9dd681")
                        .setOrderbies(Arrays.asList(
                                        new OrderBy("orderNo", true)
                                )
                        )
        );

        dataRequest.request(global, new Handler() {
            public void handleMessage(Message msg) {
                try {
                    JSONObject result = (JSONObject) msg.obj;

                    JSONArray dataList = result.getJSONObject("data").getJSONArray("data");
                    //RadioGroup linearLayout = null;
                    for (int i = 0; i < dataList.length(); i++) {
                        JSONObject item = dataList.getJSONObject(i);

                        /*RadioButton radioButton = new RadioButton(StdQueryDialogActivity.this);
                        radioButton.setTextColor(Color.parseColor("#333333"));
                        radioButton.setText(item.getString("name"));
                        radioButton.setHint(item.getString("id"));
                        ((GridLayout)rg_std_category.findViewById(R.id.rg_std_category_grid))
                                .addView(radioButton, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);*/


                        RadioButton radioButtonLeft = new RadioButton(rg_std_category.getContext());
                        radioButtonLeft.setTextColor(Color.parseColor("#333333"));
                        radioButtonLeft.setText(item.getString("name"));
                        radioButtonLeft.setHint(item.getString("id"));
                        radioButtonLeft.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                        //linearLayout.addView(radioButtonLeft/*, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT*/);
                        rg_std_category.addView(radioButtonLeft);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
