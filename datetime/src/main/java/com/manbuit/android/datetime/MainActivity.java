package com.manbuit.android.datetime;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

public class MainActivity extends AppCompatActivity {

    EditText dateEdit,timeEdit;
    int index = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dateEdit = (EditText) findViewById(R.id.dateEdit);
        timeEdit = (EditText) findViewById(R.id.timeEdit);

        dateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(index==0) {
                    new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            dateEdit.setText(String.format("%d-%d-%d", year, monthOfYear, dayOfMonth));
                        }
                    }, 2015, 3, 3).show();
                    index++;
                }
            }
        });
        dateEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && index > 0) {
                    new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            dateEdit.setText(String.format("%d-%d-%d", year, monthOfYear, dayOfMonth));
                        }
                    }, 2015, 3, 3).show();
                }
            }
        });

        /*timeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timeEdit.setText(String.format("%d:%d", hourOfDay, minute));
                    }
                }, 10, 30, true).show();
            }
        });*/
        timeEdit.setOnFocusChangeListener(new View.OnFocusChangeListener()

        {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            timeEdit.setText(String.format("%d:%d", hourOfDay, minute));
                        }
                    }, 10, 30, true).show();
                }
            }
        });
    }
}
