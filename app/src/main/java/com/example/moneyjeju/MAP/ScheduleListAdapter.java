package com.example.moneyjeju.MAP;


import android.app.Application;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneyjeju.JejuApp;
import com.example.moneyjeju.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.sql.Date;
import java.util.ArrayList;


public class ScheduleListAdapter extends RecyclerView.Adapter<ScheduleListAdapter.ViewHolder> {
    final static private String URL = "http://192.168.0.8/planinitdelete.php";
    private ArrayList<com.example.moneyjeju.MAP.ScheduleDate> list;
    private String userId;
    private String planNo;
    private Application application;



    public ScheduleListAdapter(ArrayList<com.example.moneyjeju.MAP.ScheduleDate> list, String userId, Application application) {
        this.list=list;
        this.userId=userId;
        this.application=application;

    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        public TextView txtState,txtScheduleStartDate,txtScheduleEndDate;
        public ImageButton btnDelete;


        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            this.txtState=itemView.findViewById(R.id.txtState);
            this.txtScheduleStartDate=itemView.findViewById(R.id.txtScheduleStartDate);
            this.txtScheduleEndDate=itemView.findViewById(R.id.txtScheduleEndDate);
            this.btnDelete=itemView.findViewById(R.id.btnDelete2);



            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String locate=Integer.toString(getAdapterPosition());
                    planNo=list.get(getAdapterPosition()).getPlanNo();


                    PlanDelete planDelete=new PlanDelete();

                    planDelete.execute(URL,userId,planNo,locate);

                    list.remove(getAdapterPosition());
                    notifyDataSetChanged();



                }
            });


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                 Intent intent=new Intent(itemView.getContext(), ScheduleTotal.class);
                 intent.putExtra("startDay",txtScheduleStartDate.getText().toString());
                 JejuApp jejuApp=(JejuApp) application.getApplicationContext();
                 jejuApp.startDate=txtScheduleStartDate.getText().toString();

                 intent.putExtra("endDay",txtScheduleEndDate.getText().toString());
                 intent.putExtra("userId",userId);
                 planNo=list.get(getAdapterPosition()).getPlanNo();

                 intent.putExtra("planNo",planNo);




                 itemView.getContext().startActivity(intent);
                }
            });
        }
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View holderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_item_list, parent, false);


        ViewHolder viewHolder = new ViewHolder(holderView);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {


       Date startDay = Date.valueOf(list.get(position).getStartDate());


       long now = System.currentTimeMillis();
       Date current = new Date(now);

        int compare1=startDay.compareTo(current);

        Date endDay = Date.valueOf(list.get(position).getEndDate());


       int compare2=endDay.compareTo(current);

       if(compare1>0 && compare2>0){
           holder.txtState.setText("계획중인 여행");
       }
       else if(compare1<=0 && compare2>=0){
           holder.txtState.setText("진행중인 여행");
       }
       else if(compare2<0){
           holder.txtState.setText("종료된 여행");
       }

        holder.txtScheduleStartDate.setText(list.get(position).getStartDate());
        holder.txtScheduleEndDate.setText(list.get(position).getEndDate());


    }


    @Override
    public int getItemCount()
    {

        return list.size();
    }


    private class PlanDelete extends AsyncTask<String,Void,String>{

        String errorString = null;


        @Override
        protected String doInBackground(String... strings) {
            String URL=strings[0];
            String userId=strings[1];
            String planNo=strings[2];
            String locate=strings[3];


            String postParameters = "userId=" + userId + "&planNo=" + planNo+ "&locate=" + locate;

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

                Log.d("delete", "InsertData: Error ", e);
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
        }
    }


}
