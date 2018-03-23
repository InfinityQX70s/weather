package com.infinity.weather;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import android.support.v7.app.AlertDialog;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.infinity.weather.model.latlong.LatLong;
import com.infinity.weather.model.weather.Weather;
import com.infinity.weather.network.LatitudeLongitudeRequest;
import com.infinity.weather.network.WeatherRequest;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import android.support.v4.content.ContextCompat;
import android.widget.Toast;

/**
 * Created by m.mazurkevich on 12.08.15.
 */
public class SplashActivity extends RoboSpiceActivity {

    private static final String DB_NAME = "city";
    private static final int REQUEST_GEO_POSITION = 50;
    private static final int DB_VERSION = 1;

    private Context mContext;
    private LocationManager locationManager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        mContext = this;
        checkLocationPermission();
//        DataBaseHelper data = new DataBaseHelper(this, DB_NAME, null, DB_VERSION);
//        data.deleteAllElements();
//        data.insertElement(new Element("2471217", "Philadelphia"));
//        data.insertElement(new Element("2459115", "New York"));
//        data.insertElement(new Element("2442047", "Los Angeles"));
//        data.insertElement(new Element("2379574", "Chicago"));
//        data.insertElement(new Element("2424766", "Houston"));
//        data.close();
    }


    private void checkLocationPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, "GeoLocation permission is needed to get your precise location",
                        Toast.LENGTH_SHORT).show();
            }


            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_GEO_POSITION);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        } else {
            checkAvaliableLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_GEO_POSITION: {
                checkAvaliableLocation();
            }
        }
    }


    private void checkAvaliableLocation() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Boolean isEnabled = locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER);
        if (!isEnabled) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
            dialog.setTitle("Change settings");
            dialog.setMessage("Current location is not available. Do you want to enable it?");
            dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    mContext.startActivity(intent);
                }
            });
            dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                }
            });
            dialog.show();
        } else {
            Location locationl = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            LatitudeLongitudeRequest mLatLongRequest;
            if (locationl == null)
                mLatLongRequest = new LatitudeLongitudeRequest(59.9321, 30.1968);
            else
                mLatLongRequest = new LatitudeLongitudeRequest(locationl.getLatitude(), locationl.getLongitude());
            //TODO: woeid по широте и долготе
            getSpiceManager().execute(mLatLongRequest, new LatLongRequestListener());
        }
    }

    private final class LatLongRequestListener implements RequestListener<LatLong> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
        }

        @Override
        public void onRequestSuccess(LatLong result) {
            if (result != null) {
                String woeid = result.getQuery().getResults().getResult().getWoeid();
                WeatherRequest mWeatherRequest = new WeatherRequest(woeid); //TODO: погода по woeid
                getSpiceManager().execute(mWeatherRequest, new WeatherRequestListener());
            }

        }
    }

    private final class WeatherRequestListener implements RequestListener<Weather> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
        }

        @Override
        public void onRequestSuccess(Weather result) {
            if (result != null) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.putExtra(MainActivity.WEATHER_OBJECT, new Gson().toJson(result));
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.splash_start, R.anim.splash_stop);
            }
        }
    }
}
