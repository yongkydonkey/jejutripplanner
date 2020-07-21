package com.example.moneyjeju.MAP;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.moneyjeju.MONEY.MainActivity;
import com.example.moneyjeju.R;

public class IntroActivity extends AppCompatActivity {

    VideoView IntroVideo;
    Handler handler;
    Intro_Runnable intro_runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        IntroVideo=findViewById(R.id.IntroVideo);



        /*Uri videoUri=Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.jeju);

        IntroVideo.setMediaController(new MediaController(getApplicationContext()));
        IntroVideo.setVideoURI(videoUri);
        IntroVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                IntroVideo.start();
            }
        });*/

        handler=new Handler();
        intro_runnable=new Intro_Runnable();
        handler.postDelayed(intro_runnable,1000);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(IntroVideo!=null && IntroVideo.isPlaying()) IntroVideo.pause();


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(IntroVideo!=null) IntroVideo.stopPlayback();;
        handler.removeCallbacks(intro_runnable);

    }

    class Intro_Runnable implements Runnable {
        @Override
        public void run() {
            Intent intent=new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }
}