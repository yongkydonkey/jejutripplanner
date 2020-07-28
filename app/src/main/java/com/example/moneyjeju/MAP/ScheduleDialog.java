package com.example.moneyjeju.MAP;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


import com.example.moneyjeju.R;
import com.google.android.gms.maps.model.Marker;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

public class ScheduleDialog extends DialogFragment {


    final static private String URL = "http://192.168.0.8/plandetailsave.php";
   private ArrayList<TourSpots> list;
    private String name;
   private Marker marker;
   private String userId,planNo,date;


    public ScheduleDialog(ArrayList<TourSpots> list,String name,Marker marker,String userId,String planNo,String date){
       this.list=list;
       this.name=name;
       this.marker=marker;
       this.userId=userId;
       this.planNo=planNo;
       this.date=date;


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.scheduledialog,container,false);

        final TextView txtName=view.findViewById(R.id.txtName);
        txtName.setText(marker.getTitle());
        TextView txtInfo=view.findViewById(R.id.txtInfo);
        for(int i=0;i<list.size();i++)
        {
            if(marker.getTitle().equals(list.get(i).getName()))
            {
                txtInfo.setText(list.get(i).getInformation());
            }

        }

        setCancelable(false);

        Button btnConfirm=view.findViewById(R.id.btnConfirm);
        Button btnCancel=view.findViewById(R.id.btnCancel);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name=txtName.getText().toString();

                PlanDetailSave planDetailSave=new PlanDetailSave();
                planDetailSave.execute(URL,userId,planNo,date,name);

                Intent intent=getActivity().getIntent();
                int position=intent.getIntExtra("count",0);
                Intent intent2=new Intent();
                intent2.putExtra("name",name);
                intent2.putExtra("position",position);
                getActivity().setResult(4,intent2);
                getActivity().finish();
                dismiss();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }

    private class PlanDetailSave extends AsyncTask<String, Void,String>{


        @Override
        protected String doInBackground(String... strings) {
            String URL=strings[0];
            String userId=strings[1];
            String planNo=strings[2];
            String date=strings[3];
            String name=strings[4];

            String postParameters = "userId=" + userId + "&planNo=" + planNo + "&date=" + date + "&tourspotname=" + name;

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
                Log.d("dialog", "response code - " + responseStatusCode);

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

                System.out.println(sb.toString().trim());


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

}
