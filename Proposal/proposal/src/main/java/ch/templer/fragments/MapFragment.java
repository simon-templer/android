package ch.templer.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import ch.templer.activities.R;
import ch.templer.animation.FloatingActionButtonTransitionAnimation;
import ch.templer.animation.ViewAppearAnimation;
import ch.templer.controls.fabtoolbarlayout.FABToolbarLayout;
import ch.templer.controls.reveallayout.RevealLayout;
import ch.templer.fragments.service.FragmentTransactionProcessingService;
import ch.templer.services.location.LocationService;
import ch.templer.model.MapLocationModel;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {
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

    private FloatingActionButton floatingActionButton;
    private View mRevealView;
    private RevealLayout mRevealLayout;
    private FABToolbarLayout fabToolbarLayout;
    private Fragment fragment;

    private OnFragmentInteractionListener mListener;

    public MapFragment() {
        // Required empty public constructor
    }

    public static MapFragment newInstance(MapLocationModel mapLocationModel) {
        MapFragment fragment = new MapFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.floatingButton);
        mRevealLayout = (RevealLayout) view.findViewById(R.id.reveal_layout);
        mRevealView = view.findViewById(R.id.reveal_view);
        mRevealView.setBackgroundColor(nextFragmentBackgroundColor);

        FragmentTransaction transaction = FragmentTransactionProcessingService.prepareNextFragmentTransaction(getFragmentManager().beginTransaction());

        FloatingActionButtonTransitionAnimation floatingActionButtonAnimationOnClickListener = new FloatingActionButtonTransitionAnimation(floatingActionButton,mRevealView, mRevealLayout, transaction);
        floatingActionButton.setOnClickListener(floatingActionButtonAnimationOnClickListener);

//        fabToolbarLayout = (FABToolbarLayout) view.findViewById(R.id.fabtoolbar);
//        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fabtoolbar_fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fabToolbarLayout.show();
//            }
//        });

        FragmentManager fragmentManager = getChildFragmentManager();

        SupportMapFragment test = (SupportMapFragment)fragmentManager.findFragmentById(R.id.map);

        test.getMapAsync(this);


        return view;
    }

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
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                fabToolbarLayout.hide();
            }
        });
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


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
