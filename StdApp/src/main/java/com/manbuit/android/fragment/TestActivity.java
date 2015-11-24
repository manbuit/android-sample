package com.manbuit.android.fragment;

import android.app.ActivityGroup;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TestActivity extends AppCompatActivity {

    RequestQueue mQueue;

    private StdApp global;

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        global = (StdApp) getApplication();

        mQueue = Volley.newRequestQueue(TestActivity.this);

        textView = (TextView) findViewById(R.id.textView);

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                //editText.getText().toString(),
                "http://222.190.98.24:8099/whoami;jsessionid="+global.getMyContext().get("token"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Toast.makeText(LoginActivity.this,s,Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject result = new JSONObject(s);
                            JSONObject data = result.getJSONObject("data");
                            textView.setText(data.getString("name"));
                            Toast.makeText(TestActivity.this,data.getString("name"),Toast.LENGTH_SHORT).show();
                        }
                        catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(TestActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(TestActivity.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                Map<String, String> map = new HashMap<String, String>();
                //map.put("username", username);
                //map.put("password", password);
                return map;
            }
        };
        mQueue.add(stringRequest);
    }
}
