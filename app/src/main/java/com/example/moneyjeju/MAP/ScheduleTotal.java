package com.example.moneyjeju.MAP;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.moneyjeju.JejuApp;
import com.example.moneyjeju.MONEY.MainActivity;
import com.example.moneyjeju.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class ScheduleTotal extends AppCompatActivity {

    final static private String DELETE_URL = "http://192.168.0.8/plandetaildelete.php";
    final static private String INIT_URL = "http://192.168.0.8/plandetailinit.php";
    private static final String TAG_JSON="webnautes";
    private static final String TAG_DATE = "date";
    private static final String TAG_TOURSPOTNAME = "tourspotname";
    String startDay,endDay,s_Start,userId,planNo,JsonString,adddate;
    Spinner selectDate;


    ArrayList<ScheduleTotalDate> list=new ArrayList<ScheduleTotalDate>();
    ArrayList<TourSpotName> list2=new ArrayList<TourSpotName>();
    ArrayList<String> date=new ArrayList<>();
    ArrayList<ScheduleDetailInit> list3=new ArrayList<>();
    ScheduleDetailInit scheduleDetailInit;

    int position;
    long calDateDays;

    LinearLayout[] last;
    TextView[] txtDate;
    JejuApp jejuApp;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_total);

        jejuApp=(JejuApp) getApplicationContext();

        LinearLayout llDetailFrame=findViewById(R.id.llDetailFrame);
        selectDate=findViewById(R.id.selectDate);


        Intent intent=getIntent();
        startDay=intent.getStringExtra("startDay");
        endDay=intent.getStringExtra("endDay");
        userId=intent.getStringExtra("userId");
        planNo=intent.getStringExtra("planNo");

        jejuApp.userId=userId;
        jejuApp.planNo=planNo;


        Date startDay1 = Date.valueOf(startDay);
        Date endDay1 = Date.valueOf(endDay);

        long calDate = startDay1.getTime() - endDay1.getTime();
        calDateDays = calDate / ( 24*60*60*1000);

        calDateDays = Math.abs(calDateDays)+1;
        s_Start=startDay;


        int c=(int)calDateDays;

        last=new LinearLayout[c];
        txtDate=new TextView[c];



        PlanDetailInit planDetailInit=new PlanDetailInit();
        planDetailInit.execute(INIT_URL,userId,planNo);


        for(int i=0;i<calDateDays;i++)
        {
            final int count=i;

            ScheduleTotalDate scheduleTotalDate=new ScheduleTotalDate();

            scheduleTotalDate.setDay(s_Start);
            list.add(scheduleTotalDate);

            date.add(s_Start);

            LinearLayout test=new LinearLayout(this);

            test.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            test.setLayoutParams(params);

            txtDate[i]=new TextView(this);

            txtDate[i].setText(s_Start);
            txtDate[i].setLayoutParams(params);

            last[i]=new LinearLayout(this);
            last[i].setOrientation(LinearLayout.VERTICAL);
            last[i].setLayoutParams(params);

            Button btnAdd=new Button(this);
            btnAdd.setText("추가");
            btnAdd.setLayoutParams(params);

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent=new Intent(getApplicationContext(), ScheduleSelectActivity.class);
                    intent.putExtra("count",count);
                    intent.putExtra("userId",userId);
                    intent.putExtra("planNo",planNo);
                    intent.putExtra("date",date.get(count));
                    startActivityForResult(intent,3);
                }
            });


            test.addView(txtDate[i]);
            test.addView(last[i]);
            test.addView(btnAdd);


            Date d_Start=Date.valueOf(s_Start);
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(d_Start);
            calendar.add(Calendar.DATE,1);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            s_Start=dateFormat.format(calendar.getTime());

           llDetailFrame.addView(test);

        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, date);
        selectDate.setAdapter(adapter);

        selectDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectDate=date.get(position);
                jejuApp.selectDate=selectDate;
                ScheduleTotalMap scheduleTotalMap=new ScheduleTotalMap();
                scheduleTotalMap.SelectDate(selectDate);

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                String selectDate=jejuApp.startDate;
                ScheduleTotalMap scheduleTotalMap=new ScheduleTotalMap();
                scheduleTotalMap.SelectDate(selectDate);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();



        if (list2.size() != 0) {

            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);


            final LinearLayout detail = (LinearLayout) inflater.inflate(R.layout.schedule_item_detail, null, false);
            final TextView txtTourName = detail.findViewById(R.id.txtTourName);
            txtTourName.setText(list2.get(list2.size() - 1).getName());

            final ImageButton btnDelete = detail.findViewById(R.id.btnDelete);
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String deleteDate=null;
                    for(int i=0;i<(int)calDateDays;i++)
                    {
                        if(detail.getParent().getParent()==txtDate[i].getParent()){

                            deleteDate=txtDate[i].getText().toString();



                        }

                    }

                   PlanDetailDelete planDetailDelete=new PlanDetailDelete();
                   planDetailDelete.execute(DELETE_URL,userId,planNo,deleteDate,txtTourName.getText().toString());

                   detail.removeAllViews();

                    ScheduleTotalMap scheduleTotalMap=new ScheduleTotalMap();
                    scheduleTotalMap.SelectDate(deleteDate);
                }
            });


            last[position].addView(detail);
            ScheduleTotalMap scheduleTotalMap=new ScheduleTotalMap();
            scheduleTotalMap.SelectDate(adddate);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==3 && resultCode==4){

            TourSpotName tourSpotName=new TourSpotName();
            tourSpotName.setName(data.getStringExtra("name"));
            list2.add(tourSpotName);
            position=data.getIntExtra("position",0);
            adddate=data.getStringExtra("date");

        }
    }

    public void scheduleFinish(View v){

        Intent intent=new Intent(this,StartActivity.class);
        intent.putExtra("id",userId);
        jejuApp.startDate=null;
        startActivity(intent);

    }
    private class PlanDetailDelete extends AsyncTask<String,Void,String>{


        @Override
        protected String doInBackground(String... strings) {
            String userId = strings[1];
            String planNo = strings[2];
            String deleteDate=strings[3];
            String tourSpotName=strings[4];

            String URL = strings[0];


            String postParameters = "userId=" + userId + "&planNo=" + planNo + "&date=" + deleteDate + "&tourspotname=" + tourSpotName;

            try{
                java.net.URL url=new java.net.URL(URL);
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
                Log.d("TAG", "response code - " + responseStatusCode);

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
        }
    }
    private class PlanDetailInit extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
           String userId=strings[1];
           String planNo=strings[2];
           String URL=strings[0];
           String postParameters = "userId=" + userId + "&planNo=" + planNo;

            String errorString = null;

            try{
                java.net.URL url=new java.net.URL(URL);
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
                Log.d("PlanDetailInit", "response code - " + responseStatusCode);

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

                Log.d("PlanDetailInit", "InsertData: Error ", e);
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

            Log.d("PlanDetailInit", "response - " + s);

            if (s == null){

            }
            else {
                JsonString=s;
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

                String date = item.getString(TAG_DATE);
                String tourSpotName = item.getString(TAG_TOURSPOTNAME);

                scheduleDetailInit=new ScheduleDetailInit();

                scheduleDetailInit.setDate(date);
                scheduleDetailInit.setTourSpotName(tourSpotName);

               list3.add(scheduleDetailInit);
            }

        } catch (JSONException e) {

            Log.d("PlanDetailInit2", "showResult : ", e);
        }



        for(int i=0;i<list3.size();i++){

            for(int j=0;j<(int)calDateDays;j++){

                if(txtDate[j].getText().equals(list3.get(i).getDate()))
                {

                    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);


                    final LinearLayout detail = (LinearLayout) inflater.inflate(R.layout.schedule_item_detail, null, false);
                    final TextView txtTourName = detail.findViewById(R.id.txtTourName);




                    txtTourName.setText(list3.get(i).getTourSpotName());


                    final ImageButton btnDelete = detail.findViewById(R.id.btnDelete);

                    btnDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String deleteDate=null;
                            for(int k=0;k<(int)calDateDays;k++)
                            {
                                if(detail.getParent().getParent()==txtDate[k].getParent()){

                                    deleteDate=txtDate[k].getText().toString();

                                }

                            }

                            PlanDetailDelete planDetailDelete=new PlanDetailDelete();
                            planDetailDelete.execute(DELETE_URL,userId,planNo,deleteDate,txtTourName.getText().toString());

                            detail.removeAllViews();
                            ScheduleTotalMap scheduleTotalMap=new ScheduleTotalMap();
                            scheduleTotalMap.SelectDate(deleteDate);


                        }
                    });

                    last[j].addView(detail);
                    ScheduleTotalMap scheduleTotalMap=new ScheduleTotalMap();
                    scheduleTotalMap.SelectDate(adddate);
                }
            }
        }


    }
}