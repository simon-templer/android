package ch.templer.model;

import java.io.Serializable;

/**
 * Created by Templer on 04.04.2016.
 */
public abstract class AbstractMessageModel implements Serializable {

    private FragmentColors fragmentColors;
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

    public FragmentColors getFragmentColors() {
        return fragmentColors;
    }

    public void setFragmentColors(FragmentColors fragmentColors) {
        this.fragmentColors = fragmentColors;
    }
}
