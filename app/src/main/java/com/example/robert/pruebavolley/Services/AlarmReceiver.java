package com.example.robert.pruebavolley.Services;

import android.app.*;
import android.content.*;
import android.os.*;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

            Intent background = new Intent(context, BackgroundService.class);
            context.startService(background);
    }

}