package ch.templer.fragments.mapfragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

import ch.templer.activities.R;
import ch.templer.animation.FabTransformAnimation;
import ch.templer.animation.FloatingActionButtonTransitionAnimation;
import ch.templer.animation.ViewAppearAnimation;
import ch.templer.controls.fab.fabtoolbarlayout.FABToolbarLayout;
import ch.templer.controls.listener.AnimationFinishedListener;
import ch.templer.controls.reveallayout.RevealLayout;
import ch.templer.fragments.service.FragmentTransactionProcessingService;
import ch.templer.model.MapLocationModel;
import ch.templer.services.multimedia.SoundService;
import ch.templer.services.navigation.AvoidMode;
import ch.templer.services.navigation.Directions;
import ch.templer.services.navigation.NavigationMode;
import ch.templer.services.navigation.Navigator;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    private static final String MAP_FRAGMENT_MODEL_ID = "MAP_FRAGMENT_MODEL_ID";
    private static final String CURRENT_NAVIGATIONMODE = "CURRENT_NAVIGATIONMODE";
    private static final String CURRENT_LATITUDE = "CURRENT_LATITUDE";
    private static final String CURRENT_LONGITUTE = "CURRENT_LONGITUTE";
    private static final String IS_FABTOOLBARLAYOUT_SHOWN = "IS_FABTOOLBARLAYOUT_SHOWN";




    Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    double currentLatitude, currentLongitude;

    private GoogleMap mMap;

    private Marker destinationMarker;

    private View mRevealView;
    private RevealLayout mRevealLayout;
    private FABToolbarLayout fabToolbarLayout;
    private LinearLayout linearLayout;
    private CoordinatorLayout coordinatorLayout;
    private ImageView refresh;
    private ImageView car;
    private ImageView train;
    private ImageView walk;

    private NavigationMode prevMode;
    private Navigator navigator;
    private String currentSnackbarDescription;

    private OnFragmentInteractionListener mListener;
    private Snackbar mySnackbar;
    private MapLocationModel mapLocationModel;
    private Location destinationLocation = new Location("destination");
    private boolean isFabToolbarLayoutShown = false;

    // Vibrate pattern
    int dot = 300;      // Length of a Morse Code "dot" in milliseconds
    int short_gap = 200;    // Length of Gap Between dots/dashes
    long[] pattern = {
            0,  // Start immediately
            dot, short_gap, dot, short_gap, dot};

    public MapFragment() {

    }

    public static MapFragment newInstance(MapLocationModel mapLocationModel) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putSerializable(MAP_FRAGMENT_MODEL_ID, mapLocationModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mapLocationModel = (MapLocationModel) getArguments().getSerializable(MAP_FRAGMENT_MODEL_ID);
        }

        destinationLocation.setLatitude(mapLocationModel.getLatitude());
        destinationLocation.setLongitude(mapLocationModel.getLongitude());

        buildGoogleApiClient();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        //restore state
        if (savedInstanceState != null) {
            prevMode = (NavigationMode) savedInstanceState.getSerializable(CURRENT_NAVIGATIONMODE);
            currentLatitude = savedInstanceState.getDouble(CURRENT_LATITUDE);
            currentLongitude = savedInstanceState.getDouble(CURRENT_LONGITUTE);
            isFabToolbarLayoutShown = savedInstanceState.getBoolean(IS_FABTOOLBARLAYOUT_SHOWN);
        }

        mRevealLayout = (RevealLayout) view.findViewById(R.id.reveal_layout);
        mRevealView = view.findViewById(R.id.reveal_view);
        mRevealView.setBackgroundColor(mapLocationModel.getNextFragmentBackroundColor());

        fabToolbarLayout = (FABToolbarLayout) view.findViewById(R.id.fabtoolbar);
        fabtoolbar_fab = (FloatingActionButton) view.findViewById(R.id.fabtoolbar_fab);
        fabtoolbar_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabToolbarLayout.show();
            }
        });

        refresh = (ImageView) view.findViewById(R.id.refresh);
        car = (ImageView) view.findViewById(R.id.car);
        train = (ImageView) view.findViewById(R.id.train);
        walk = (ImageView) view.findViewById(R.id.walk);

        refresh.setOnClickListener(this);
        car.setOnClickListener(this);
        train.setOnClickListener(this);
        walk.setOnClickListener(this);
        lockNavigationModeButtons();

        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.CoordinatorLayout);

        FragmentManager fragmentManager = getChildFragmentManager();

        SupportMapFragment test = (SupportMapFragment) fragmentManager.findFragmentById(R.id.map);

        test.getMapAsync(this);

        linearLayout = (LinearLayout) view.findViewById(R.id.fabtoolbar_toolbar);

        return view;
    }

    private FloatingActionButton fabtoolbar_fab;

    protected void transformFab() {
        SoundService.playSound(this.getContext(),R.raw.navigation_success);
        fabToolbarLayout.hide();
        Vibrator v = (Vibrator) this.getContext().getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(pattern,-1);
        showSnackbar("Location Reached", Snackbar.LENGTH_INDEFINITE);
        fabToolbarLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.bright_green));
                Drawable drawable = getResources().getDrawable(R.drawable.ic_arrow_forward_24dp, null);
                FabTransformAnimation fabTransformAnimation = new FabTransformAnimation(fabtoolbar_fab, colorStateList, drawable);
                fabTransformAnimation.setAnimationFinishedListener(new AnimationFinishedListener() {
                    @Override
                    public void onAnimationFinished() {
                        FragmentTransaction transaction = FragmentTransactionProcessingService.prepareNextFragmentTransaction(getFragmentManager().beginTransaction());
                        FloatingActionButtonTransitionAnimation floatingActionButtonAnimationOnClickListener = new FloatingActionButtonTransitionAnimation(fabtoolbar_fab, mRevealView, mRevealLayout, mySnackbar, transaction);
                        fabtoolbar_fab.setOnClickListener(floatingActionButtonAnimationOnClickListener);
                    }
                });
                fabTransformAnimation.runAnimation();
            }
        }, 500);
    }

    private void showSnackbar(String message, int length) {
//        if (mySnackbar != null){
//            mySnackbar.dismiss();
//        }
        Spanned formatedMessage = Html.fromHtml(message);
        mySnackbar = Snackbar.make(coordinatorLayout, formatedMessage, length);
        View snackbarView = mySnackbar.getView();
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setMaxLines(4);
        mySnackbar.setAction("Dismiss", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mySnackbar.dismiss();
            }
        });
        mySnackbar.show();
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
        LatLng destinationCoordinates = new LatLng(mapLocationModel.getLatitude(), mapLocationModel.getLongitude());
        destinationMarkerOptions.position(destinationCoordinates);
        destinationMarker = mMap.addMarker(destinationMarkerOptions);

        ViewAppearAnimation.runAnimation(this.getView(), 1000);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(mapLocationModel.getLatitude(), mapLocationModel.getLongitude())));
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

        // as soon as map is ready navigation requests are allowed
        unlockNavigationModeButtons();

        //Initialize Navigator
        setUpNavigator();
        //restore potential navigation state after rotation
        if (prevMode != null){
            navigator.setMode(prevMode,AvoidMode.TOLLS);
            navigator.findDirections(false);
        }
        //restore FabToolbarLayout if allready shown
