package com.vishalsharma.mydemo19;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) { //method call onCreate to be used during application start
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main); //Setting XML view to be used with this activity

    }

    //function to see visualization of BPM Values on thingspeak cloud
    public void bpmup(View view) {

        Intent bibpm = new Intent(Intent.ACTION_VIEW, Uri.parse("https://thingspeak.com/channels/428871"));
        startActivity(bibpm);
    }
    //function to see visualization of TEMPRATURE Values on thingspeak cloud
    public void tempup(View view) {

        Intent bitemp = new Intent(Intent.ACTION_VIEW, Uri.parse("https://thingspeak.com/channels/414656"));
        startActivity(bitemp);
    }

    public void medicsvdl(View view) {

        Intent bicsv = new Intent(Intent.ACTION_VIEW, Uri.parse("https://thingspeak.com/channels/459257/feed.csv"));
        startActivity(bicsv);
    }

}
