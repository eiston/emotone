package sideproject.uwaterloo.ca.emotone;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    AudioManager setupAM;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // create Media
        MediaPlayer positiveAudio = MediaPlayer.create(MainActivity.this, R.raw.ding);
        positiveAudio.start();
        setupAM = new AudioManager(positiveAudio);
        // run service
        startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
    }

    public void startService(View view){

    }

    public void stopSerivice(View view){

    }
}

