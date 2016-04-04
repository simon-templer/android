package ch.templer.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import ch.templer.data.TestData;
import ch.templer.location.LocationService;

public class StartActivity extends AppCompatActivity implements LocationService.LocationChangedListener, LocationService.LocationReachedListener, View.OnClickListener {

    private LocationService locationService;
    private EditText coordinateContainer;
    private double longitude = 7.437491;
    private double latitude = 46.947213;
    //radius in meters
    private int radius = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        if ((ContextCompat.checkSelfPermission((Activity) this,
//                Manifest.permission.ACCESS_COARSE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission((Activity) this,
//                Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED)) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
//
//            } else {
//                ActivityCompat.requestPermissions((Activity) this,
//                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
//                        1);
//            }
//        } else {
            locationService = new LocationService(this, 1000, 1);
            locationService.setLocationChangedListener(this);
            locationService.setLocationReachedListener(this, longitude, latitude, radius);
       // }
        coordinateContainer = (EditText) findViewById(R.id.coordinateContainer);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                }
                return;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_fragments) {
            Intent intent = new Intent(this, FragmentContainerActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(Location location) {
        String msg = location.getLatitude() + ", " + location.getLongitude();
        coordinateContainer.getText().append("\n");
        coordinateContainer.getText().append(msg);
    }

    @Override
    public void onLocationReached(Location location) {
        String msg = location.getLatitude() + ", " + location.getLongitude();
        Toast.makeText(this, "Location reached event", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {

    }
}
