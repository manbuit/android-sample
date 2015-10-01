package com.manbuit.android.layout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class Activity_DyncLayout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__dync_layout);

        LinearLayout layout = (LinearLayout) findViewById(R.id.layout);

        for(int i=1; i<=5; i++){
            Button button = new Button(Activity_DyncLayout.this);
            button.setText("button "+i);
            layout.addView(button);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((LinearLayout)v.getParent()).removeView(v);
                }
            });
        }
    }
}
