package com.example.moneyjeju.Login;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.moneyjeju.MAP.StartActivity;
import com.example.moneyjeju.MONEY.AddPlace;
import com.example.moneyjeju.R;
import com.facebook.CallbackManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginSuccess extends AppCompatActivity {
    private String userID;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapmoneyadapter);
        Button btnmoney = findViewById(R.id.btnmoney);
        Button btnmap = findViewById(R.id.btnmap);
        Intent intent=getIntent();
        userID=intent.getStringExtra("id");
    }

    public void mOnclick(View v){
        if(v.getId()==R.id.btnmoney){
            Intent intent = new Intent(LoginSuccess.this, AddPlace.class);
            intent.putExtra("id",userID);
            startActivity(intent);
            finish();
        }
        else{
            Intent intent = new Intent(LoginSuccess.this, StartActivity.class);
            intent.putExtra("id", userID);
            startActivity(intent);
            finish();
        }
    }
}
