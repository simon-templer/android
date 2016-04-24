package ch.templer.model;

/**
 * Created by Templer on 04.04.2016.
 */
public abstract class MessageData {
    private int backgroundColor;
    private int nextFragmentBackroundColor;

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getNextFragmentBackroundColor() {
        return nextFragmentBackroundColor;
    }

    public void setNextFragmentBackroundColor(int nextFragmentBackroundColor) {
        this.nextFragmentBackroundColor = nextFragmentBackroundColor;
    }
}
