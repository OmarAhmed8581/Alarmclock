package com.example.alarmclock;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.CalendarContract;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.security.PrivateKey;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    Button startbtn,endbtn;
    TextView starttext,endtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createnotification();
        Button btn= findViewById(R.id.alarm);

        // Storing data into SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);

// Creating an Editor object to edit(write to the file)
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        starttext = findViewById(R.id.starttime);

        endtext = findViewById(R.id.endtime);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Reminder set",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this , reminder.class);
                reminder.scheduleNotification(getApplicationContext());

            }
        });
        final String[] m_Text = {""};
        starttext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Set Interval Minutes");

                // Set up the input
                final EditText input = new EditText(MainActivity.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("Set Time", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text[0] = input.getText().toString();
                        //
                        Calendar calendar = Calendar.getInstance();
                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
                        int min = calendar.get(Calendar.MINUTE);
                        TimePickerDialog timePickerDialog=new TimePickerDialog(MainActivity.this, androidx.appcompat.R.style.Theme_AppCompat_Dialog, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                Calendar c =new GregorianCalendar();
                                c.set(Calendar.HOUR_OF_DAY,i);
                                c.set(Calendar.MINUTE,i1);
                                c.setTimeZone(TimeZone.getDefault());
                                SimpleDateFormat simpleDateFormat =new SimpleDateFormat("k:mm");
                                String stime = simpleDateFormat.format(c.getTime());
                                Calendar calendar = Calendar.getInstance();
                                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                                int min = calendar.get(Calendar.MINUTE);
                                TimePickerDialog timePickerDialog=new TimePickerDialog(MainActivity.this, androidx.appcompat.R.style.Theme_AppCompat_Dialog, new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                        Calendar c =new GregorianCalendar();
                                        c.set(Calendar.HOUR_OF_DAY,i);
                                        c.set(Calendar.MINUTE,i1);
                                        c.setTimeZone(TimeZone.getDefault());
                                        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("k:mm");
                                        String etime = simpleDateFormat.format(c.getTime());
                                        myEdit.putString("weekdaysint", m_Text[0]);
                                        myEdit.putString("weekdaystime", stime);
                                        myEdit.putString("weekdayetime", etime);
//                                        myEdit.putInt("age", Integer.parseInt(age.getText().toString()));
                                         starttext.setText("Interval Minutes: "+m_Text[0]+" , Start time: "+stime+" End time: "+etime);

                                        myEdit.commit();

                                    }
                                },hour,min,false);
                                timePickerDialog.show();
                            }
                        },hour,min,false);
                        timePickerDialog.show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

        if(sh.contains("weekendsint")){
            String s2 = sh.getString("weekendsint", "");
            String s1 = sh.getString("weekendstime", "");
            String s3 = sh.getString("weekendetime", "");
            endtext.setText("Interval Second: "+s2+" , Start time: "+s1+" End time: "+s2);
            String s4 = sh.getString("weekdaysint", "");
            String s5 = sh.getString("weekdaystime", "");
            String s6 = sh.getString("weekdayetime", "");
            starttext.setText("Interval Second: "+s4+" , Start time: "+s5+" End time: "+s6);
            reminder.scheduleNotification(getApplicationContext());

        }

        final String[] m_Text1 = {""};
        endtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Set Interval Minutes");

                // Set up the input
                final EditText input = new EditText(MainActivity.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("Set Time", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text1[0] = input.getText().toString();
                        //
                        Calendar calendar = Calendar.getInstance();
                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
                        int min = calendar.get(Calendar.MINUTE);
                        TimePickerDialog timePickerDialog=new TimePickerDialog(MainActivity.this, androidx.appcompat.R.style.Theme_AppCompat_Dialog, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                Calendar c =new GregorianCalendar();
                                c.set(Calendar.HOUR_OF_DAY,i);
                                c.set(Calendar.MINUTE,i1);
                                c.setTimeZone(TimeZone.getDefault());
                                SimpleDateFormat simpleDateFormat =new SimpleDateFormat("k:mm");
                                String stime = simpleDateFormat.format(c.getTime());
//                                starttext.setText("Interval minutes: "+m_Text[0]+" and Start time");

                                //
                                Calendar calendar = Calendar.getInstance();
                                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                                int min = calendar.get(Calendar.MINUTE);
                                TimePickerDialog timePickerDialog=new TimePickerDialog(MainActivity.this, androidx.appcompat.R.style.Theme_AppCompat_Dialog, new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                        Calendar c =new GregorianCalendar();
                                        c.set(Calendar.HOUR_OF_DAY,i);
                                        c.set(Calendar.MINUTE,i1);
                                        c.setTimeZone(TimeZone.getDefault());
                                        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("k:mm");
                                        String etime = simpleDateFormat.format(c.getTime());
                                        myEdit.putString("weekendsint", m_Text[0]);
                                        myEdit.putString("weekendstime", stime);
                                        myEdit.putString("weekendetime", etime);
                                        endtext.setText("Interval Second: "+m_Text1[0]+" , Start time: "+stime+" End time: "+etime);
                                        myEdit.commit();
                                    }
                                },hour,min,false);
                                timePickerDialog.show();
                            }
                        },hour,min,false);
                        timePickerDialog.show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

    }

    private  void createnotification(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            CharSequence charSequence = "Test";
            String descption = "channel name";
            int imp = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel("Notification",charSequence,imp);
            notificationChannel.setDescription(descption);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}