package com.vishalsharma.mydemo15;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Random;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class MainActivity extends AppCompatActivity {

    AlarmManager alarmManager;
    private PendingIntent pending_intent;

    private TimePicker alarmTimePicker;
    private TextView alarmTextView;

    private AlarmReceiver alarm;


    MainActivity inst;
    Context context;

    String str;
    String hup;
    String mup;
    int hours1;
    int mins1;
    int hour;
    int minute;
    SQLiteDatabase articlesDb;//including SQLiteDatabase and naming it

    @Override
    protected void onCreate(Bundle savedInstanceState) { //method call onCreate to be used during application start
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//Setting XML view to be used with this activity

        TextView myView = (TextView)findViewById(R.id.checkid);//Naming textview with id checkid
        articlesDb = this.openOrCreateDatabase("times", MODE_PRIVATE, null);//Creating a database or opening if already present
        articlesDb.execSQL("CREATE TABLE IF NOT EXISTS newtime(id INTEGER PRIMARY KEY, hours INTEGER, mins INTEGER)");//Creating a table if not already present
        Cursor cur = articlesDb.rawQuery("SELECT COUNT(*) FROM newtime", null);//setting a variable to traverse through table
        if (cur != null) {
            cur.moveToFirst();
            if (cur.getInt (0) == 0) { //if table is empty inserting one row
                articlesDb.execSQL("INSERT INTO newtime (id, hours, mins) VALUES (1, 100, 21)");
            }
        }
        Cursor c = articlesDb.rawQuery("SELECT * FROM newtime", null);//setting another variable to traverse through table
        int hin = c.getColumnIndex("hours");//storing index of respective column title
        int minin = c.getColumnIndex("mins");//storing index of respective column title
        c.moveToFirst();//arranging traversing variable to beggining
        hours1 = c.getInt(hin);//extracting values from table
        mins1 = c.getInt(minin);//extracting values from table
        if(hours1 == 100) {//checking if any alarm is set
            str = " ";
            myView.setText(str);
        }
        else{
            if(mins1 < 10){
                str = "Alarm-> " + hours1 + " : 0" + mins1;//adding 0 in minutes to improve readability
            }
            else{
                str = "Alarm-> " + hours1 + " : " + mins1;
            }
            myView.setText(str);//setting text in textview
        }


        this.context = this;


        alarmTextView = (TextView) findViewById(R.id.alarmText);//Naming textview with id alarmText

        final Intent myIntent = new Intent(this.context, AlarmReceiver.class);//Creating intent to communicate with AlarmReceiver java class


        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);//creating variable of alarm manager to get alarm service


        final Calendar calendar = Calendar.getInstance();//creating variable of calendar type to get time details

        alarmTimePicker = (TimePicker) findViewById(R.id.alarmTimePicker);//Naming timepicker with id alarmTimePicker



        Button start_alarm= (Button) findViewById(R.id.start_alarm);//setting up button with id start_alarm
        start_alarm.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)

            @Override
            public void onClick(View v) {

                calendar.add(Calendar.SECOND, 0);//initializing calendar object


                hour = alarmTimePicker.getCurrentHour();//getting hours set by user
                minute = alarmTimePicker.getCurrentMinute();//getting minutes set by user

                Log.e("MyActivity", "In the receiver with " + hour + " and " + minute);//setting log in case of error
                setAlarmText("You clicked a " + hour + " and " + minute);//setting alarm text
                //updating database hours and minutes of alarm
                hup = "UPDATE newtime SET hours ="+ hour;
                articlesDb.execSQL(hup);
                mup = "UPDATE newtime SET mins ="+ minute;
                articlesDb.execSQL(mup);


                calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());//setting hour and minute of required time
                calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());

                myIntent.putExtra("extra", "yes");//initializing intent
                pending_intent = PendingIntent.getBroadcast(MainActivity.this, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);//changing the pendingintent extra value

                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);//setting alarm



                if(minute < 10){
                    setAlarmText("Alarm set to " + hour + ": 0" + minute);//adding 0 in minutes to improve readability
                }
                else{
                    setAlarmText("Alarm set to " + hour + ":" + minute);
                }
            }

        });

        Button stop_alarm= (Button) findViewById(R.id.stop_alarm);
        stop_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int random_number = 1;//setting number to associate for ringtone playing
                Log.e("random number is ", String.valueOf(random_number));//setting log in case of error
                articlesDb.execSQL("UPDATE newtime SET hours = 100");//updating database

                myIntent.putExtra("extra", "no");//calling intent
                sendBroadcast(myIntent);//communicating intent

                alarmManager.cancel(pending_intent);//cancelling alarm
                setAlarmText("is canceled");//setting tectview
                Uri uri = Uri.parse("https://maker.ifttt.com/trigger/Daily_Alert/with/key/kC5uBCvbLmKvSnintdCRo"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);//going to link to trigger mail at alarm shutdown
            }
        });

    }
    public void passer1(View view)// linking button to another activity
    {
        Intent intent = new Intent(MainActivity.this, Information.class);
        startActivity(intent);
    }

    public void setAlarmText(String alarmText) { alarmTextView.setText(alarmText);}//function to set textview



    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.e("MyActivity", "on Destroy");
    }





}
