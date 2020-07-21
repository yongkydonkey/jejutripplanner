package com.example.moneyjeju.MONEY;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moneyjeju.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/*
* mainproject class
* */
public class MainActivity extends AppCompatActivity {

    FloatingActionButton ab;
    Button btnt;
    Button btnd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv=findViewById(R.id.textView);
        ab=(FloatingActionButton)findViewById(R.id.about);
        btnd=(Button)findViewById(R.id.btnday);
        btnt=(Button)findViewById(R.id.btntrip);
        setTitle("여행 가계부");
        OnClickButtonListener();
        OnButtonTransClicked();
        OnaboutClicked();

    }
    //dialog box to display aboutus
    public void OnaboutClicked(){
        ab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder adb=new AlertDialog.Builder(MainActivity.this);
                        adb.setMessage("필요하다 싶은 기능은 \n abc@abc.com으로 문의주세요.");
                        adb.setTitle("여행 가계부");
                        adb.show();
                    }
                }
        );
    }

    //activity to add new trip
    public void OnClickButtonListener(){
        btnt.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent= new Intent(MainActivity.this,AddPlace.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );
    }

    //function call for transactions with friends
    public void OnButtonTransClicked(){
        btnd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(getApplicationContext(),Display_friend.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );
    }

    //when back pressed to confirm whether accidently pressed or not
    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        if (exit) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            System.exit(0);
        } else {
            Toast.makeText(this, "나가기 위해서는 뒤로가기 버튼을 누르세요",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }
}
