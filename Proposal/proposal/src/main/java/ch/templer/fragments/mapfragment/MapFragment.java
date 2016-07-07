package ch.templer.fragments.mapfragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ch.templer.activities.R;
import ch.templer.activities.settingsactivity.SettingsActivity;
import ch.templer.animation.FabTransformAnimation;
import ch.templer.animation.ViewAppearAnimation;
import ch.templer.controls.alertdialog.SpotsDialog;
import ch.templer.controls.fab.fabtoolbarlayout.FABToolbarLayout;
import ch.templer.controls.listener.AnimationFinishedListener;
import ch.templer.controls.reveallayout.RevealLayout;
import ch.templer.fragments.AbstractFragment;
import ch.templer.fragments.service.SnackbarService;
import ch.templer.model.ColorTheme;
import ch.templer.model.MapLocationModel;
import ch.templer.services.SettingsService;
import ch.templer.services.multimedia.SoundService;
import ch.templer.services.navigation.Navigator;
import ch.templer.services.navigation2.AbstractRouting;
import ch.templer.services.navigation2.Route;
import ch.templer.services.navigation2.RouteException;
import ch.templer.services.navigation2.Routing;
import ch.templer.services.navigation2.RoutingListener;
import ch.templer.utils.logging.Logger;

public class MapFragment extends AbstractFragment implements OnMapReadyCallback, View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener, RoutingListener {
    //Staticidentifier
    private static final String MAP_FRAGMENT_MODEL_ID = "MAP_FRAGMENT_MODEL_ID";
    private static final String CURRENT_NAVIGATIONMODE = "CURRENT_NAVIGATIONMODE";
    private static final String CURRENT_LATITUDE = "CURRENT_LATITUDE";
    private static final String CURRENT_LONGITUTE = "CURRENT_LONGITUTE";
    private static final String IS_FABTOOLBARLAYOUT_SHOWN = "IS_FABTOOLBARLAYOUT_SHOWN";
    private static final String IS_LOCATION_REACHED_ID = "IS_LOCATION_REACHED_ID";

    //UI member variables
    private View mRevealView;
    private RevealLayout mRevealLayout;
    private FABToolbarLayout fabToolbarLayout;
    private CoordinatorLayout coordinatorLayout;
    private ImageView refresh;
    private ImageView car;
    private ImageView train;
    private ImageView walk;
    private Marker destinationMarker;
    private FloatingActionButton fabtoolbar_fab;
    private AlertDialog progressDialog;

    //Location member variables
    Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    double currentLatitude, currentLongitude;
    private GoogleMap mMap;
    private List<Polyline> polylines = new ArrayList<>();

    //Navigation member Variables
    private AbstractRouting.TravelMode prevMode;
    private Navigator navigator;
    private Location destinationLocation;

    // Vibrate pattern
    int dot = 300;      // Length of a Morse Code "dot" in milliseconds
    int short_gap = 200;    // Length of Gap Between dots/dashes
    long[] pattern = {
            0,  // Start immediately
            dot, short_gap, dot, short_gap, dot};

