package ch.templer.services.navigation;

/**
 * Created by Templer on 6/9/2016.
 */
public enum NavigationMode{
    DRIVING("driving") ,TRANSIT("transit"), WALKING("walking"), BICYCLING("bicycling");

    private String value;

    NavigationMode(String navigationMode){
        this.value = navigationMode;
    }

    public String getValue() {
        return value;
    }
}
