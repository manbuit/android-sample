package com.manbuit.android.button;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener //将按钮Click响应事件绑定到Activity，通过btn4.setOnClickListener(this)调用onClick，不推荐
{
    private TextView textView;
    private Button btn1,btn2,btn3,btn4;

    class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            btnHandler(v);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1 = (Button) findViewById(R.id.button1);
        btn2 = (Button) findViewById(R.id.button2);
        btn3 = (Button) findViewById(R.id.button3);
        btn4 = (Button) findViewById(R.id.button4);

        //btn1有属性android:onClick="btn1_OnClick"

        //使用匿名内部类
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnHandler(v);
            }
        });

        MyClickListener myClickListener = new MyClickListener();

        //使用自定义监听类
        btn3.setOnClickListener(myClickListener);
        //btn3.setOnClickListener(new MyClickListener());

        //Activity实现View.OnClickListener
        btn4.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        this.btnHandler(v);
    }

    public void btn1_OnClick(View v){
        this.btnHandler(v);
    }

    private void btnHandler(View v){
        Button button = (Button)v;
        Toast.makeText(MainActivity.this, button.getText(), Toast.LENGTH_SHORT).show();
    }
}
