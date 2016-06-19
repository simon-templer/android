package ch.templer.fragments.mapfragment;

import android.os.AsyncTask;
import android.os.Handler;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import ch.templer.services.navigation.Directions;
import ch.templer.services.navigation2.Route;

/**
 * Created by Templer on 6/11/2016.
 */
public class ApplyDirectionResultAsyn extends AsyncTask<List<Route>, Void, List<Polyline>> {
    GoogleMap map;

    public ApplyDirectionResultAsyn(GoogleMap map) {
        this.map = map;
    }

    private Handler mHandler = new Handler();



    @Override
    protected List<Polyline> doInBackground(List<Route>... routes) {
        List<Route> route;
        List<Polyline> polylines = new ArrayList<>();
        if (routes.length >= 1){
            route = routes[0];
            //add route(s) to the map.
            for (int i = 0; i <route.size(); i++) {
                PolylineOptions polyOptions = new PolylineOptions();
                //polyOptions.color(polylineColor);
                polyOptions.width(10 + i * 3);
                polyOptions.addAll(route.get(i).getPoints());
                mHandler.post( new test(polyOptions));
            }

        }
        return polylines;
    }

    private class test implements Runnable {
        private PolylineOptions polyOptions;
        public test(PolylineOptions polyOptions){
            this.polyOptions = polyOptions;
        }
        @Override
        public void run() {
            Polyline polyline = map.addPolyline(polyOptions);
            //polylines.add(polyline);
        }
    }

    @Override
    protected void onPostExecute(List<Polyline> polylines) {
        super.onPostExecute(polylines);
    }
}
