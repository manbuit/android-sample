package com.manbuit.android.intent;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btn1,btn2,btn3,btn4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1 = (Button) findViewById(R.id.button1);
        btn2 = (Button) findViewById(R.id.button2);
        btn3 = (Button) findViewById(R.id.button3);
        btn4 = (Button) findViewById(R.id.button4);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button btn = (Button)v;
                Toast.makeText(MainActivity.this,btn.getText(),Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                intent.setClass(MainActivity.this,Activity1.class);
                startActivity(intent);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button btn = (Button)v;
                Toast.makeText(MainActivity.this,btn.getText(),Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                intent.setClassName(MainActivity.this, "com.manbuit.android.intent.Activity1");
                //intent.setClassName("com.manbuit.android.intent", "com.manbuit.android.intent.Activity1");//这种方式也可以
                startActivity(intent);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button btn = (Button)v;
                Toast.makeText(MainActivity.this,btn.getText(),Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                intent.setAction("com.manbuit.android.intent.action.activity2");
                startActivity(intent);
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button btn = (Button)v;
                Toast.makeText(MainActivity.this,btn.getText(),Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                //打电话，必须设置<uses-permission android:name="android.permission.CALL_PHONE" />
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:18951613698"));
                startActivity(intent);
            }
        });
    }
}