    //misc
    private String currentSnackbarDescription;
    private MapLocationModel mapLocationModel;
    private boolean isFabToolbarLayoutShown = false;
    private int polylineColor = Color.rgb(66, 133, 244);
    private Handler mHandler = new Handler();
    public OnFragmentInteractionListener mListener;
    private static Logger log = Logger.getLogger();
    private ColorTheme colorTheme;

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
            colorTheme = ColorTheme.constructColorTheme(mapLocationModel.getFragmentColors().getColorThemeType(), this.getContext());
        }
        destinationLocation = new Location(getString(R.string.map_fragment_destination_title));
        destinationLocation.setLatitude(mapLocationModel.getLatitude());
        destinationLocation.setLongitude(mapLocationModel.getLongitude());
        buildGoogleApiClient();
    }

    synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this.getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        //restore state
        if (savedInstanceState != null) {
            prevMode = (AbstractRouting.TravelMode) savedInstanceState.getSerializable(CURRENT_NAVIGATIONMODE);
            currentLatitude = savedInstanceState.getDouble(CURRENT_LATITUDE);
            currentLongitude = savedInstanceState.getDouble(CURRENT_LONGITUTE);
            isFabToolbarLayoutShown = savedInstanceState.getBoolean(IS_FABTOOLBARLAYOUT_SHOWN);
        }

        //setup UI
        mRevealLayout = (RevealLayout) view.findViewById(R.id.reveal_layout);
        mRevealView = view.findViewById(R.id.reveal_view);
        mRevealView.setBackgroundColor(mapLocationModel.getFragmentColors().getNextFragmentBackroundColor());

        fabToolbarLayout = (FABToolbarLayout) view.findViewById(R.id.fabtoolbar);
        fabtoolbar_fab = (FloatingActionButton) view.findViewById(R.id.fabtoolbar_fab);
        boolean debuggingEnabled = SettingsService.getInstance().getBooleanSetting(SettingsActivity.GENERAL_DEBUGGING_SWITCH, false);
        if (!this.isFragmentFinished()) {
            fabtoolbar_fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_navigation_white_40dp, null));
            fabtoolbar_fab.setBackgroundTintList(ColorStateList.valueOf(colorTheme.getMainColor()));
            fabtoolbar_fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fabToolbarLayout.show();
                }
            });
        } else {
            fabtoolbar_fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_forward_24dp, null));
            fabtoolbar_fab.setBackgroundTintList(ColorStateList.valueOf(colorTheme.getMainColor()));
            ColorStateList colorStateList = ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.bright_green));
            fabtoolbar_fab.setBackgroundTintList(colorStateList);
            prepareNextFragmentNavigation();
        }

        if (debuggingEnabled && !isFragmentFinished()) {
            fabToolbarLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finishNavigation();
                }
            }, 5000);
        }

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

        //get Google Map API v2 MapFragment
        FragmentManager fragmentManager = getChildFragmentManager();
        SupportMapFragment supportMapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);

        //check if GPS is available/active
        checkGPSEnabled();

        return view;
    }

    private void checkGPSEnabled() {
        LocationManager lm = (LocationManager) this.getContext().getSystemService(Context.LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) && !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            SnackbarService.showSnackbar(this.getContext(), coordinatorLayout, colorTheme.getMainColor(), getString(R.string.map_fragment_gps_unavailable_error_message), Snackbar.LENGTH_INDEFINITE, true);
        }
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
        //setUpNavigator();
        switch (view.getId()) {
            case R.id.walk:
                prevMode = AbstractRouting.TravelMode.WALKING;
                break;
            case R.id.train:
                prevMode = AbstractRouting.TravelMode.TRANSIT;
                break;
            case R.id.car:
                prevMode = AbstractRouting.TravelMode.DRIVING;
                break;
            case R.id.refresh:
                if (prevMode == null) {
                    unlockNavigationModeButtons();
                    return;
                }
                break;
        }
        startNavigation();

        //delay unlocking the Navigation Buttons. This avoid to many requests.
        fabToolbarLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                unlockNavigationModeButtons();
            }
        }, 2000);
    }

    //Callback when GoogleMap is ready for use. The Fragment is only usable after this method call
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // hide toolbar when map is clicked
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                fabToolbarLayout.hide();
            }
        });
        mMap.getUiSettings().setMapToolbarEnabled(false);

        //requestin location service
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

        //setting up destination marker with complete address information
        MarkerOptions destinationMarkerOptions = new MarkerOptions();
        destinationMarkerOptions.title(getString(R.string.map_fragment_destination_title));
        destinationMarkerOptions.snippet(mapLocationModel.getLatitude() + "/" + mapLocationModel.getLongitude());
        destinationMarkerOptions.snippet(getCompleteAddressString(mapLocationModel.getLatitude(), mapLocationModel.getLongitude()));
        destinationMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        LatLng destinationCoordinates = new LatLng(mapLocationModel.getLatitude(), mapLocationModel.getLongitude());
        destinationMarkerOptions.position(destinationCoordinates);
        destinationMarker = mMap.addMarker(destinationMarkerOptions);
        destinationMarker.showInfoWindow();

        // Fragment might be reconstructed after a onDestroy(). In these situations we navigate to the previous know location
        if (prevMode != null) {
            startNavigation();
        } else {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(mapLocationModel.getLatitude(), mapLocationModel.getLongitude())));
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        }

        // as soon as map is ready navigation requests are allowed
        unlockNavigationModeButtons();

        //restore FabToolbarLayout if allready shown
