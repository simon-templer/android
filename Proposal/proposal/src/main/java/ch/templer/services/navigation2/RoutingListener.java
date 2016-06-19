package ch.templer.services.navigation2;

/**
 * Created by Templer on 6/11/2016.
 */
import java.util.List;

public interface RoutingListener {
    void onRoutingFailure(RouteException e);

    void onRoutingStart();

    void onRoutingSuccess(List<Route> route, int shortestRouteIndex);

    void onRoutingCancelled();
}