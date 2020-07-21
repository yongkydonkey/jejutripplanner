package com.example.moneyjeju.MAP;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.moneyjeju.R;
import com.squareup.timessquare.CalendarPickerView;

import java.util.Calendar;
import java.util.Date;

public class ScheduleDateSelectActivity extends AppCompatActivity {

    Date start,end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_date_select);



        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

        final CalendarPickerView calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
        Date today = new Date();
        calendar.init(today, nextYear.getTime())
                .inMode(CalendarPickerView.SelectionMode.RANGE);

       calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
           @Override
           public void onDateSelected(Date date) {
               start=calendar.getSelectedDates().get(0);
               end=calendar.getSelectedDates().get(calendar.getSelectedDates().size()-1);
           }

           @Override
           public void onDateUnselected(Date date) {

           }
       });
    }

    public void ScheduleDateConfirm(View v){


        Intent intent=new Intent();
        long startDay=start.getTime();
        long endDay=end.getTime();



       intent.putExtra("start",startDay);

       intent.putExtra("end",endDay);




        setResult(1,intent);

        finish();

    }
}