package ch.templer.services.navigation;

/**
 * Created by Templer on 6/9/2016.
 */
public enum AvoidMode {
    TOLLS("tolls"), HIGHWAYS("highways");

    private String value;

    AvoidMode(String avoidMode) {
        this.value = avoidMode;
    }

    public String getValue() {
        return value;
    }


}
