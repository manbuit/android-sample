package com.manbuit.android.volley;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import static com.android.volley.Request.*;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView textView;

    RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        editText = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.textView);

        //TextView能够滚动
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());

        mQueue = Volley.newRequestQueue(MainActivity.this);

        Button btnStringRequest = (Button) findViewById(R.id.btnStringRequest);
        btnStringRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(
                        Request.Method.GET,
                        //editText.getText().toString(),
                        "http://222.190.98.24:8090/help/app-web",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                textView.setText(s);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                textView.setText(volleyError.getMessage());
                            }
                        }
                );
                mQueue.add(stringRequest);
            }
        });

        Button btnJsonRequest = (Button) findViewById(R.id.btnJsonRequest);
        btnJsonRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonRequest jsonRequest = new JsonObjectRequest(
                        Request.Method.GET,
                        //editText.getText().toString(),
                        "http://www.weather.com.cn/adat/cityinfo/101010100.html",
                        "", //不能使用null
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject s) {
                                textView.setText(s.toString());
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                textView.setText(volleyError.getMessage());
                            }
                        }
                );
                mQueue.add(jsonRequest);
            }
        });

        Button btnImageRequest = (Button) findViewById(R.id.btnImageRequest);
        btnImageRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageRequest imageRequest = new ImageRequest(
                        "http://t10.baidu.com/it/u=2997908931,3785893858&fm=58",
                        new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap bitmap) {
                                /*mImageView.setBackgroundDrawable(new BitmapDrawable(
                                        MainActivity.this.getResources(), bitmap));*/
                                textView.setText(String.format("bitmap.getByteCount():%d",bitmap.getByteCount()));
                            }
                        },
                        100,100,
                        ImageView.ScaleType.CENTER,
                        Bitmap.Config.ARGB_8888,
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                textView.setText(volleyError.getMessage());
                            }
                        }
                );
                mQueue.add(imageRequest);
            }
        });
    }
}
