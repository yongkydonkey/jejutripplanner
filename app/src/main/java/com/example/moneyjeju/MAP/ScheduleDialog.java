package com.example.moneyjeju.MAP;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;

public class ScheduleDialog extends DialogFragment {

   private ArrayList<TourSpots> list;
    private String name;
   private Marker marker;


    public ScheduleDialog(ArrayList<TourSpots> list,String name,Marker marker){
       this.list=list;
       this.name=name;
       this.marker=marker;


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

}
