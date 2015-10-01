package com.manbuit.android.async_http;

import android.hardware.camera2.CameraManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.*;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.textView);

        Button btnAsyncHttpClient = (Button) findViewById(R.id.btnAsyncHttpClient);

        btnAsyncHttpClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncHttpClient client = new AsyncHttpClient();
                client.get(
                    //"http://222.190.98.24:8090/help/app-web",
                    editText.getText().toString(),
                    new AsyncHttpResponseHandler() {
                        @Override
                        public void onStart() {
                            // called before request is started
                            Toast.makeText(MainActivity.this, "loading...", Toast.LENGTH_SHORT);
                        }

                        @Override
                        public void onSuccess(int i, Header[] headers, byte[] bytes) {
                            textView.setText(new String(bytes));
                        }

                        @Override
                        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                            Toast.makeText(MainActivity.this, "error...", Toast.LENGTH_SHORT);
                        }

                        @Override
                        public void onRetry(int retryNo) {
                            // called when request is retried
                            Toast.makeText(MainActivity.this, "retrying...", Toast.LENGTH_SHORT);
                        }
                    }
                );
            }
        });

    }
}
