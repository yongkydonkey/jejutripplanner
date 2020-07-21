package com.example.moneyjeju.MAP;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.PrecomputedText;
import android.view.View;
import android.widget.DatePicker;

import com.example.moneyjeju.R;
import com.squareup.timessquare.CalendarPickerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class StartActivity extends AppCompatActivity {

    Intent intent = null;
    String s_StartDate, s_EndDate;
    RecyclerView recyclerView;
    com.example.moneyjeju.MAP.ScheduleListAdapter scheduleListAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<com.example.moneyjeju.MAP.ScheduleDate> list=new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

    }


    public void FabScheduleAdd(View v) {

        intent = new Intent(getApplicationContext(), com.example.moneyjeju.MAP.ScheduleDateSelectActivity.class);
        startActivityForResult(intent, 0);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == 1) {


            long L_StartDate = data.getLongExtra("start", 0);
            long L_EndDate = data.getLongExtra("end", 0);

            Date StartDate = new Date(L_StartDate);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            s_StartDate = dateFormat.format(StartDate);

            Date EndDate = new Date(L_EndDate);
            s_EndDate = dateFormat.format(EndDate);


            com.example.moneyjeju.MAP.ScheduleDate scheduleDate=new com.example.moneyjeju.MAP.ScheduleDate();
            scheduleDate.setStartDate(s_StartDate);
            scheduleDate.setEndDate(s_EndDate);

            list.add(scheduleDate);
        }

        recyclerView = findViewById(R.id.ScheduleList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        scheduleListAdapter = new com.example.moneyjeju.MAP.ScheduleListAdapter(list);
        recyclerView.setAdapter(scheduleListAdapter);
    }
}