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
    private String userId,s_Start,s_End;
    private int locate;
    long startDay;
    long endDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_date_select);

        Intent intent=getIntent();
        userId=intent.getStringExtra("userId");
        locate=intent.getIntExtra("locate",9999);



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



        startDay=start.getTime();
        endDay=end.getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
       s_Start = dateFormat.format(start);
        s_End = dateFormat.format(end);

        String s_locate=Integer.toString(locate);
        PlanScheduleSave planScheduleSave=new PlanScheduleSave();



        planScheduleSave.execute(URL,userId,s_Start,s_End,s_locate);



    }

    private class PlanScheduleSave extends AsyncTask<String,Void,String>{


        @Override
        protected String doInBackground(String... strings) {

            String userId = strings[1];
            String startDate=strings[2];
            String endDate=strings[3];
            String locate=strings[4];



            String URL = strings[0];


            String postParameters = "userId=" + userId +"&startdate=" + startDate + "&enddate=" + endDate+ "&locate=" + locate;

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

                InputStream inputStream;

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d("test", "response code - " + responseStatusCode);

                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();



                return sb.toString().trim();

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

            Intent intent=new Intent();

            intent.putExtra("start",startDay);

            intent.putExtra("end",endDay);

            intent.putExtra("planNo",s);

            setResult(1,intent);

            finish();
        }
    }
}