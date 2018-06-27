package com.vishalsharma.mydemo15;

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

public class Information extends AppCompatActivity implements LocationListener {

    Button getLocationBtn;
    TextView latText;
    TextView lonText;
    double lati;
    double longi;

    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) { //method call onCreate to be used during application start
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.info); //Setting XML view to be used with this activity
        //naming textview and buttons variables
        getLocationBtn = (Button)findViewById(R.id.getLocationBtn);
        latText = (TextView)findViewById(R.id.latText);
        lonText = (TextView)findViewById(R.id.lonText);

        //checking for permissions and requesting if not given
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }


        getLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        }); //launching function getLocation on clicking button
    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);//setting up location manager
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);//requesting location manager
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        lati=location.getLatitude();//getting latitude
        longi=location.getLongitude();//getting longitude
        latText.setText("Latitude: " + lati);//printing values
        lonText.setText("Longitude: " + longi);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(Information.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();//if no service is giving location
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }
    //function to update latitude to thingspeak cloud
    public void uplat1(View view) {

        String link = "https://api.thingspeak.com/update?api_key=0TEBLHFR0NK3JX9B&field1=";
        String linkf = link+lati;
        Intent bilat = new Intent(Intent.ACTION_VIEW, Uri.parse(linkf));
        startActivity(bilat);
    }
    //function to update longitude to thingspeak cloud
    public void uplon1(View view) {

        String link = "https://api.thingspeak.com/update?api_key=KQDCZVXVPB6LBJ79&field1=";
        String linkf = link+longi;
        Intent bilon = new Intent(Intent.ACTION_VIEW, Uri.parse(linkf));
        startActivity(bilon);
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
