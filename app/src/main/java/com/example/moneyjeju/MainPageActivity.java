package com.example.moneyjeju;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.moneyjeju.MAP.StartActivity;
import com.example.moneyjeju.MONEY.AddPlace;
import com.example.moneyjeju.MONEY.MainActivity;

public class MainPageActivity extends AppCompatActivity {

    private String userId;
    Intent pageIntent=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        Intent intent=getIntent();
        userId=intent.getStringExtra("id");

    }

    public void SelectPage(View v){

        switch (v.getId()){
            case R.id.ibSchedule :
                pageIntent=new Intent(getApplicationContext(), StartActivity.class);

                System.out.println("111111"+userId);
                pageIntent.putExtra("id",userId);
                startActivity(pageIntent);
                break;

            case R.id.ibMoney :
                pageIntent=new Intent(getApplicationContext(), AddPlace.class);
                pageIntent.putExtra("id",userId);
                startActivity(pageIntent);
                finish();
                break;

        }

    }
}