package com.vishalsharma.mydemo15;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String state = intent.getExtras().getString("extra");//getting extra value from intent broadcasted
        Log.e("MyActivity", "In the receiver with " + state);

        Intent serviceIntent = new Intent(context,RingtonePlayingService.class);//forwarding intent to play ringtone class
        serviceIntent.putExtra("extra", state);

        context.startService(serviceIntent);//starting intent suggested activity
    }
}
