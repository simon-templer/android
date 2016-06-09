package ch.templer.services.navigation;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;

public class Navigator {
    @SuppressWarnings("unused")
    private Context context;
    private LatLng startPosition, endPosition;
    private String mode;
    private GoogleMap map;
    private Directions directions;
    private int pathColor = Color.BLUE;
    private int pathBorderColor = Color.BLUE;
    private int secondPath = Color.CYAN;
    private int thirdPath = Color.RED;
    private float pathWidth = 8;
    private OnPathSetListener listener;
    private boolean alternatives = false;
    private String avoid;
    private ArrayList<Polyline> lines = new ArrayList<Polyline>();
    private NavigationDescriptionListener navigationDescriptionListener;

    public Navigator(GoogleMap map, LatLng startLocation, LatLng endLocation) {
        this.startPosition = startLocation;
        this.endPosition = endLocation;
        this.map = map;
    }

    public interface OnPathSetListener {
        public void onPathSetListener(Directions directions);
    }

    public void setNavigationDescriptionListener(NavigationDescriptionListener navigationDescriptionListener) {
        this.navigationDescriptionListener = navigationDescriptionListener;
    }

    public void clear() {
        for (Polyline polyline : lines) {
            polyline.remove();
        }
        lines.clear();
    }

    public void setOnPathSetListener(OnPathSetListener listener) {
        this.listener = listener;
    }

    /**
     * Gets the starting location for the directions
     */
    public LatLng getStartPoint() {
        return startPosition;
    }

    public void setStartPosition(LatLng startPosition) {
        this.startPosition = startPosition;
    }

    /**
     * Gets the end location for the directions
     */
    public LatLng getEndPoint() {
        return endPosition;
    }

    /**
     * Get's driving directions from the starting location to the ending location
     * <p/>
     * Set to true if you want to show a ProgressDialog while retrieving directions
     *
     * @param findAlternatives give alternative routes to the destination
     */
    public void findDirections(boolean findAlternatives) {
        this.alternatives = findAlternatives;
        new PathCreator().execute();
    }

    public void setMode(NavigationMode mode, AvoidMode avoid) {
        switch (mode) {
            case DRIVING:
                this.mode = NavigationMode.DRIVING.getValue();
                break;
            case TRANSIT:
                this.mode = NavigationMode.TRANSIT.getValue();
                break;
            case BICYCLING:
                this.mode = NavigationMode.BICYCLING.getValue();
                break;
            case WALKING:
                this.mode = NavigationMode.WALKING.getValue();
                break;
            default:
                this.mode = NavigationMode.DRIVING.getValue();
                break;
        }

        switch (avoid) {
            case HIGHWAYS:
                this.avoid = AvoidMode.HIGHWAYS.getValue();
                break;
            case TOLLS:
                this.avoid = AvoidMode.TOLLS.getValue();
                break;
            default:
                break;
        }
    }

    /**
     * Get all direction information
     *
     * @return
     */
    public Directions getDirections() {
        return directions;
    }

    /**
     * Change the color of the path line, must be called before calling findDirections().
     *
     * @param firstPath  Color of the first line, default color is blue.
     * @param secondPath Color of the second line, default color is cyan
     * @param thirdPath  Color of the third line, default color is red
     */
    public void setPathColor(int firstPath, int secondPath, int thirdPath) {
        pathColor = firstPath;
    }

    public void setPathBorderColor(int firstPath, int secondPath, int thirdPath) {
        pathBorderColor = firstPath;
    }

    /**
     * Change the width of the path line
     *
     * @param width Width of the line, default width is 3
     */
    public void setPathLineWidth(float width) {
        pathWidth = width;
    }

    private Polyline showPath(Route route, int color) {
        return map.addPolyline(new PolylineOptions().addAll(route.getPath()).color(color).width(pathWidth));
    }

    private Polyline showBorderPath(Route route, int color) {
        return map.addPolyline(new PolylineOptions().addAll(route.getPath()).color(color).width(pathWidth + 12));
    }

    public ArrayList<Polyline> getPathLines() {
        return lines;
    }

    private class PathCreator extends AsyncTask<Void, Void, Directions> {

        @Override
        protected Directions doInBackground(Void... params) {
            if (mode == null) {
                mode = "driving";
            }

            String url = "http://maps.googleapis.com/maps/api/directions/json?"
                    + "origin=" + startPosition.latitude + "," + startPosition.longitude
                    + "&destination=" + endPosition.latitude + "," + endPosition.longitude
                    + "&sensor=false&units=metric&mode=" + mode + "&alternatives=" + String.valueOf(alternatives);

            if (avoid != null) {
                url += url + "&avoid=" + avoid;
            }

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                HttpPost httpPost = new HttpPost(url);
                HttpResponse response = httpClient.execute(httpPost, localContext);

                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

                    String s = EntityUtils.toString(response.getEntity());
                    Directions direction = new Directions(s);
                    if (navigationDescriptionListener != null) {
                        navigationDescriptionListener.onNavigationDescription(prepareDescription(direction));
                    }
                    return direction;
                }


                return null;
            } catch (Exception e) {
                navigationDescriptionListener.onNavigationDescription("A Problem occured while connecting to the navigation service. Is a internet connection present?");
                return null;
            }
        }

        @Override
        protected void onPostExecute(Directions directions) {
            if (directions != null) {
                Navigator.this.directions = directions;
                for (int i = 0; i < directions.getRoutes().size(); i++) {
                    Route r = directions.getRoutes().get(i);
                    if (i == 0) {
                        lines.add(showBorderPath(r, pathBorderColor));
                        lines.add(showPath(r, pathColor));
                    } else if (i == 1) {
                        lines.add(showBorderPath(r, pathBorderColor));
                        lines.add(showPath(r, secondPath));
                    } else if (i == 3) {
                        lines.add(showBorderPath(r, pathBorderColor));
                        lines.add(showPath(r, thirdPath));
                    }
                }

                if (listener != null) {
                    listener.onPathSetListener(directions);
                }
            }
        }
    }

    private String prepareDescription(Directions direction) {
        String description = "";
        for (Route route : direction.getRoutes()) {
            for (Legs leg : route.getLegs()) {
                for (Steps step : leg.getSteps()) {
                    description = step.getStepInstructions();
                    break;
                }
            }
        }
        return description;
    }

    public interface NavigationDescriptionListener {
        void onNavigationDescription(String descitpion);
    }
}
