package com.example.alarmclock;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class MyBackgroundService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Perform your background tasks here
        // This method is called when the service is started
        return START_STICKY; // If the service is killed, it will be restarted automatically
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
