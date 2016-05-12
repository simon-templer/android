package ch.templer.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import ch.templer.animation.ViewAppearAnimation;
import ch.templer.services.location.LocationService;
import ch.templer.model.MapLocationModel;
import ch.templer.services.navigation.Navigator;
import ch.templer.utils.Colors;

public class MapLocationFragment extends SupportMapFragment implements OnMapReadyCallback, LocationService.LocationChangedListener, LocationService.LocationReachedListener {
    private static final String DESTINATION_LONGITUTE = "DESTINATION_LONGITUTE";
    private static final String DESTINATION_LATITUDE = "DESTINATION_LATITUDE";
    private static final String VALIDATION_RADIUS = "VALIDATION_RADIUS";
    private static final String TIME_INTERVAL = "TIME_INTERVAL";
    private static final String MIN_DISTANCE = "MIN_DISTANCE";
    private static final String BACKGROUND_COLOR_ID = "BACKGROUND_COLOR_ID";
    private static final String NEXT_FRAGMENT_BACKGROUND_COLOR_ID = "NEXT_FRAGMENT_BACKGROUND_COLOR_ID";

    private double destinationLongitute;
    private double destinationLatitude;
    //radius in meters
    private int validationRadius;

    private int timeInterval;
    private int minDistance;

    private int backgroundColor;
    private int nextFragmentBackgroundColor;

    private LocationService locationService;
    private GoogleMap mMap;

    private Marker currentPostitionMarker;
    private Marker destinationMarker;

    private OnFragmentInteractionListener mListener;

    public MapLocationFragment() {
        // Required empty public constructor
    }

    public static MapLocationFragment newInstance(MapLocationModel mapLocationModel) {
        MapLocationFragment fragment = new MapLocationFragment();
        Bundle args = new Bundle();
        args.putDouble(DESTINATION_LONGITUTE, mapLocationModel.getLongitude());
        args.putDouble(DESTINATION_LATITUDE, mapLocationModel.getLatitude());
        args.putInt(VALIDATION_RADIUS, mapLocationModel.getRadius());
        args.putInt(TIME_INTERVAL, mapLocationModel.getTimeInterval());
        args.putInt(MIN_DISTANCE, mapLocationModel.getMinDistance());
        args.putInt(BACKGROUND_COLOR_ID, mapLocationModel.getBackgroundColor());
        args.putInt(NEXT_FRAGMENT_BACKGROUND_COLOR_ID, mapLocationModel.getNextFragmentBackroundColor());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            destinationLongitute = getArguments().getDouble(DESTINATION_LONGITUTE);
            destinationLatitude = getArguments().getDouble(DESTINATION_LATITUDE);
            validationRadius = getArguments().getInt(VALIDATION_RADIUS);
            timeInterval = getArguments().getInt(TIME_INTERVAL);
            minDistance = getArguments().getInt(MIN_DISTANCE);
            backgroundColor  = getArguments().getInt(BACKGROUND_COLOR_ID);
            nextFragmentBackgroundColor =  getArguments().getInt(NEXT_FRAGMENT_BACKGROUND_COLOR_ID);
        }

        locationService = new LocationService(this.getContext(), timeInterval, minDistance);
        locationService.setLocationChangedListener(this);
        locationService.setLocationReachedListener(this, destinationLongitute, destinationLatitude, validationRadius);

        this.getMapAsync(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.getView().setBackgroundColor(Colors.Emerald);
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
        MarkerOptions destinationMarkerOptions = new MarkerOptions();
        destinationMarkerOptions.title("Destination");
        destinationMarkerOptions.snippet("Marker Xo Xo");
        destinationMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        LatLng destinationCoordinates = new LatLng(destinationLatitude, destinationLongitute);
        destinationMarkerOptions.position(destinationCoordinates);
        destinationMarker = mMap.addMarker(destinationMarkerOptions);

        ViewAppearAnimation.runAnimation(this.getView(), 1000);
    }


    @Override
    public void onLocationChanged(Location location) {
        if (mMap != null){
//            if (currentPostitionMarker != null) {
//                currentPostitionMarker.remove();
//            }
//            currentPostitionMarker = moveMarker(location);

            LatLng testdestinationCoordinates = new LatLng(location.getLatitude(), location.getLongitude());
            LatLng destinationCoordinates = new LatLng(destinationLatitude, destinationLongitute);
            Navigator nav = new Navigator(mMap,testdestinationCoordinates,destinationCoordinates);
            //nav.setMode(1,System.currentTimeMillis(),0);
            nav.findDirections(false);

            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        }
    }

    private Marker moveMarker(Location location) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title("CurrentPosition");
        markerOptions.snippet("Current Position");
//        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.current_postition_icon));
        LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
        markerOptions.position(currentPosition);
        currentPostitionMarker = mMap.addMarker(markerOptions);
        return currentPostitionMarker;
    }

    @Override
    public void onLocationReached(Location location) {
        String msg = location.getLatitude() + ", " + location.getLongitude();
        Toast.makeText(this.getContext(), "Location reached event", Toast.LENGTH_LONG).show();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
