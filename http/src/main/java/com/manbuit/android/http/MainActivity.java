package com.manbuit.android.http;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView textView;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.button);

        editText = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.textView);
        webView = (WebView) findViewById(R.id.webView);

        //TextView能够滚动
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<String,Void,String>(){
                    @Override
                    protected String doInBackground(String... params) {
                        StringBuilder result = new StringBuilder();
                        try {
                            URL url = new URL(params[0]);
                            URLConnection connection = url.openConnection();
                            InputStream is = connection.getInputStream();
                            InputStreamReader isr = new InputStreamReader(is,"utf-8");
                            BufferedReader reader = new BufferedReader(isr);
                            String line;
                            while ((line = reader.readLine()) != null){
                                result.append(line);
                            }
                            reader.close();
                            isr.close();;
                            is.close();
                            connection.getClass();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }finally {
                            return result.toString();
                        }
                    };

                    @Override
                    protected void onPostExecute(String result) {
                        textView.setText(result);
                        webView.loadData(result,"text/html","ISO-8859-1");//"UTF-8","GBK","ISO-8859-1"
                    }

                    @Override
                    protected void onPreExecute() {
                        Toast.makeText(MainActivity.this,"Loading...",Toast.LENGTH_SHORT).show();
                    }
                }.execute(editText.getText().toString());
            }
        });
    }
}
