package ch.templer.services.navigation2;

/**
 * Created by Templer on 6/11/2016.
 */
import java.util.List;

public interface Parser {
    List<Route> parse() throws RouteException;
}
