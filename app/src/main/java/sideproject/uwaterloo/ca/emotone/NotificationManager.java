package sideproject.uwaterloo.ca.emotone;

import android.content.Intent;
import android.os.Bundle;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;

/**
 * Created by Johnson on 2016-10-24.
 */

public class NotificationManager extends NotificationListenerService {
    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {

    }
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        final String packageName = sbn.getPackageName();
        if(TextUtils.isEmpty(packageName) == true) return;
        String tickerText = "";
        if(sbn.getNotification().tickerText != null) {
            tickerText = sbn.getNotification().tickerText.toString();
        }
        Bundle extras = sbn.getNotification().extras;
        final String title = extras.getString("android.title");
        final String text = extras.getCharSequence("android.text").toString();

        //Build Notification
        Intent notification = new Intent("notification");
        notification.putExtra("package", packageName);
        notification.putExtra("ticker", tickerText);
        notification.putExtra("title", title);
        notification.putExtra("text", text);

        //API

        if(packageName.equals("com.google.android.apps.messaging") && text.length() <= 200){

            AudioManager.playPositive();
        }
    }

    public static int getMood(String input) throws IOException{
        //String input = "Good night";
        String builder = "{\"texts\": [\"" + input + "\"]}";
        System.out.println(builder);
        byte[] requestData = builder.getBytes("UTF-8");
        URL url = new URL("https://api.uclassify.com/v1/uclassify/sentiment/classify");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setInstanceFollowRedirects(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Content-Length", Integer.toString(requestData.length));
        conn.setRequestProperty("Authorization", "Token 3tlob1gJnqIw");
        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        wr.write(requestData);
        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        String output = "";
        for (int c = in.read(); c != -1; c = in.read()) {
            output += Character.toString((char)c);
        }
        int result = moodPercentage(output);
        // neutral
        if(result == -1 || result == 50) return 0;
        // positive
        else if(result > 50) return 1;
        // negative
        else return -1;
    }

    private static int moodPercentage(String input){
        int positive = 0, negative = 0;
        boolean virgin = true;
        for(int x = 0;x < input.length();x++){
            System.out.print(input.charAt(x));
            // p == 112 && " == 34
            if(input.charAt(x) != 34) continue;
            if(input.charAt(x) == 34 && input.charAt(x+1) == 112 && input.charAt(x+2) == 34){
                if(isValidNumber(input.charAt(x+6)) && isValidNumber(input.charAt(x+7))){
                    String builder = Character.toString(input.charAt(x+6)) + Character.toString(input.charAt(x+7));
                    if(virgin)  negative = Integer.parseInt(builder);
                    else	    positive = Integer.parseInt(builder);
                    virgin = false;
                }
            }
        }
        if((positive + negative) > 90) return positive;
        else return -1;
    }
    private static boolean isValidNumber(char input){
        if(input >= 48 && input <= 57) return true;return false;
    }
}