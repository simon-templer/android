package ch.templer.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import ch.templer.activities.R;
import ch.templer.location.LocationService;
import ch.templer.model.MapLocationModel;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapLocationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapLocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapLocationFragment extends SupportMapFragment implements OnMapReadyCallback, LocationService.LocationChangedListener, LocationService.LocationReachedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String DESTINATION_LONGITUTE = "DESTINATION_LONGITUTE";
    private static final String DESTINATION_LATITUDE = "DESTINATION_LATITUDE";
    private static final String VALIDATION_RADIUS = "VALIDATION_RADIUS";
    private static final String TIME_INTERVAL = "TIME_INTERVAL";
    private static final String MIN_DISTANCE = "MIN_DISTANCE";

    private double destinationLongitute;
    private double destinationLatitude;
    //radius in meters
    private int validationRadius;

    private int timeInterval;
    private int minDistance;

    private LocationService locationService;
    private GoogleMap mMap;

    private Marker currentPostitionMarker;
    private Marker destinationMarker;

    private OnFragmentInteractionListener mListener;

    public MapLocationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment MapLocationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapLocationFragment newInstance(MapLocationModel mapLocationModel) {
        MapLocationFragment fragment = new MapLocationFragment();
        Bundle args = new Bundle();
        args.putDouble(DESTINATION_LONGITUTE, mapLocationModel.getLongitude());
        args.putDouble(DESTINATION_LATITUDE, mapLocationModel.getLatitude());
        args.putInt(VALIDATION_RADIUS, mapLocationModel.getRadius());
        args.putInt(TIME_INTERVAL, mapLocationModel.getTimeInterval());
        args.putInt(MIN_DISTANCE, mapLocationModel.getMinDistance());
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
        }

        locationService = new LocationService(this.getContext(), timeInterval, minDistance);
        locationService.setLocationChangedListener(this);
        locationService.setLocationReachedListener(this, destinationLongitute, destinationLatitude, validationRadius);
        this.getMapAsync(this);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
    }


    @Override
    public void onLocationChanged(Location location) {
        if (mMap != null){
            if (currentPostitionMarker != null) {
                currentPostitionMarker.remove();
            }
            currentPostitionMarker = moveMarker(location);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        }
    }

    private Marker moveMarker(Location location) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title("CurrentPosition");
        markerOptions.snippet("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.current_postition_icon));
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
