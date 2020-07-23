package com.example.moneyjeju.MAP;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.moneyjeju.R;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ScheduleTotal extends AppCompatActivity {

    String startDay,endDay,s_Start;
    Spinner selectDate;

    ArrayList<ScheduleTotalDate> list=new ArrayList<ScheduleTotalDate>();
    ArrayList<TourSpotName> list2=new ArrayList<TourSpotName>();
    ArrayList<String> date=new ArrayList<>();

    int position;
    long calDateDays;

    LinearLayout[] last;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_total);

        LinearLayout llDetailFrame=findViewById(R.id.llDetailFrame);
        selectDate=findViewById(R.id.selectDate);



        Intent intent=getIntent();
        startDay=intent.getStringExtra("startDay");
        endDay=intent.getStringExtra("endDay");

        Date startDay1 = Date.valueOf(startDay);
        Date endDay1 = Date.valueOf(endDay);

        long calDate = startDay1.getTime() - endDay1.getTime();
        calDateDays = calDate / ( 24*60*60*1000);

        calDateDays = Math.abs(calDateDays)+1;
        s_Start=startDay;


        int c=(int)calDateDays;

        last=new LinearLayout[c];






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
            TextView txtDate=new TextView(this);
            txtDate.setText(s_Start);
            txtDate.setLayoutParams(params);

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
                    startActivityForResult(intent,3);
                }
            });


            test.addView(txtDate);
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
                ScheduleTotalMap scheduleTotalMap=new ScheduleTotalMap();
                scheduleTotalMap.selectDate(selectDate);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                String selectDate=date.get(0);
                ScheduleTotalMap scheduleTotalMap=new ScheduleTotalMap();
                scheduleTotalMap.selectDate(selectDate);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (list2.size() != 0) {

            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);


            final LinearLayout detail = (LinearLayout) inflater.inflate(R.layout.schedule_item_detail, null, false);
            TextView txtTourName = detail.findViewById(R.id.txtTourName);
            txtTourName.setText(list2.get(list2.size() - 1).getName());

            final ImageButton btnDelete = detail.findViewById(R.id.btnDelete);
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                   detail.removeAllViews();
                }
            });


            last[position].addView(detail);

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

        }
    }

    public void scheduleFinish(View v){
        Intent intent=new Intent(this,StartActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

}