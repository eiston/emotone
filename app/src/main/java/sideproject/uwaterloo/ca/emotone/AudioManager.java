package sideproject.uwaterloo.ca.emotone;

import android.media.MediaPlayer;

/**
 * Created by Johnson on 2016-10-24.
 */
public class AudioManager {

    static MediaPlayer positive = null;
    AudioManager(MediaPlayer positive){
        this.positive = positive;
    }
    public static void playPositive(){
        if(positive.isPlaying()) positive.seekTo(0);
        positive.start();
    }
}