//        if (isFabToolbarLayoutShown){
//            fabToolbarLayout.show();
//            fabToolbarLayout.show();
//        }

        // complete view appear animation
        ViewAppearAnimation.runAnimation(this.getView(), 1000);
    }

    private void startNavigation() {
        this.getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        showProgressDialog();

        this.getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        LatLng startPosition = new LatLng(currentLatitude, currentLongitude);
        LatLng destinationCoordinates = new LatLng(mapLocationModel.getLatitude(), mapLocationModel.getLongitude());
        Routing routing = new Routing.Builder()
                .travelMode(prevMode)
                .withListener(this)
                .alternativeRoutes(false)
                .waypoints(startPosition, destinationCoordinates)
                .language(Locale.getDefault().getLanguage())
                .build();
        routing.execute();
    }

    // returns the complete address based on specified location coordinates
    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this.getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(", ");
                }
                strAdd = strReturnedAddress.toString();
                log.w("My Current loction address:" + strReturnedAddress.toString(), Logger.LOGGER_DEPTH.LOGGER_METHOD);
            } else {
                strAdd = LATITUDE + ", " + LONGITUDE;
                log.w("My Current loction address: No Address returned. Specified Location coordinates returned.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            strAdd = LATITUDE + ", " + LONGITUDE;
            log.w("My Current loction address: Canont get Address.  Specified Location coordinates returned.");
        }
        return strAdd;
    }


    // The immersive fullscreen mode of Android does not work correctly with a Dialog because the window focus changes
    // and forces a appearance of the soft buttons. To avoid this you have to set the new dialog as non focusable
    private void showProgressDialog() {
        int styleID = getResources().getIdentifier(colorTheme.getAlerDialogStyle(), "style", getContext().getPackageName());
        progressDialog = new SpotsDialog(this.getContext(), getString(R.string.alert_dialog_title), styleID);
        //Set the dialog to not focusable (makes navigation ignore us adding the window)
        progressDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        progressDialog.show();
        //Set the dialog to immersive
        progressDialog.getWindow().getDecorView().setSystemUiVisibility(this.getActivity().getWindow().getDecorView().getSystemUiVisibility());

        //Clear the not focusable flag from the window
        this.getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
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

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000); // Update location every second

        if (!this.isFragmentFinished()) {
            //set this class as LocationListener
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

        //retrieve last location
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

    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
        // check if location is within the destination radius
        if (location.distanceTo(destinationLocation) <= mapLocationModel.getRadius() && !isFragmentFinished()) {
            //this avoid more onLocationChanged events of entering this if clause
            finishNavigation();
        }
    }

    private void finishNavigation() {
        //Checks if Fragment is attached to activity. This is necessary because otherwise it might causes a exception while rotating.
        if (isAdded()) {
            transformFabToContinueButton();
        }
    }

    private void startUserFeedback() {
        //hide toolbar
        fabToolbarLayout.hide();
        getSoundService().playSound(R.raw.navigation_success);
        Vibrator v = (Vibrator) this.getContext().getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(pattern, -1);
        SnackbarService.dismiss();
        fabToolbarLayout.postDelayed(new Runnable() {
            @Override
            public void run() {

                fabToolbarLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SnackbarService.showSnackbar(getContext(), coordinatorLayout, colorTheme.getSecondaryColor(), getString(R.string.map_fragment_location_reached_message), Snackbar.LENGTH_INDEFINITE);
                    }
                }, 500);
            }
        }, 200);
    }

    protected void transformFabToContinueButton() {
        //transform animation. Animation is delayed, because of the user feedback above takes some time (Especially the toolbar hide animation)
        fabToolbarLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isAdded()) {
                    ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.bright_green));
                    Drawable drawable = getResources().getDrawable(R.drawable.ic_arrow_forward_24dp, null);
                    FabTransformAnimation fabTransformAnimation = new FabTransformAnimation(fabtoolbar_fab, colorStateList, drawable);
                    fabTransformAnimation.setAnimationFinishedListener(new AnimationFinishedListener() {
                        @Override
                        public void onAnimationFinished() {
                            prepareNextFragmentNavigation();
                            startUserFeedback();
                        }
                    });
                    fabTransformAnimation.runAnimation();
                }
            }
        }, 500);
    }

    private void prepareNextFragmentNavigation() {
        fragmentFinished(fabtoolbar_fab, mRevealView, mRevealLayout, new TransactionCallback() {
            @Override
            public void onClick() {
                SnackbarService.dismiss();
            }
        });
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
        getSoundService().stop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        progressDialog.dismiss();
        SnackbarService.showSnackbar(this.getContext(), coordinatorLayout, colorTheme.getSecondaryColor(), getString(R.string.map_fragment_connection_error_message), Snackbar.LENGTH_INDEFINITE, true);
    }

    @Override
    public void onRoutingStart() {
        // not unessessary
    }

    @Override
    public void onRoutingSuccess(List<Route> route, int shortestRouteIndex) {
        LatLng startPosition = new LatLng(currentLatitude, currentLongitude);
        LatLng destinationCoordinates = new LatLng(mapLocationModel.getLatitude(), mapLocationModel.getLongitude());

        //get routing instruction
        currentSnackbarDescription = route.get(0).getSegments().get(0).getInstruction();

        // Is necessary to make a smooth animation between two Snackbar apperances.
        // The first postDelayed gives enough time for the snackbar to vanish, before the next one is shown
        fabToolbarLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                SnackbarService.dismiss();
                fabToolbarLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SnackbarService.showSnackbar(getContext(), coordinatorLayout, colorTheme.getSecondaryColor(), currentSnackbarDescription, Snackbar.LENGTH_INDEFINITE, false);
                    }
                }, 500);

            }
        }, 500);

        CameraUpdate center = CameraUpdateFactory.newLatLng(startPosition);
        mMap.moveCamera(center);

        //remove previous Polylines
        if (polylines.size() > 0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();

        // add Polylines asynchronously
        mHandler.post(new ApplyDirectionResultAsyn(route));

        // End marker
        MarkerOptions options = new MarkerOptions();
        options = new MarkerOptions();
        options.position(destinationCoordinates);
        options.title(getString(R.string.map_fragment_destination_title));
        options.snippet(route.get(0).getEndAddressText());
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mMap.addMarker(options);

        progressDialog.dismiss();
        destinationMarker.remove();
    }

    // This Runnable draws all Polylines for a specific rout on the GoogleMap
    class ApplyDirectionResultAsyn implements Runnable {
        private List<Route> route;

        public ApplyDirectionResultAsyn(List<Route> route) {
            this.route = route;
        }

        @Override
        public void run() {
            for (int i = 0; i < route.size(); i++) {
                PolylineOptions polyOptions = new PolylineOptions();
                polyOptions.color(polylineColor);
                polyOptions.width(10 + i * 3);
                polyOptions.addAll(route.get(i).getPoints());
                Polyline polyline = mMap.addPolyline(polyOptions);
                polylines.add(polyline);
            }
        }
    }

    @Override
    public void onRoutingCancelled() {
        // is not allowed user interaction (no UI element)
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
