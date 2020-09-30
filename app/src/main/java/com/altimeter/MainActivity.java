package com.altimeter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    LocationManager locationManager;
    LocationListener locationListener;
    TextView dispAlt,dispAcc,dispLal,dispLong,dispAddress;
    ConstraintLayout cons;

    public void startListening()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
    }
    public void changeDisp(double longitude,double latitude,double alt,float acc,Location location)
    {
        dispAlt.setText(alt + "");
        dispLong.setText(longitude + "°");
        dispLal.setText(latitude + "°");
        dispAcc.setText(acc + "");
        int i = -1;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String address = "Could Not Find Location!";
        try
        {
            List<Address> list  = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            if(list != null && list.size() > 0)
            {
                address = "";
                if(list.get(0).getThoroughfare() != null)
                    address += list.get(0).getThoroughfare() + ", ";
                if(list.get(0).getLocality() != null)
                    address += list.get(0).getLocality() + ", ";
                if(list.get(0).getAdminArea() != null)
                    address += list.get(0).getAdminArea() + ", ";
                if(list.get(0).getCountryName() != null)
                    address += list.get(0).getCountryName();
            }
            dispAddress.setText(address);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.d("rock","Adress problem!");
        }
        if(alt < 51)
            i = 1;
        else if(alt > 50 && alt < 101)
            i = 3;
        else if(alt > 100 && alt < 251)
            i = 2;
        else if(alt > 250 && alt < 501)
            i = 4;
        else if(alt > 500 && alt < 751)
            i = 5;
        else if(i > 750)
            i = 6;
        setBackground(i);

    }
    public void setBackground(int i)
    {
        if(i == -1)
            return;
        switch (i)
        {
            case 1:
                cons.setBackgroundResource(R.drawable.beach);
                break;
            case 2:
                cons.setBackgroundResource(R.drawable.nightcity);
                break;
            case 3:
                cons.setBackgroundResource(R.drawable.grassland);
                break;
            case 4:
                cons.setBackgroundResource(R.drawable.hills);
                break;
            case 5:
                cons.setBackgroundResource(R.drawable.mountain);
                break;
            case 6:
                cons.setBackgroundResource(R.drawable.peak);
                break;
            default:
                cons.setBackgroundResource(R.drawable.city);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dispAcc = (TextView) findViewById(R.id.dispAccuracy);
        dispAddress = (TextView)findViewById(R.id.dispInfo);
        cons = (ConstraintLayout) findViewById(R.id.cons);
        dispLal = (TextView) findViewById(R.id.dispLatitude);
        dispLong = (TextView) findViewById(R.id.dispLongitude);
        dispAlt = (TextView) findViewById(R.id.dispAltitude);
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("rock","Your Location is " + location.toString());
                
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                double alt = location.getAltitude();
                float acc = location.getAccuracy();
                String disp = "Longitude is " + longitude + " \nlatitude is " + latitude + " \nAltitude is " + alt + "\nAccuracy is " + acc;
                Log.d("rock","Details:- \n " + disp);
                changeDisp(longitude,latitude,alt,acc,location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this ,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastLocation != null)
                updateLocation(lastLocation);
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
    }

    public void updateLocation(Location lastLocation)
    {
        Log.d("rock","Your Location 2 " + lastLocation.toString());

    }
}
