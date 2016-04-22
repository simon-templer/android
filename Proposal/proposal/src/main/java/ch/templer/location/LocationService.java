package ch.templer.location;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

/**
 * Created by Templer on 3/22/2016.
 */

public class LocationService implements android.location.LocationListener {

    public interface LocationChangedListener {
        void onLocationChanged(Location location);
    }

    public interface LocationReachedListener {
        void onLocationReached(Location location);
    }

    private LocationManager locationManager;
    private Context context;
    private LocationChangedListener locationChangedListener;
    private LocationReachedListener locationReachedListener;

    private double longitude;
    private double latitude;
    private int radius;


    private static int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;

    public LocationService(Context context, int timeInterval, int minDistance) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        PackageManager manager = context.getPackageManager();
        this.context = context;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
            }
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                2000, 1, this);
    }


    private void TryGetLocationAsync() {
    }

    public void setLocationChangedListener(LocationChangedListener eventListener) {
        locationChangedListener = eventListener;
    }

    public void setLocationReachedListener(LocationReachedListener eventListener, double longitude, double latitude, int radius) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.radius = radius;
        this.locationReachedListener = eventListener;
    }

    @Override
    public void onLocationChanged(Location location) {
        locationChangedListener.onLocationChanged(location);
        if (locationReached(location)) {
            locationReachedListener.onLocationReached(location);
        }

    }

    private boolean locationReached(Location location) {
        //calculate id point is inside destination circle (position + radius)
        float[] results = new float[1];
        Location.distanceBetween(this.latitude, this.longitude, location.getLatitude(), location.getLongitude(), results);
        float distanceInMeters = results[0];
        return distanceInMeters < this.radius;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        context.startActivity(intent);
        Toast.makeText(context, "Gps is turned on!! ",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(context, "Gps is turned off!! ",
                Toast.LENGTH_SHORT).show();
    }
}
