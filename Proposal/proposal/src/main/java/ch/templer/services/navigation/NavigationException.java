package ch.templer.services.navigation;

/**
 * Created by Templer on 6/9/2016.
 */
public class NavigationException extends Throwable {
    public NavigationException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
    public NavigationException(Throwable throwable) {
        super(throwable);
    }
}