//        if (isFabToolbarLayoutShown){
//            fabToolbarLayout.show();
//        }
    }

    synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this.getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(CURRENT_NAVIGATIONMODE, prevMode);
        outState.putDouble(CURRENT_LATITUDE, currentLatitude);
        outState.putDouble(CURRENT_LONGITUTE, currentLongitude);
        outState.putBoolean(IS_FABTOOLBARLAYOUT_SHOWN, fabToolbarLayout.isShown());
    }


    @Override
    public void onClick(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(this.getContext(), R.anim.image_click));
        //avvoid multiple immediate clicks
        lockNavigationModeButtons();
        setUpNavigator();
        switch (view.getId()) {
            case R.id.walk:
                navigator.setMode(NavigationMode.WALKING, AvoidMode.TOLLS);
                prevMode = NavigationMode.WALKING;
                break;
            case R.id.train:
                navigator.setMode(NavigationMode.TRANSIT, AvoidMode.TOLLS);
                prevMode = NavigationMode.TRANSIT;
                break;
            case R.id.car:
                navigator.setMode(NavigationMode.DRIVING, AvoidMode.TOLLS);
                prevMode = NavigationMode.DRIVING;
                break;
            case R.id.refresh:
                if (prevMode == null) {
                    return;
                }
                navigator.setMode(prevMode, AvoidMode.TOLLS);
                break;
        }
        navigator.findDirections(false);

        fabToolbarLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                unlockNavigationModeButtons();
            }
        },2000);
    }

    private void lockNavigationModeButtons() {
        refresh.setClickable(false);
        car.setClickable(false);
        train.setClickable(false);
        walk.setClickable(false);
    }

    private void unlockNavigationModeButtons() {
        refresh.setClickable(true);
        car.setClickable(true);
        train.setClickable(true);
        walk.setClickable(true);
    }

    private void setUpNavigator() {
        LatLng startPosition = new LatLng(currentLatitude, currentLongitude);
        LatLng destinationCoordinates = new LatLng(mapLocationModel.getLatitude(), mapLocationModel.getLongitude());
        if (navigator == null) {
            navigator = new Navigator(mMap, startPosition, destinationCoordinates, Locale.getDefault().getLanguage());
            navigator.setNavigationDescriptionListener(new Navigator.NavigationDescriptionListener() {
                @Override
                public void onNavigationDescription(String descitpion) {
                    currentSnackbarDescription = descitpion;

                    fabToolbarLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mySnackbar != null) {
                                mySnackbar.dismiss();
                            }
                            fabToolbarLayout.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    showSnackbar(currentSnackbarDescription, Snackbar.LENGTH_INDEFINITE);
                                }
                            }, 500);

                        }
                    }, 500);
                }
            });
        } else {
            navigator.clear();
            navigator.setStartPosition(startPosition);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000); // Update location every second

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            currentLatitude = mLastLocation.getLatitude();
            currentLongitude = mLastLocation.getLongitude();

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    boolean isLocationReached = false;

    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
        if (location.distanceTo(destinationLocation) <= mapLocationModel.getRadius() && !isLocationReached) {
            isLocationReached = true;
            finishNavigation();
        }
    }

    private void finishNavigation() {
        transformFab();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        buildGoogleApiClient();
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
