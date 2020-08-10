package com.example.moneyjeju.MAP;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Application;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.View;

import android.widget.TextView;

import com.example.moneyjeju.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class StartActivity extends AppCompatActivity {

    final static private String URL = "http://192.168.0.8/planinit.php";
    private static String TAG = "PlanInit";
    private static final String TAG_JSON="webnautes";
    private static final String TAG_ID = "userId";
    private static final String TAG_PLANNO = "planNo";
    private static final String TAG_STARTDATE ="startdate";
    private static final String TAG_ENDDATE ="enddate";
    private String userID;
    private Application application;


    Intent intent = null;
    String s_StartDate, s_EndDate;
    RecyclerView recyclerView;
    com.example.moneyjeju.MAP.ScheduleListAdapter scheduleListAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<com.example.moneyjeju.MAP.ScheduleDate> list=new ArrayList<>();
    String JsonString;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Intent intent=getIntent();
        userID=intent.getStringExtra("id");
        TextView txtUserID=findViewById(R.id.txtUserId);
        txtUserID.setText(userID+"환영합니다.");
        PlanInit planInit=new PlanInit();
        planInit.execute(URL,userID);
        application=getApplication();

    }


    public void FabScheduleAdd(View v) {

        intent = new Intent(getApplicationContext(), com.example.moneyjeju.MAP.ScheduleDateSelectActivity.class);
        intent.putExtra("userId",userID);
        intent.putExtra("locate",list.size());
        startActivityForResult(intent, 0);

    }

   private class PlanInit extends AsyncTask<String, Void,String>{

        String errorString = null;

       @Override
       protected String doInBackground(String... strings) {

           String URL=strings[0];
           String userId=strings[1];

           String postParameters = "userId=" + userId;



           try{
               java.net.URL url=new URL(URL);
               HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();

               httpURLConnection.setReadTimeout(5000);
               httpURLConnection.setConnectTimeout(5000);
               httpURLConnection.setRequestMethod("POST");
               httpURLConnection.setDoInput(true);
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
           catch (Exception e){

               Log.d(TAG, "InsertData: Error ", e);
               errorString = e.toString();
               return null;}

       }

       @Override
       protected void onPreExecute() {
           super.onPreExecute();
       }

       @Override
       protected void onPostExecute(String s) {

           super.onPostExecute(s);

           //mTextViewResult.setText(s);
           Log.d(TAG, "response - " + s);

           if (s == null){

              // mTextViewResult.setText(errorString);
           }
           else {

               JsonString = s;


               showResult();
           }
       }
   }

    private void showResult(){
        try {
            JSONObject jsonObject = new JSONObject(JsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String id = item.getString(TAG_ID);

                int tempPlanNo=item.getInt(TAG_PLANNO);
                String PlanNo=Integer.toString(tempPlanNo);

                String startDate = item.getString(TAG_STARTDATE);
                String endDate = item.getString(TAG_ENDDATE);

                com.example.moneyjeju.MAP.ScheduleDate scheduleDate=new com.example.moneyjeju.MAP.ScheduleDate();
                scheduleDate.setId(id);
                scheduleDate.setPlanNo(PlanNo);
                scheduleDate.setStartDate(startDate);
                scheduleDate.setEndDate(endDate);

                list.add(scheduleDate);


            }

            recyclerView = findViewById(R.id.ScheduleList);
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            scheduleListAdapter = new com.example.moneyjeju.MAP.ScheduleListAdapter(list,userID);
            recyclerView.setAdapter(scheduleListAdapter);

        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

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

            String PlanNo=data.getStringExtra("planNo");



            com.example.moneyjeju.MAP.ScheduleDate scheduleDate=new com.example.moneyjeju.MAP.ScheduleDate();
            scheduleDate.setId(userID);
            scheduleDate.setPlanNo(PlanNo);
            scheduleDate.setStartDate(s_StartDate);
            scheduleDate.setEndDate(s_EndDate);

            list.add(scheduleDate);
        }

        recyclerView = findViewById(R.id.ScheduleList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        scheduleListAdapter = new com.example.moneyjeju.MAP.ScheduleListAdapter(list,userID);
        recyclerView.setAdapter(scheduleListAdapter);
    }
}