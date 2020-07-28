package com.example.moneyjeju.MAP;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.moneyjeju.R;
import com.squareup.timessquare.CalendarPickerView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ScheduleDateSelectActivity extends AppCompatActivity {

    final static private String URL = "http://192.168.0.8/planschedulesave.php";
    Date start,end;
    private String userId;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_date_select);

        Intent intent=getIntent();
        userId=intent.getStringExtra("userId");
        position=intent.getIntExtra("planNo",9999);
        position=position+1;




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

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String s_Start = dateFormat.format(start);
        String s_End = dateFormat.format(end);

        PlanScheduleSave planScheduleSave=new PlanScheduleSave();
        String planNo=Integer.toString(position);

        planScheduleSave.execute(URL,userId,planNo,s_Start,s_End);



       intent.putExtra("start",startDay);

       intent.putExtra("end",endDay);

        setResult(1,intent);

        finish();

    }

    private class PlanScheduleSave extends AsyncTask<String,Void,String>{


        @Override
        protected String doInBackground(String... strings) {

            String userId = strings[1];
            String planNo = strings[2];
            String startDate=strings[3];
            String endDate=strings[4];

            String URL = strings[0];


            String postParameters = "userId=" + userId + "&planNo=" + planNo + "&startdate=" + startDate + "&enddate=" + endDate;

            try{
                java.net.URL url=new URL(URL);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();

                OutputStream outputStream=httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                return null;
            }
            catch (Exception e) {
                return null;

            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}