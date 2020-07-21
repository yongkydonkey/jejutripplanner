package com.example.moneyjeju.MAP;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneyjeju.R;

import java.sql.Date;
import java.util.ArrayList;


public class ScheduleListAdapter extends RecyclerView.Adapter<ScheduleListAdapter.ViewHolder> {

    private ArrayList<com.example.moneyjeju.MAP.ScheduleDate> list;



    public ScheduleListAdapter(ArrayList<com.example.moneyjeju.MAP.ScheduleDate> list) {
        this.list=list;

    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        public TextView txtState,txtScheduleStartDate,txtScheduleEndDate;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            this.txtState=itemView.findViewById(R.id.txtState);
            this.txtScheduleStartDate=itemView.findViewById(R.id.txtScheduleStartDate);
            this.txtScheduleEndDate=itemView.findViewById(R.id.txtScheduleEndDate);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                 Intent intent=new Intent(itemView.getContext(), ScheduleTotal.class);
                 intent.putExtra("startDay",txtScheduleStartDate.getText());
                 intent.putExtra("endDay",txtScheduleEndDate.getText());
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


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


}
