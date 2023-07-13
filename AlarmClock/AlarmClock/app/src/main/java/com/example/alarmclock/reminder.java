package com.example.alarmclock;

import static android.os.ParcelFileDescriptor.MODE_APPEND;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;

public class reminder extends BroadcastReceiver {
    static  int[] weekend = {1,7};

    static  int total_minutes = 60*15;  // 15 minutes interval
    static  int total_hour = 60*60;  // 15 minutes interval
    static  int default_min = 0;
    private static final String CHANNEL_ID = "my_channel";
    private  static Runnable cancelAlarmRunnable;
    private static final int NOTIFICATION_ID = 1;
    private static final int INTERVAL_SECONDS = 1;
    private static Handler handler;
    private static AlarmManager alarmManager;
    static String test = "";
    private static PendingIntent pendingIntent;
    private MediaPlayer mediaPlayer;
    static final Boolean[] alarm = {false};
    static final Boolean[] lunch = {false};
    static Boolean alarmstatus = false;
    static String starttime;
    static String endtime;
    static NotificationCompat.Builder builder;
    private static boolean check(int[] arr, int toCheckValue)
    {
        boolean test = false;
        for (int element : arr) {
            if (element == toCheckValue) {
                test = true;
                break;
            }
        }
        return test;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            showNotification(context);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void scheduleNotification(Context context) {

//        starttime = starttime1;
//        endtime = endtime1;
        alarmstatus = true;
        SharedPreferences sh = context.getApplicationContext().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if(check(weekend, day)) {
            String s2 = sh.getString("weekendsint", "");
            total_minutes = 60*Integer.parseInt(s2);
        }
        else{
            String s2 = sh.getString("weekdaysint", "");
            total_minutes = 60*Integer.parseInt(s2);
        }

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, reminder.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long triggerAtMillis = System.currentTimeMillis();
        long intervalMillis = INTERVAL_SECONDS * 1000; //

        if (alarmManager != null) {

            handler = new Handler();
            cancelAlarmRunnable = new Runnable() {
                private long time = default_min;



                @Override
                public void run() {
                    Calendar calendar = Calendar.getInstance();
                    int day = calendar.get(Calendar.DAY_OF_WEEK);

                    if(alarmstatus) {


                        try {

                            if (check(weekend, day)) {
                                String s2 = sh.getString("weekendsint", "");
                                String s1 = sh.getString("weekendstime", "");
                                String[] splitst = s1.split(":");

                                String s11 = sh.getString("weekendetime", "");
                                String[] splitet = s11.split(":");

                                if (Objects.equals(splitst[0], "24")) {
                                    splitst[0] = "0";
                                }

                                if (Objects.equals(splitet[0], "24")) {
                                    splitet[0] = "0";
                                }

                                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                                int min = calendar.get(Calendar.MINUTE);

                                if (hour == Integer.parseInt(splitet[0]) && min >= Integer.parseInt(splitet[1])) {
                                    //                        cancelAlarm();
                                    total_minutes = 60 * Integer.parseInt(s2);
                                    default_min = 0;
                                    handler.postDelayed(this, 1000);

                                } else if (hour >= Integer.parseInt(splitst[0]) && hour < Integer.parseInt((splitet[0])) && min >= Integer.parseInt(splitst[1])) {

                                    if(alarmstatus) {
                                        System.out.print("Time: " + time);
                                        time += 1;

                                        if (time == total_hour) {

                                            total_minutes = 60 * Integer.parseInt(s2);
                                            default_min = 0;
                                            // lunch
                                            test = "Lunch Time";
                                            Toast.makeText(context, "Lunch Time", Toast.LENGTH_SHORT).show();
                                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                                            lunch[0] = true;
                                        }
                                        if (!lunch[0]) {
                                            if (time >= total_minutes) {
                                                test = "Drink Time";
                                                alarmManager.setExact(AlarmManager.RTC_WAKEUP, 1000, pendingIntent);
                                            } else {
                                                handler.postDelayed(this, 1000); // Check every minute
                                            }
                                        }
                                    }
                                    else{
                                        total_minutes = 60*15;  // 15 minutes interval
                                        total_hour = 60*60;  // 15 minutes interval
                                        default_min = 0;
                                        handler.removeCallbacksAndMessages(null);
                                    }

                                } else {
                                    handler.postDelayed(this, 1000);
                                }
                            } else {
                                String s2 = sh.getString("weekdaysint", "");
                                String s1 = sh.getString("weekdaystime", "");
                                String[] splitst = s1.split(":");
                                String s11 = sh.getString("weekdayetime", "");
                                String[] splitet = s11.split(":");
                                if (Objects.equals(splitst[0], "24")) {
                                    splitst[0] = "0";
                                }

                                if (Objects.equals(splitet[0], "24")) {
                                    splitet[0] = "0";
                                }

                                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                                int min = calendar.get(Calendar.MINUTE);
                                System.out.print("Time: " + time);
                                if (hour == Integer.parseInt(splitet[0]) && min >= Integer.parseInt(splitet[1])) {
                                    //                        cancelAlarm();
                                    total_minutes = 60 * Integer.parseInt(s2);
                                    default_min = 0;
                                    handler.postDelayed(this, 1000);

                                } else if (hour >= Integer.parseInt(splitst[0]) && hour <= Integer.parseInt((splitet[0])) && min >= Integer.parseInt(splitst[1])) {
//                                    Toast.makeText(context, String.valueOf(alarmstatus), Toast.LENGTH_SHORT).show();
                                    if(alarmstatus) {
                                        time += 1;


                                        if (time == total_hour) {

                                            total_minutes = 60 * Integer.parseInt(s2);
                                            default_min = 0;
                                            // lunch
                                            test = "Lunch Time";
                                            Toast.makeText(context, "Lunch Time", Toast.LENGTH_SHORT).show();
                                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                                            lunch[0] = true;
                                        }
                                        if (!lunch[0]) {
//                                            Toast.makeText(context, String.valueOf(time), Toast.LENGTH_SHORT).show();
                                            if (time >= total_minutes) {
                                                test = "Drink Time";
                                                Toast.makeText(context, "Drink TIme", Toast.LENGTH_SHORT).show();
                                                alarmManager.setExact(AlarmManager.RTC_WAKEUP, 1000, pendingIntent);
                                            } else {
                                                handler.postDelayed(this, 1000); // Check every minute
                                            }
                                        }
                                    }
                                    else{
                                        total_minutes = 60*15;  // 15 minutes interval
                                        total_hour = 60*60;  // 15 minutes interval
                                        default_min = 0;
                                        handler.removeCallbacksAndMessages(null);
                                    }
                                } else {
                                    handler.postDelayed(this, 1000);
                                }
                            }
                        } catch (Exception e) {
                            Toast.makeText(context.getApplicationContext(), "Alarm is stop", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        total_minutes = 60*15;  // 15 minutes interval
                        total_hour = 60*60;  // 15 minutes interval
                        default_min = 0;
                        handler.removeCallbacksAndMessages(null);
                    }
//                    handler.postDelayed(this, 300);
                }

            };

            handler.post(cancelAlarmRunnable);


        }
    }

    private static void cancelAlarm() {
        if (alarmManager != null && pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    public static void cancelNotification(Context context) {
        alarmstatus = false;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, reminder.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    private void showNotification(Context context) throws IOException {
        createNotificationChannel(context);



        builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_local_drink_24)
                .setContentText(test)
                .setContentTitle("AlarmClock")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        Notification notification = builder.build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        notificationManager.notify(NOTIFICATION_ID, notification);
        Uri soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.notification);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(context, soundUri);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
        mediaPlayer.setLooping(true); // Set the MediaPlayer to repeat the sound
        mediaPlayer.prepare();
        mediaPlayer.start();

        final Handler h = new Handler();
        h.postDelayed(new Runnable()
        {
            private long time = 0;

            @Override
            public void run()
            {
                time += 1000;
                if(time==5000){
                    mediaPlayer.stop();
                    h.removeCallbacks(null);

                    default_min = total_minutes;
                    total_minutes+=total_minutes;
                    if(lunch[0]){
                        lunch[0] = false;
                    }
                    handler.post(cancelAlarmRunnable);
                }
                else {
                    Log.d("TimerExample", "Going for... " + time);
                    h.postDelayed(this, 1000);
                }
            }
        }, 1000); // 1 second delay (takes millis)


    }

    private void createNotificationChannel(Context context) throws IOException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "My Channel";
            String channelDescription = "My Channel Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            channel.setDescription(channelDescription);
//            channel.setSound(soundUri, audioAttributes);



            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
