package com.example.moneyjeju.MAP;


import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.moneyjeju.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class ScheduleStartActivity extends FragmentActivity implements OnMapReadyCallback {

    private  long backKeyPressedTime=0;
    private GoogleMap mMap;
    EditText etName;
    ArrayList<TourSpots> list;
    String name;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_start);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        etName=findViewById(R.id.etName);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        LatLng Jeju = new LatLng(33.3809145, 126.53578739783740);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Jeju,10));
        list=xmlParser();

        for(int i=0;i<list.size();i++)
        {
            Marker marker=mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(list.get(i).getLatitude(), list.get(i).getLongitude()))
                    .title(list.get(i).getName()));
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {

                    com.example.moneyjeju.MAP.ScheduleDialog scheduleDialog=new com.example.moneyjeju.MAP.ScheduleDialog(list,name,marker);
                    scheduleDialog.show(getSupportFragmentManager(),null);


                    return true;
                }
            });
        }
    }

    public void onSearch(View v){
        String Name=etName.getText().toString();

        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).getName().contains(Name))
            {
                Marker marker=mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(list.get(i).getLatitude(), list.get(i).getLongitude()))
                        .title(list.get(i).getName())
                        .alpha(5));
                marker.showInfoWindow();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(list.get(i).getLatitude(), list.get(i).getLongitude()),10));

            }
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed(); // 뒤로가기 버튼 막기 -> 인트로 화면을 다시 볼 필요가 없음.

        if(System.currentTimeMillis() > backKeyPressedTime+2000){
            backKeyPressedTime=System.currentTimeMillis();
            Toast.makeText(this,"\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.",Toast.LENGTH_SHORT).show();
            return;
        }

        if(System.currentTimeMillis() <= backKeyPressedTime+2000){
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setMessage("종료하시겠습니까?");
            builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });

            builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    return;
                }
            });
            builder.setTitle("프로그램 종료");
            builder.show();
        }
    }

    private ArrayList<TourSpots> xmlParser(){
        ArrayList<TourSpots> arrayList=new ArrayList<TourSpots>();
        InputStream is=getResources().openRawResource(R.raw.tourspots);


        boolean b_name=false;
        boolean b_address=false;
        boolean b_latitude=false;
        boolean b_longitude=false;
        boolean b_information=false;

        try{
            XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
            XmlPullParser parser=factory.newPullParser();
            parser.setInput(new InputStreamReader(is));
            int eventType=parser.getEventType();
            TourSpots tourSpots=null;

            while (eventType !=XmlPullParser.END_DOCUMENT){


                if(eventType==XmlPullParser.START_TAG)
                {
                    if(parser.getName().equals("Row"))
                    {
                        tourSpots=new TourSpots();
                    }
                    else if(parser.getName().equals("관광지명"))
                    {
                        b_name=true;
                    }else if(parser.getName().equals("소재지지번주소"))
                    {
                        b_address=true;
                    }
                    else if(parser.getName().equals("위도"))
                    {
                        b_latitude=true;
                    }
                    else if(parser.getName().equals("경도"))
                    {
                        b_longitude=true;
                    }
                    else if(parser.getName().equals("관광지소개"))
                    {
                        b_information=true;
                    }
                }
                else if(eventType== XmlPullParser.END_TAG)
                {
                    if(parser.getName().equals("Row"))
                    {
                        arrayList.add(tourSpots);
                    }
                }
                else if(eventType==XmlPullParser.TEXT)
                {
                    if(b_name){tourSpots.setName(parser.getText()); b_name=false;}
                    else if(b_address){tourSpots.setAddress(parser.getText()); b_address=false;}
                    else if(b_latitude){tourSpots.setLatitude(Double.parseDouble(parser.getText())); b_latitude=false;}
                    else if(b_longitude){tourSpots.setLongitude(Double.parseDouble(parser.getText())); b_longitude=false;}
                    else if(b_information){tourSpots.setInformation(parser.getText()); b_information=false;}
                }
                eventType=parser.next();
            }
        }
        catch (Exception e){e.printStackTrace();}

        return arrayList;
    }


}